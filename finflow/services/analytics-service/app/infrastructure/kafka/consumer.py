import asyncio
import json
import logging
import os
from typing import Callable, Awaitable

from aiokafka import AIOKafkaConsumer
from aiokafka.errors import KafkaConnectionError, KafkaError

from .events import PortfolioEvent

logger = logging.getLogger(__name__)


class KafkaConsumerService:
    """
    Async Kafka consumer with automatic reconnection and error handling.

    Features:
    - Graceful handling when Kafka is unavailable
    - Auto-reconnect with exponential backoff
    - Manual offset commit after successful processing
    - Configurable message handlers
    """

    def __init__(self):
        self._consumer: AIOKafkaConsumer | None = None
        self._running = False
        self._task: asyncio.Task | None = None

        self.bootstrap_servers = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092")
        self.group_id = os.getenv("KAFKA_CONSUMER_GROUP", "analytics-service")
        self.topics = [os.getenv("KAFKA_TOPIC_PORTFOLIO", "portfolio.updated")]

        self._handlers: dict[str, Callable[[PortfolioEvent], Awaitable[None]]] = {}

    def register_handler(
        self, action: str, handler: Callable[[PortfolioEvent], Awaitable[None]]
    ):
        """Register a handler for a specific event action."""
        self._handlers[action] = handler

    async def start(self):
        """Start the consumer in a background task."""
        if self._running:
            logger.warning("Consumer already running")
            return

        self._running = True
        self._task = asyncio.create_task(self._consume_loop())
        logger.info("Kafka consumer started")

    async def stop(self):
        """Stop the consumer gracefully."""
        self._running = False

        if self._task:
            self._task.cancel()
            try:
                await self._task
            except asyncio.CancelledError:
                pass

        if self._consumer:
            await self._consumer.stop()
            self._consumer = None

        logger.info("Kafka consumer stopped")

    async def _create_consumer(self) -> AIOKafkaConsumer:
        """Create and configure the Kafka consumer."""
        return AIOKafkaConsumer(
            *self.topics,
            bootstrap_servers=self.bootstrap_servers,
            group_id=self.group_id,
            auto_offset_reset="earliest",
            enable_auto_commit=False,  # Manual commit for reliability
            value_deserializer=lambda m: json.loads(m.decode("utf-8")),
        )

    async def _consume_loop(self):
        """Main consume loop with reconnection logic."""
        backoff = 1
        max_backoff = 60

        while self._running:
            try:
                self._consumer = await self._create_consumer()
                await self._consumer.start()
                logger.info(f"Connected to Kafka, subscribed to {self.topics}")
                backoff = 1  # Reset backoff on successful connection

                async for msg in self._consumer:
                    if not self._running:
                        break

                    await self._process_message(msg)

            except KafkaConnectionError as e:
                logger.warning(
                    f"Kafka connection failed: {e}. Retrying in {backoff}s..."
                )
                await asyncio.sleep(backoff)
                backoff = min(backoff * 2, max_backoff)

            except KafkaError as e:
                logger.error(f"Kafka error: {e}. Retrying in {backoff}s...")
                await asyncio.sleep(backoff)
                backoff = min(backoff * 2, max_backoff)

            except asyncio.CancelledError:
                logger.info("Consumer loop cancelled")
                break

            except Exception as e:
                logger.exception(f"Unexpected error in consumer: {e}")
                await asyncio.sleep(backoff)
                backoff = min(backoff * 2, max_backoff)

            finally:
                if self._consumer:
                    try:
                        await self._consumer.stop()
                    except Exception:
                        pass
                    self._consumer = None

    async def _process_message(self, msg):
        """Process a single message with error handling."""
        try:
            logger.info(
                f"Received message: topic={msg.topic} partition={msg.partition} "
                f"offset={msg.offset} key={msg.key}"
            )

            event = PortfolioEvent.model_validate(msg.value)
            logger.info(
                f"Portfolio event: user={event.user_id} symbol={event.symbol} "
                f"action={event.action}"
            )

            # Call registered handler if exists
            handler = self._handlers.get(event.action)
            if handler:
                await handler(event)
            else:
                await self._default_handler(event)

            # Commit offset after successful processing
            await self._consumer.commit()

        except Exception as e:
            # Log but don't crash - let the message be reprocessed on restart
            # In production, you might want dead-letter queue here
            logger.error(f"Failed to process message at offset {msg.offset}: {e}")
            # Still commit to avoid infinite loop on bad messages
            # Alternative: send to DLQ and then commit
            await self._consumer.commit()

    async def _default_handler(self, event: PortfolioEvent):
        """Default handler - just logs the event."""
        logger.info(
            f"Processed {event.action} for {event.symbol} (user: {event.user_id})"
        )


# Singleton instance
_consumer: KafkaConsumerService | None = None


def get_kafka_consumer() -> KafkaConsumerService:
    global _consumer
    if _consumer is None:
        _consumer = KafkaConsumerService()
    return _consumer

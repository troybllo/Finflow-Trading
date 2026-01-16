import redis.asyncio as redis
from redis.asyncio.connection import ConnectionPool
import os
import json
import logging
from typing import Any
from datetime import timedelta

logger = logging.getLogger(__name__)


class AsyncRedisClient:
    """Async Redis client with connection pooling and helper methods."""

    def __init__(self):
        self._pool: ConnectionPool | None = None
        self._client: redis.Redis | None = None

    async def connect(self) -> None:
        """Initialize connection pool and connect to Redis."""
        if self._client is not None:
            return

        self._pool = ConnectionPool(
            host=os.getenv("REDIS_HOST", "localhost"),
            port=int(os.getenv("REDIS_PORT", 6379)),
            db=int(os.getenv("REDIS_DB", 0)),
            password=os.getenv("REDIS_PASSWORD"),
            max_connections=int(os.getenv("REDIS_MAX_CONNECTIONS", 50)),
            decode_responses=True,
        )
        self._client = redis.Redis(connection_pool=self._pool)
        await self._client.ping()
        logger.info("Connected to Redis")

    async def close(self) -> None:
        """Close client and pool."""
        if self._client:
            await self._client.close()
            self._client = None
        if self._pool:
            await self._pool.disconnect()
            self._pool = None
        logger.info("Redis connection closed")

    @property
    def client(self) -> redis.Redis:
        if self._client is None:
            raise RuntimeError("Redis client not connected. Call connect() first.")
        return self._client

    async def ping(self) -> bool:
        """Health check."""
        try:
            return await self.client.ping()
        except Exception:
            return False

    # -------------------------------------------------------------------------
    # String Operations
    # -------------------------------------------------------------------------

    async def get(self, key: str) -> str | None:
        return await self.client.get(key)

    async def set(
        self, key: str, value: str, ttl: int | timedelta | None = None
    ) -> bool:
        if ttl is None:
            return await self.client.set(key, value)
        if isinstance(ttl, timedelta):
            ttl = int(ttl.total_seconds())
        return await self.client.setex(key, ttl, value)

    async def delete(self, *keys: str) -> int:
        if not keys:
            return 0
        return await self.client.delete(*keys)

    async def exists(self, key: str) -> bool:
        return await self.client.exists(key) > 0

    async def expire(self, key: str, ttl: int | timedelta) -> bool:
        if isinstance(ttl, timedelta):
            ttl = int(ttl.total_seconds())
        return await self.client.expire(key, ttl)

    async def ttl(self, key: str) -> int:
        return await self.client.ttl(key)

    # -------------------------------------------------------------------------
    # JSON Helpers
    # -------------------------------------------------------------------------

    async def get_json(self, key: str) -> Any | None:
        value = await self.client.get(key)
        if value is None:
            return None
        try:
            return json.loads(value)
        except json.JSONDecodeError:
            logger.error(f"Failed to decode JSON for key: {key}")
            return None

    async def set_json(
        self, key: str, value: Any, ttl: int | timedelta | None = None
    ) -> bool:
        serialized = json.dumps(value, default=str)
        return await self.set(key, serialized, ttl=ttl)

    # -------------------------------------------------------------------------
    # Cache-Aside Pattern
    # -------------------------------------------------------------------------

    async def get_or_set(
        self, key: str, fetch_func, ttl: int | timedelta | None = None
    ) -> Any:
        """Get from cache or fetch and cache."""
        cached = await self.get_json(key)
        if cached is not None:
            return cached

        value = await fetch_func()
        if value is not None:
            await self.set_json(key, value, ttl=ttl)
        return value

    # -------------------------------------------------------------------------
    # Counter Operations
    # -------------------------------------------------------------------------

    async def incr(self, key: str, amount: int = 1) -> int:
        return await self.client.incrby(key, amount)

    async def decr(self, key: str, amount: int = 1) -> int:
        return await self.client.decrby(key, amount)

    # -------------------------------------------------------------------------
    # Hash Operations
    # -------------------------------------------------------------------------

    async def hget(self, name: str, key: str) -> str | None:
        return await self.client.hget(name, key)

    async def hset(self, name: str, mapping: dict[str, Any]) -> int:
        return await self.client.hset(name, mapping=mapping)

    async def hgetall(self, name: str) -> dict[str, str]:
        return await self.client.hgetall(name)

    async def hincrby(self, name: str, key: str, amount: int = 1) -> int:
        return await self.client.hincrby(name, key, amount)


# Singleton instance
_redis_client: AsyncRedisClient | None = None


def get_redis_client() -> AsyncRedisClient:
    """Get the global Redis client instance."""
    global _redis_client
    if _redis_client is None:
        _redis_client = AsyncRedisClient()
    return _redis_client


async def init_redis() -> AsyncRedisClient:
    """Initialize and connect the global Redis client."""
    client = get_redis_client()
    await client.connect()
    return client


async def close_redis() -> None:
    """Close the global Redis client."""
    global _redis_client
    if _redis_client is not None:
        await _redis_client.close()
        _redis_client = None

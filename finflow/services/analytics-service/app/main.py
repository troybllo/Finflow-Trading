"""
FinFlow Analytics Service
========================
FastAPI-based analytics and AI service for market insights.

Day 1: FastAPI skeleton setup
"""

import logging

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager

from app.api import health, analytics
from app.infrastructure.cache.redis_client import get_redis_client
from app.infrastructure.kafka import get_kafka_consumer

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Application lifespan events - startup and shutdown."""
    logger.info("Analytics Service starting up...")

    app.state.vector_store = None
    app.state.embedding_model = None

    redis_client = get_redis_client()
    try:
        await redis_client.connect()
        app.state.redis = redis_client
    except Exception as e:
        logger.warning(f"Redis unavailable, caching disabled: {e}")
        app.state.redis = None

    kafka_consumer = get_kafka_consumer()
    try:
        await kafka_consumer.start()
        app.state.kafka_consumer = kafka_consumer
    except Exception as e:
        logger.warning(f"Kafka consumer failed to start: {e}")
        app.state.kafka_consumer = None

    yield

    if app.state.kafka_consumer:
        await app.state.kafka_consumer.stop()
    if app.state.redis:
        await app.state.redis.close()
    logger.info("Analytics Service shutting down...")


app = FastAPI(
    title="FinFlow Analytics Service",
    description="AI-powered market analytics and insights using RAG",
    version="0.1.0",
    lifespan=lifespan,
)

# CORS middleware for frontend communication
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],  # React frontend
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Register routers
app.include_router(health.router, tags=["Health"])
app.include_router(analytics.router, prefix="/api/v1", tags=["Analytics"])


@app.get("/")
async def root():
    """Root endpoint - service info."""
    return {
        "service": "FinFlow Analytics",
        "version": "0.1.0",
        "status": "running",
        "message": "Welcome to the FinFlow Analytics Service!",
    }

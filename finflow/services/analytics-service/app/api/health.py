"""Health check endpoints for monitoring and orchestration."""

from fastapi import APIRouter, Request
from pydantic import BaseModel
from datetime import datetime

import asyncio

router: APIRouter = APIRouter()


class HealthResponse(BaseModel):
    """Health check response model."""

    status: str
    timestamp: datetime
    service: str
    version: str


class ReadinessResponse(BaseModel):
    """Readiness check response - includes dependency status."""

    status: str
    timestamp: datetime
    dependencies: dict[str, bool]


@router.get("/health", response_model=HealthResponse)
async def health_check() -> HealthResponse:
    """
    Basic health check - is the service running?
    Used by: Load balancers, Kubernetes liveness probes
    """
    return HealthResponse(
        status="healthy",
        timestamp=datetime.utcnow(),
        service="analytics-service",
        version="0.1.0",
    )


async def check_postgres_connection(request: Request) -> bool:
    """Check PostgreSQL database connection."""
    try:
        async with asyncio.timeout(2.0):
            if not hasattr(request.app.state, "db_pool"):
                return False
            pool = request.app.state.db_pool
            async with pool.acquire() as connection:
                await connection.execute("SELECT 1")
            return True
    except Exception as e:
        print(f"PostgreSQL connection check failed : {e}")
        return False


async def check_vector_store_connection(request: Request) -> bool:
    """Check vector store (Pinecone/Weaviate) connection."""
    try:
        async with asyncio.timeout(2.0):
            if not hasattr(request.app.state, "vector_store"):
                return False
            vector_store = request.app.state.vector_store
            # Check for Pinecone
            if hasattr(vector_store, "describe_index_stats"):
                vector_store.describe_index_stats()
                return True

            # Check for Weaviate
            elif hasattr(vector_store, "is_ready"):
                return vector_store.is_ready()

                # Fallback - check if schem method exists (Weaviate)
            elif hasattr(vector_store, "schema"):
                vector_store.schema.get()
                return True
        return True
    except Exception as e:
        print(f"Vector store connection check failed: {e}")
        return False


async def check_redis_connection(request: Request) -> bool:
    """Check Redis connection."""
    try:
        async with asyncio.timeout(2.0):
            if not hasattr(request.app.state, "redis"):
                return False
            redis = request.app.state.redis
            await redis.ping()
            return True
    except Exception as e:
        print(f"Redis connection check failed: {e}")
        return False


@router.get("/ready", response_model=ReadinessResponse)
async def readiness_check(request: Request) -> ReadinessResponse:
    """
    Readiness check - is the service ready to accept traffic?
    Checks all dependencies (DB, vector store, etc.)
    Used by: Kubernetes readiness probes, deployment orchestration
    """
    dependencies = {
        "database": await check_postgres_connection(request),
        "vector_store": await check_vector_store_connection(request),
        "redis": await check_redis_connection(request),
    }

    all_ready = all(dependencies.values())

    return ReadinessResponse(
        status="ready" if all_ready else "not_ready",
        timestamp=datetime.utcnow(),
        dependencies=dependencies,
    )

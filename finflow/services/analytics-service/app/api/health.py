"""Health check endpoints for monitoring and orchestration."""

from fastapi import APIRouter
from pydantic import BaseModel
from datetime import datetime

router = APIRouter()


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
async def health_check():
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


@router.get("/ready", response_model=ReadinessResponse)
async def readiness_check():
    """
    Readiness check - is the service ready to accept traffic?
    Checks all dependencies (DB, vector store, etc.)
    Used by: Kubernetes readiness probes, deployment orchestration
    """
    # TODO: Add actual dependency checks
    dependencies = {
        "database": True,  # TODO: Check PostgreSQL connection
        "vector_store": True,  # TODO: Check Pinecone/Weaviate connection
        "redis": True,  # TODO: Check Redis connection
    }

    all_ready = all(dependencies.values())

    return ReadinessResponse(
        status="ready" if all_ready else "not_ready",
        timestamp=datetime.utcnow(),
        dependencies=dependencies,
    )

"""
FinFlow Analytics Service
========================
FastAPI-based analytics and AI service for market insights.

Day 1: FastAPI skeleton setup
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager

from app.api import health, analytics


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Application lifespan events - startup and shutdown."""
    # Startup: Initialize connections, load models, etc.
    print(" Analytics Service starting up...")
    # TODO: Initialize vector store connections
    app.state.vector_store = None
    # TODO: Load embedding models
    app.state.embedding_model = None
    yield
    # Shutdown: Cleanup resources
    print("👋 Analytics Service shutting down...")


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

"""
Tests for Analytics Service
Day 1: Basic endpoint tests
"""

import pytest
from fastapi.testclient import TestClient
from app.main import app


client = TestClient(app)


class TestHealthEndpoints:
    """Test health check endpoints."""

    def test_root(self):
        """Test root endpoint returns service info."""
        response = client.get("/")
        assert response.status_code == 200
        data = response.json()
        assert data["service"] == "FinFlow Analytics"
        assert data["status"] == "running"

    def test_health_check(self):
        """Test health endpoint returns healthy status."""
        response = client.get("/health")
        assert response.status_code == 200
        data = response.json()
        assert data["status"] == "healthy"
        assert data["service"] == "analytics-service"

    def test_readiness_check(self):
        """Test readiness endpoint returns dependency status."""
        response = client.get("/ready")
        assert response.status_code == 200
        data = response.json()
        assert "status" in data
        assert "dependencies" in data


class TestAnalyticsEndpoints:
    """Test analytics API endpoints."""

    def test_query_market(self):
        """Test market query endpoint."""
        response = client.post(
            "/api/v1/analytics/query", json={"query": "What's the sentiment on AAPL?"}
        )
        assert response.status_code == 200
        data = response.json()
        assert data["query"] == "What's the sentiment on AAPL?"
        assert "insights" in data

    def test_analyze_portfolio(self):
        """Test portfolio analysis endpoint."""
        response = client.post(
            "/api/v1/analytics/portfolio", json={"user_id": "test-user-123"}
        )
        assert response.status_code == 200
        data = response.json()
        assert data["user_id"] == "test-user-123"

    def test_get_symbol_analysis(self):
        """Test symbol analysis endpoint."""
        response = client.get("/api/v1/analytics/symbols/AAPL")
        assert response.status_code == 200
        data = response.json()
        assert data["symbol"] == "AAPL"


# ============================================================
# Day 1 Challenges - Implement these!
# ============================================================


class TestDay1Challenges:
    """
    Your Day 1 mini-challenges:

    1. Add a new endpoint: GET /api/v1/analytics/trending
       - Should return a list of trending symbols
       - Add the test below first (TDD!)

    2. Add request validation:
       - MarketQueryRequest.query should have min_length=3
       - Test that empty queries return 422

    3. Add a custom exception handler:
       - Create a custom AnalyticsException
       - Return proper error responses
    """

    @pytest.mark.skip(reason="Implement this endpoint first!")
    def test_get_trending_symbols(self):
        """Test trending symbols endpoint - YOUR TURN!"""
        response = client.get("/api/v1/analytics/trending")
        assert response.status_code == 200
        data = response.json()
        assert "symbols" in data
        assert isinstance(data["symbols"], list)

    def test_query_validation_empty(self):
        """Test that empty queries are rejected - YOUR TURN!"""
        response = client.post("/api/v1/analytics/query", json={"query": ""})
        assert response.status_code == 422

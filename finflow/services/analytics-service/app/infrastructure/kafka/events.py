from datetime import datetime
from typing import Any
from pydantic import BaseModel, Field
from enum import Enum


class EventType(str, Enum):
    HOLDING_CREATED = "HOLDING_CREATED"
    HOLDING_ADDED = "HOLDING_ADDED"
    HOLDING_UPDATED = "HOLDING_UPDATED"
    HOLDING_SOLD = "HOLDING_SOLD"
    HOLDING_DELETED = "HOLDING_DELETED"


class PortfolioEvent(BaseModel):
    """Event received from portfolio-service."""
    user_id: str = Field(alias="userId")
    portfolio_id: str = Field(alias="portfolioId")
    symbol: str
    action: str
    timestamp: datetime

    class Config:
        populate_by_name = True


class EventEnvelope(BaseModel):
    """
    Standard event wrapper for all Kafka messages.
    Use this when you control both producer and consumer.
    """
    event_id: str = Field(alias="eventId")
    event_type: str = Field(alias="eventType")
    timestamp: datetime
    payload: dict[str, Any]

    class Config:
        populate_by_name = True

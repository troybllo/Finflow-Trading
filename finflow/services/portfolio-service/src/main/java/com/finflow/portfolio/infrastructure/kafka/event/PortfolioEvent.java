package com.finflow.portfolio.infrastructure.kafka.event;

import java.time.Instant;

public record PortfolioEvent(
    String userId,
    String portfolioId,
    String symbol,
    String action,
    Instant timestamp
) {
    public static PortfolioEvent of(String userId, String portfolioId, String symbol, String action) {
        return new PortfolioEvent(userId, portfolioId, symbol, action, Instant.now());
    }
}

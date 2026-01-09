package com.finflow.portfolio.dto.response;

import com.finflow.portfolio.domain.Portfolio;

import java.math.BigDecimal;

/**
 * Lightweight portfolio summary for lists and quick views.
 * Use PortfolioResponse for full details with holdings.
 */
public record PortfolioSummaryResponse(
    String id,
    String userId,
    String name,
    BigDecimal totalValue,
    BigDecimal dailyChange,
    BigDecimal dailyChangePercent,
    BigDecimal totalGainLoss,
    BigDecimal totalGainLossPercent,
    int holdingsCount
) {
    /**
     * Factory method to create PortfolioSummaryResponse from Portfolio entity
     */
    public static PortfolioSummaryResponse from(Portfolio portfolio) {
        return new PortfolioSummaryResponse(
            portfolio.getId(),
            portfolio.getUser() != null ? portfolio.getUser().getId() : null,
            portfolio.getName(),
            portfolio.getTotalValue(),
            portfolio.getDailyChange(),
            portfolio.getDailyChangePercent(),
            portfolio.getTotalGainLoss(),
            portfolio.getTotalGainLossPercent(),
            portfolio.getHoldings() != null ? portfolio.getHoldings().size() : 0
        );
    }
}

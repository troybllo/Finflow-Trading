package com.finflow.portfolio.dto.response;

import com.finflow.portfolio.domain.AssetType;
import com.finflow.portfolio.domain.Holding;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for holding/position data.
 */
public record HoldingResponse(
    String id,
    String portfolioId,
    String userId,
    String symbol,
    BigDecimal quantity,
    BigDecimal averageCost,
    BigDecimal currentPrice,
    BigDecimal marketValue,
    BigDecimal unrealizedPnL,
    BigDecimal unrealizedPnLPercent,
    AssetType assetType,
    String exchange,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    /**
     * Factory method to create HoldingResponse from Holding entity
     */
    public static HoldingResponse from(Holding holding) {
        return new HoldingResponse(
            holding.getId(),
            holding.getPortfolio() != null ? holding.getPortfolio().getId() : null,
            holding.getUserId(),
            holding.getSymbol(),
            holding.getQuantity(),
            holding.getAverageCost(),
            holding.getCurrentPrice(),
            holding.getMarketValue(),
            holding.getUnrealizedPnL(),
            holding.getUnrealizedPnLPercent(),
            holding.getAssetType(),
            holding.getExchange(),
            holding.getCreatedAt(),
            holding.getUpdatedAt()
        );
    }
}

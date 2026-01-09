package com.finflow.portfolio.dto.response;

import com.finflow.portfolio.domain.Portfolio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Full portfolio response with all details including holdings.
 * Use PortfolioSummaryResponse for lightweight responses without holdings.
 */
public record PortfolioResponse(
    String id,
    String userId,
    String name,
    BigDecimal totalValue,
    BigDecimal cashBalance,
    BigDecimal buyingPower,
    BigDecimal dailyChange,
    BigDecimal dailyChangePercent,
    BigDecimal totalGainLoss,
    BigDecimal totalGainLossPercent,
    int holdingsCount,
    List<HoldingResponse> holdings,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    /**
     * Factory method to create PortfolioResponse from Portfolio entity without holdings
     */
    public static PortfolioResponse from(Portfolio portfolio) {
        return new PortfolioResponse(
            portfolio.getId(),
            portfolio.getUser() != null ? portfolio.getUser().getId() : null,
            portfolio.getName(),
            portfolio.getTotalValue(),
            portfolio.getCashBalance(),
            portfolio.getBuyingPower(),
            portfolio.getDailyChange(),
            portfolio.getDailyChangePercent(),
            portfolio.getTotalGainLoss(),
            portfolio.getTotalGainLossPercent(),
            portfolio.getHoldings() != null ? portfolio.getHoldings().size() : 0,
            null,  // Holdings not loaded by default
            portfolio.getCreatedAt(),
            portfolio.getUpdatedAt()
        );
    }

    /**
     * Factory method to create PortfolioResponse from Portfolio entity with holdings
     */
    public static PortfolioResponse fromWithHoldings(Portfolio portfolio) {
        List<HoldingResponse> holdingResponses = portfolio.getHoldings() != null
            ? portfolio.getHoldings().stream()
                .map(HoldingResponse::from)
                .toList()
            : List.of();

        return new PortfolioResponse(
            portfolio.getId(),
            portfolio.getUser() != null ? portfolio.getUser().getId() : null,
            portfolio.getName(),
            portfolio.getTotalValue(),
            portfolio.getCashBalance(),
            portfolio.getBuyingPower(),
            portfolio.getDailyChange(),
            portfolio.getDailyChangePercent(),
            portfolio.getTotalGainLoss(),
            portfolio.getTotalGainLossPercent(),
            holdingResponses.size(),
            holdingResponses,
            portfolio.getCreatedAt(),
            portfolio.getUpdatedAt()
        );
    }
}

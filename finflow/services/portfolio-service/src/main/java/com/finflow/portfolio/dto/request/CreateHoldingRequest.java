package com.finflow.portfolio.dto.request;

import com.finflow.portfolio.domain.AssetType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new holding/position.
 */
public record CreateHoldingRequest(
    @NotBlank(message = "Symbol is required")
    @Size(min = 1, max = 20, message = "Symbol must be between 1 and 20 characters")
    String symbol,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    BigDecimal quantity,

    @NotNull(message = "Average cost is required")
    @Positive(message = "Average cost must be positive")
    BigDecimal averageCost,

    @NotNull(message = "Asset type is required")
    AssetType assetType,

    @Size(max = 50, message = "Exchange must not exceed 50 characters")
    String exchange
) {
    /**
     * Calculate the total cost basis for this holding
     */
    public BigDecimal getTotalCostBasis() {
        return quantity.multiply(averageCost);
    }
}

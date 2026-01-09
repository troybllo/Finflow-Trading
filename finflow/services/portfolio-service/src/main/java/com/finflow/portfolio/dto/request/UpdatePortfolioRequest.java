package com.finflow.portfolio.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO for updating portfolio details.
 * All fields are optional - only provided fields will be updated.
 */
public record UpdatePortfolioRequest(
    @Size(min = 1, max = 100, message = "Portfolio name must be between 1 and 100 characters")
    String name,

    @PositiveOrZero(message = "Cash balance cannot be negative")
    BigDecimal cashBalance,

    @PositiveOrZero(message = "Buying power cannot be negative")
    BigDecimal buyingPower
) {
    /**
     * Check if any field is provided for update
     */
    public boolean hasUpdates() {
        return name != null || cashBalance != null || buyingPower != null;
    }
}

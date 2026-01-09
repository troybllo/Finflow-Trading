package com.finflow.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new portfolio.
 */
public record CreatePortfolioRequest(
    @NotBlank(message = "Portfolio name is required")
    @Size(min = 1, max = 100, message = "Portfolio name must be between 1 and 100 characters")
    String name,

    @PositiveOrZero(message = "Initial cash balance cannot be negative")
    BigDecimal initialCashBalance
) {
    /**
     * Returns the initial cash balance, defaulting to zero if not provided
     */
    public BigDecimal getInitialCashBalanceOrDefault() {
        return initialCashBalance != null ? initialCashBalance : BigDecimal.ZERO;
    }
}

package com.finflow.portfolio.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Request DTO for updating an existing holding/position.
 * All fields are optional - only provided fields will be updated.
 */
public record UpdateHoldingRequest(
    @PositiveOrZero(message = "Quantity cannot be negative")
    BigDecimal quantity,

    @Positive(message = "Average cost must be positive")
    BigDecimal averageCost,

    @Positive(message = "Current price must be positive")
    BigDecimal currentPrice,

    @Size(max = 50, message = "Exchange must not exceed 50 characters")
    String exchange
) {
    /**
     * Check if any field is provided for update
     */
    public boolean hasUpdates() {
        return quantity != null || averageCost != null || currentPrice != null || exchange != null;
    }

    /**
     * Check if this update would close the position (quantity = 0)
     */
    public boolean isClosingPosition() {
        return quantity != null && quantity.compareTo(BigDecimal.ZERO) == 0;
    }
}

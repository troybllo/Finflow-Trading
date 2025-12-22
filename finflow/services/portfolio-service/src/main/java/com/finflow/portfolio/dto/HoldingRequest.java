package com.finflow.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record HoldingRequest(
    @NotBlank String portfolioId,
    @NotBlank String assetSymbol,
    @Positive int quantity,
    @Positive double averagePrice) {
}

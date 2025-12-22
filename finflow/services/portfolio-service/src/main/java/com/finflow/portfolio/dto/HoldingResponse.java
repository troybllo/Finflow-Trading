package com.finflow.portfolio.dto;

public record HoldingResponse(String id, String portfolioId, String assetSymbol, int quantity, double averagePrice) {
}

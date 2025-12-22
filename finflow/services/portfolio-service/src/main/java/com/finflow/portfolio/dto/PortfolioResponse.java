package com.finflow.portfolio.dto;

public record PortfolioResponse(
    String id,
    String name,
    String ownerId,
    double totalValue,
    Integer holdings
) {
}

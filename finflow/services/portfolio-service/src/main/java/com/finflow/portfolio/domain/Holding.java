package com.finflow.portfolio.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "holdings", indexes = {
    @Index(name = "idx_holding_portfolio", columnList = "portfolio_id"),
    @Index(name = "idx_holding_symbol", columnList = "symbol"),
    @Index(name = "idx_holding_user_symbol", columnList = "user_id, symbol")
})
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(name = "user_id", nullable = false)
    private String userId;  // Denormalized for query performance

    @Column(nullable = false, length = 20)
    private String symbol;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal quantity;

    @Column(name = "average_cost", nullable = false, precision = 19, scale = 4)
    private BigDecimal averageCost;

    @Column(name = "current_price", precision = 19, scale = 4)
    private BigDecimal currentPrice;

    @Column(name = "market_value", precision = 19, scale = 4)
    private BigDecimal marketValue;

    @Column(name = "unrealized_pnl", precision = 19, scale = 4)
    private BigDecimal unrealizedPnL;

    @Column(name = "unrealized_pnl_percent", precision = 10, scale = 4)
    private BigDecimal unrealizedPnLPercent;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false, length = 20)
    private AssetType assetType;

    @Column(length = 50)
    private String exchange;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Holding() {
    }

    public Holding(String userId, String symbol, BigDecimal quantity, BigDecimal averageCost, AssetType assetType) {
        this.userId = userId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.averageCost = averageCost;
        this.assetType = assetType;
    }

    // Business logic method to update market value and calculate P&L
    public void updateMarketValue(BigDecimal newPrice) {
        this.currentPrice = newPrice;
        this.marketValue = quantity.multiply(newPrice).setScale(4, RoundingMode.HALF_UP);

        BigDecimal costBasis = quantity.multiply(averageCost).setScale(4, RoundingMode.HALF_UP);
        this.unrealizedPnL = marketValue.subtract(costBasis).setScale(4, RoundingMode.HALF_UP);

        if (costBasis.compareTo(BigDecimal.ZERO) != 0) {
            this.unrealizedPnLPercent = unrealizedPnL
                .divide(costBasis, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(4, RoundingMode.HALF_UP);
        } else {
            this.unrealizedPnLPercent = BigDecimal.ZERO;
        }
    }

    // Helper method to add to position (average down/up)
    public void addToPosition(BigDecimal additionalQuantity, BigDecimal price) {
        BigDecimal currentCostBasis = this.quantity.multiply(this.averageCost);
        BigDecimal additionalCostBasis = additionalQuantity.multiply(price);
        BigDecimal newQuantity = this.quantity.add(additionalQuantity);

        this.averageCost = currentCostBasis.add(additionalCostBasis)
            .divide(newQuantity, 4, RoundingMode.HALF_UP);
        this.quantity = newQuantity;

        if (this.currentPrice != null) {
            updateMarketValue(this.currentPrice);
        }
    }

    // Helper method to reduce position
    public void reducePosition(BigDecimal quantityToReduce) {
        this.quantity = this.quantity.subtract(quantityToReduce);

        if (this.currentPrice != null) {
            updateMarketValue(this.currentPrice);
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public BigDecimal getUnrealizedPnL() {
        return unrealizedPnL;
    }

    public void setUnrealizedPnL(BigDecimal unrealizedPnL) {
        this.unrealizedPnL = unrealizedPnL;
    }

    public BigDecimal getUnrealizedPnLPercent() {
        return unrealizedPnLPercent;
    }

    public void setUnrealizedPnLPercent(BigDecimal unrealizedPnLPercent) {
        this.unrealizedPnLPercent = unrealizedPnLPercent;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

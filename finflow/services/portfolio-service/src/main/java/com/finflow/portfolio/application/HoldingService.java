package com.finflow.portfolio.application;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finflow.portfolio.domain.AssetType;
import com.finflow.portfolio.domain.Holding;
import com.finflow.portfolio.domain.Portfolio;
import com.finflow.portfolio.dto.request.CreateHoldingRequest;
import com.finflow.portfolio.dto.request.UpdateHoldingRequest;
import com.finflow.portfolio.dto.response.HoldingResponse;
import com.finflow.portfolio.dto.response.PaginatedResponse;
import com.finflow.portfolio.exception.ResourceNotFoundException;
import com.finflow.portfolio.repository.HoldingRepository;
import com.finflow.portfolio.repository.PortfolioRepository;

@Service
@Transactional
public class HoldingService {

    private final HoldingRepository holdingRepository;
    private final PortfolioRepository portfolioRepository;

    public HoldingService(HoldingRepository holdingRepository, PortfolioRepository portfolioRepository) {
        this.holdingRepository = holdingRepository;
        this.portfolioRepository = portfolioRepository;
    }

    public HoldingResponse createHolding(String userId, CreateHoldingRequest request) {
        Portfolio portfolio = portfolioRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio for user", userId));

        BigDecimal totalCost = request.getTotalCostBasis();
        if (portfolio.getCashBalance().compareTo(totalCost) < 0) {
            throw new IllegalStateException("Insufficient cash balance");
        }

        // Check if user already has a position in this symbol
        List<Holding> existingHoldings = holdingRepository.findByUserIdAndSymbol(userId, request.symbol());
        if (!existingHoldings.isEmpty()) {
            // Add to existing position
            Holding existing = existingHoldings.get(0);
            existing.addToPosition(request.quantity(), request.averageCost());

            portfolio.setCashBalance(portfolio.getCashBalance().subtract(totalCost));
            portfolioRepository.save(portfolio);

            Holding updatedHolding = holdingRepository.save(existing);
            return HoldingResponse.from(updatedHolding);
        }

        // Create new holding
        Holding holding = new Holding();
        holding.setPortfolio(portfolio);
        holding.setUserId(userId);
        holding.setSymbol(request.symbol().toUpperCase());
        holding.setQuantity(request.quantity());
        holding.setAverageCost(request.averageCost());
        holding.setCurrentPrice(request.averageCost());
        holding.setAssetType(request.assetType());
        holding.setExchange(request.exchange());

        BigDecimal marketValue = request.quantity().multiply(request.averageCost());
        holding.setMarketValue(marketValue);
        holding.setUnrealizedPnL(BigDecimal.ZERO);
        holding.setUnrealizedPnLPercent(BigDecimal.ZERO);

        portfolio.setCashBalance(portfolio.getCashBalance().subtract(totalCost));
        portfolio.addHolding(holding);
        portfolioRepository.save(portfolio);

        return HoldingResponse.from(holding);
    }

    @Transactional(readOnly = true)
    public HoldingResponse getHoldingById(String holdingId) {
        Holding holding = holdingRepository.findById(holdingId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding", holdingId));
        return HoldingResponse.from(holding);
    }

    @Transactional(readOnly = true)
    public List<HoldingResponse> getHoldingsByUserId(String userId) {
        List<Holding> holdings = holdingRepository.findByUserId(userId);
        return holdings.stream()
                .map(HoldingResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HoldingResponse> getHoldingsByUserIdAndAssetType(String userId, AssetType assetType) {
        List<Holding> holdings = holdingRepository.findByUserIdAndAssetType(userId, assetType);
        return holdings.stream()
                .map(HoldingResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<HoldingResponse> getHoldingsByUserIdPaginated(String userId, Pageable pageable) {
        Page<Holding> holdingPage = holdingRepository.findByUserIdPaginated(userId, pageable);
        return PaginatedResponse.from(holdingPage, HoldingResponse::from);
    }

    public HoldingResponse updateHolding(String holdingId, UpdateHoldingRequest request) {
        Holding holding = holdingRepository.findById(holdingId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding", holdingId));

        if (request.quantity() != null) {
            holding.setQuantity(request.quantity());
        }
        if (request.averageCost() != null) {
            holding.setAverageCost(request.averageCost());
        }
        if (request.currentPrice() != null) {
            holding.updateMarketValue(request.currentPrice());
        }
        if (request.exchange() != null) {
            holding.setExchange(request.exchange());
        }

        // Recalculate market value if quantity changed but no price update
        if (request.quantity() != null && request.currentPrice() == null && holding.getCurrentPrice() != null) {
            holding.updateMarketValue(holding.getCurrentPrice());
        }

        Holding updatedHolding = holdingRepository.save(holding);
        return HoldingResponse.from(updatedHolding);
    }

    public HoldingResponse updateHoldingPrice(String holdingId, BigDecimal newPrice) {
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        Holding holding = holdingRepository.findById(holdingId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding", holdingId));

        holding.updateMarketValue(newPrice);
        Holding updatedHolding = holdingRepository.save(holding);
        return HoldingResponse.from(updatedHolding);
    }

    public void deleteHolding(String holdingId) {
        Holding holding = holdingRepository.findById(holdingId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding", holdingId));

        Portfolio portfolio = holding.getPortfolio();
        if (portfolio != null) {
            // Return market value to cash balance
            if (holding.getMarketValue() != null) {
                portfolio.setCashBalance(portfolio.getCashBalance().add(holding.getMarketValue()));
                portfolioRepository.save(portfolio);
            }
            portfolio.removeHolding(holding);
        }

        holdingRepository.delete(holding);
    }

    public HoldingResponse sellHolding(String holdingId, BigDecimal quantityToSell, BigDecimal sellPrice) {
        if (quantityToSell.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity to sell must be positive");
        }
        if (sellPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Sell price must be positive");
        }

        Holding holding = holdingRepository.findById(holdingId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding", holdingId));

        if (holding.getQuantity().compareTo(quantityToSell) < 0) {
            throw new IllegalStateException("Cannot sell more than current position");
        }

        BigDecimal proceeds = quantityToSell.multiply(sellPrice);

        Portfolio portfolio = holding.getPortfolio();
        if (portfolio != null) {
            portfolio.setCashBalance(portfolio.getCashBalance().add(proceeds));
            portfolioRepository.save(portfolio);
        }

        holding.reducePosition(quantityToSell);

        if (holding.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            holdingRepository.delete(holding);
            return null;
        }

        holding.updateMarketValue(sellPrice);
        Holding updatedHolding = holdingRepository.save(holding);
        return HoldingResponse.from(updatedHolding);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalMarketValue(String userId) {
        BigDecimal total = holdingRepository.getTotalMarketValueByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalUnrealizedPnL(String userId) {
        BigDecimal total = holdingRepository.getTotalUnrealizedPnLByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
    }
}

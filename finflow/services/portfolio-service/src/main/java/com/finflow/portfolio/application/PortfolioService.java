package com.finflow.portfolio.application;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finflow.portfolio.domain.Holding;
import com.finflow.portfolio.domain.Portfolio;
import com.finflow.portfolio.domain.User;
import com.finflow.portfolio.dto.request.CreatePortfolioRequest;
import com.finflow.portfolio.dto.request.UpdatePortfolioRequest;
import com.finflow.portfolio.dto.response.PortfolioResponse;
import com.finflow.portfolio.dto.response.PortfolioSummaryResponse;
import com.finflow.portfolio.exception.ConflictException;
import com.finflow.portfolio.exception.ResourceNotFoundException;
import com.finflow.portfolio.repository.PortfolioRepository;
import com.finflow.portfolio.repository.UserRepository;

@Service
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    public PortfolioResponse createPortfolio(String userId, CreatePortfolioRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (portfolioRepository.existsByUser_Id(userId)) {
            throw new ConflictException("User already has a portfolio");
        }

        Portfolio portfolio = new Portfolio();
        portfolio.setName(request.name());
        portfolio.setUser(user);
        portfolio.setCashBalance(request.getInitialCashBalanceOrDefault());
        portfolio.setBuyingPower(request.getInitialCashBalanceOrDefault());
        portfolio.setTotalValue(request.getInitialCashBalanceOrDefault());

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        return PortfolioResponse.from(savedPortfolio);
    }

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolioById(String portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", portfolioId));
        return PortfolioResponse.from(portfolio);
    }

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolioByUserId(String userId) {
        Portfolio portfolio = portfolioRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio for user", userId));
        return PortfolioResponse.from(portfolio);
    }

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolioWithHoldings(String userId) {
        Portfolio portfolio = portfolioRepository.findByUserIdWithHoldings(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio for user", userId));
        return PortfolioResponse.fromWithHoldings(portfolio);
    }

    @Transactional(readOnly = true)
    public PortfolioSummaryResponse getPortfolioSummary(String userId) {
        Portfolio portfolio = portfolioRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio for user", userId));
        return PortfolioSummaryResponse.from(portfolio);
    }

    public PortfolioResponse updatePortfolio(String portfolioId, UpdatePortfolioRequest request) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", portfolioId));

        if (request.name() != null) {
            portfolio.setName(request.name());
        }
        if (request.cashBalance() != null) {
            portfolio.setCashBalance(request.cashBalance());
        }
        if (request.buyingPower() != null) {
            portfolio.setBuyingPower(request.buyingPower());
        }

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        return PortfolioResponse.from(updatedPortfolio);
    }

    public void deletePortfolio(String portfolioId) {
        if (!portfolioRepository.existsById(portfolioId)) {
            throw new ResourceNotFoundException("Portfolio", portfolioId);
        }
        portfolioRepository.deleteById(portfolioId);
    }

    public PortfolioResponse depositCash(String portfolioId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", portfolioId));

        portfolio.setCashBalance(portfolio.getCashBalance().add(amount));
        portfolio.setBuyingPower(portfolio.getBuyingPower().add(amount));
        portfolio.setTotalValue(portfolio.getTotalValue().add(amount));

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        return PortfolioResponse.from(updatedPortfolio);
    }

    public PortfolioResponse withdrawCash(String portfolioId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", portfolioId));

        if (portfolio.getCashBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient cash balance");
        }

        portfolio.setCashBalance(portfolio.getCashBalance().subtract(amount));
        portfolio.setBuyingPower(portfolio.getBuyingPower().subtract(amount));
        portfolio.setTotalValue(portfolio.getTotalValue().subtract(amount));

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        return PortfolioResponse.from(updatedPortfolio);
    }

    public PortfolioResponse recalculatePortfolioValue(String portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", portfolioId));

        BigDecimal holdingsValue = portfolio.getHoldings().stream()
                .map(Holding::getMarketValue)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalValue = portfolio.getCashBalance().add(holdingsValue);
        portfolio.setTotalValue(totalValue);

        BigDecimal totalGainLoss = portfolio.getHoldings().stream()
                .map(Holding::getUnrealizedPnL)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        portfolio.setTotalGainLoss(totalGainLoss);

        BigDecimal totalCostBasis = portfolio.getHoldings().stream()
                .map(h -> h.getQuantity().multiply(h.getAverageCost()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalCostBasis.compareTo(BigDecimal.ZERO) > 0) {
            portfolio.setTotalGainLossPercent(
                    totalGainLoss.divide(totalCostBasis, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)));
        }

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        return PortfolioResponse.from(updatedPortfolio);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserId(String userId) {
        return portfolioRepository.existsByUser_Id(userId);
    }
}

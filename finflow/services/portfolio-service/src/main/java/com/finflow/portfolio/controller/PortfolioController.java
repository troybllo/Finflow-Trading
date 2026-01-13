package com.finflow.portfolio.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finflow.portfolio.application.PortfolioService;
import com.finflow.portfolio.dto.request.CreatePortfolioRequest;
import com.finflow.portfolio.dto.request.UpdatePortfolioRequest;
import com.finflow.portfolio.dto.response.PortfolioResponse;
import com.finflow.portfolio.dto.response.PortfolioSummaryResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<PortfolioResponse> createPortfolio(
            @PathVariable String userId,
            @Valid @RequestBody CreatePortfolioRequest request) {
        PortfolioResponse portfolio = portfolioService.createPortfolio(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(portfolio);
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioResponse> getPortfolioById(@PathVariable String portfolioId) {
        PortfolioResponse portfolio = portfolioService.getPortfolioById(portfolioId);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PortfolioResponse> getPortfolioByUserId(@PathVariable String userId) {
        PortfolioResponse portfolio = portfolioService.getPortfolioByUserId(userId);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/user/{userId}/with-holdings")
    public ResponseEntity<PortfolioResponse> getPortfolioWithHoldings(@PathVariable String userId) {
        PortfolioResponse portfolio = portfolioService.getPortfolioWithHoldings(userId);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<PortfolioSummaryResponse> getPortfolioSummary(@PathVariable String userId) {
        PortfolioSummaryResponse summary = portfolioService.getPortfolioSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @PutMapping("/{portfolioId}")
    public ResponseEntity<PortfolioResponse> updatePortfolio(
            @PathVariable String portfolioId,
            @Valid @RequestBody UpdatePortfolioRequest request) {
        PortfolioResponse portfolio = portfolioService.updatePortfolio(portfolioId, request);
        return ResponseEntity.ok(portfolio);
    }

    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable String portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{portfolioId}/deposit")
    public ResponseEntity<PortfolioResponse> depositCash(
            @PathVariable String portfolioId,
            @RequestParam BigDecimal amount) {
        PortfolioResponse portfolio = portfolioService.depositCash(portfolioId, amount);
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping("/{portfolioId}/withdraw")
    public ResponseEntity<PortfolioResponse> withdrawCash(
            @PathVariable String portfolioId,
            @RequestParam BigDecimal amount) {
        PortfolioResponse portfolio = portfolioService.withdrawCash(portfolioId, amount);
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping("/{portfolioId}/recalculate")
    public ResponseEntity<PortfolioResponse> recalculatePortfolioValue(@PathVariable String portfolioId) {
        PortfolioResponse portfolio = portfolioService.recalculatePortfolioValue(portfolioId);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/user/{userId}/exists")
    public ResponseEntity<Boolean> existsByUserId(@PathVariable String userId) {
        boolean exists = portfolioService.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }
}

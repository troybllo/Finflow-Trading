package com.finflow.portfolio.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finflow.portfolio.application.PortfolioService;
import com.finflow.portfolio.dto.HoldingRequest;
import com.finflow.portfolio.dto.HoldingResponse;
import com.finflow.portfolio.dto.PortfolioResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/portfolio/")
public class PortfolioController {
  private final PortfolioService portfolioService;

  public PortfolioController(PortfolioService portfolioService) {
    this.portfolioService = portfolioService;
  }

  @GetMapping("{userId}")
  public ResponseEntity<PortfolioResponse> getUserPortfolio(@PathVariable String userId) {
    PortfolioResponse portfolio = portfolioService.getUserPortfolio(userId);

    return ResponseEntity.ok(portfolio);
  }

  @PostMapping("{userId}/holdings")
  public ResponseEntity<HoldingResponse> addUserHolding(@PathVariable String userId,
      @Valid @RequestBody HoldingRequest request) {
    HoldingResponse holding = portfolioService.addUserHolding(userId, request);

    return ResponseEntity.status(HttpStatus.CREATED).body(holding);
  }

  @GetMapping("{userId}/holdings")
	public ResponseEntity<List<HoldingResponse>> listUserHoldings(@PathVariable String userId)   {
  	List<HoldingResponse> holdings = portfolioService.listUserHoldings(userId);

		return



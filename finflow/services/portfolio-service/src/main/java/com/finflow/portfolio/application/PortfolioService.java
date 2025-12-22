package com.finflow.portfolio.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.finflow.portfolio.dto.HoldingRequest;
import com.finflow.portfolio.dto.HoldingResponse;
import com.finflow.portfolio.dto.PortfolioResponse;

@Service
public class PortfolioService {

  public PortfolioResponse getUserPortfolio(String userId) {
    // Mock implementation for demonstration purposes
    return new PortfolioResponse("100", "Troy Bello", "123", 100.21, 4);
  }

  public HoldingResponse addUserHolding(String userId, HoldingRequest request) {
    // Mock implementation for demonstration purposes
    return new HoldingResponse("holding-1", request.portfolioId(), request.assetSymbol(),
        request.quantity(), request.averagePrice());
  }

  public List<HoldingResponse> listUserHoldings(String userId) {
    // Mock implementation for demonstration purposes
    return List.of(
        new HoldingResponse("holding-1", "portfolio-100", "AAPL", 10, 150.50),
        new HoldingResponse("holding-2", "portfolio-100", "GOOGL", 5, 2800.75)
    );
  }
}

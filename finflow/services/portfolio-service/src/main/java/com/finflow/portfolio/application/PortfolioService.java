package com.finflow.portfolio.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finflow.portfolio.domain.Portfolio;
import com.finflow.portfolio.domain.User;
import com.finflow.portfolio.dto.request.CreatePortfolioRequest;
import com.finflow.portfolio.dto.response.PortfolioResponse;
import com.finflow.portfolio.exception.ConflictException;
import com.finflow.portfolio.repository.PortfolioRepository;

@Service
@Transactional
public class PortfolioService {

	private final PortfolioRepository portfolioRepository;

	public PortfolioService(PortfolioRepository portfolioRepository) {
		this.portfolioRepository = portfolioRepository;
	}

	public PortfolioResponse createPortfolio(String userId, CreatePortfolioRequest request) {
		if (portfolioRepository.existsByUser_Id(userId)) {
			throw new ConflictException("Portfolio", "portfolioUserId", userId);
		}

		User user = new User();
		Portfolio portfolio = new Portfolio();
		portfolio.setName(request.name());
		portfolio.setUser(user);

		Portfolio savedPortfolio = portfolioRepository.save(portfolio);

		return PortfolioResponse.from(savedPortfolio);

	}

	public PortfolioResponse getPortfolioById(String portfolioId) {
		Portfolio portfolio = portfolioRepository.findById(portfolioId)
				.orElseThrow(() -> new ConflictException("Portfolio", "portfolioId", portfolioId));

		return PortfolioResponse.from(portfolio);
	}

	public PortfolioResponse getPortoflioByUserId(String userId) {
		Portfolio portfolio = portfolioRepository.findByUser_Id(userId)
				.orElseThrow(() -> new ConflictException("Portfolio", "portfolioUserId", userId));
		return PortfolioResponse.from(portfolio);
	}

	public PortfolioResponse getPortfolioWithHoldings(String userId) {
		Portfolio portfolio = portfolioRepository.findByUserIdWithHoldings(userId)
				.orElseThrow(() -> new ConflictException("Portfolio", "portfolioUserId", userId));
		return PortfolioResponse.from(portfolio);
	}

}

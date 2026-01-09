package com.finflow.portfolio.dto.response;

import java.time.LocalDateTime;

import com.finflow.portfolio.domain.User;

public record UserResponse(String id, String username, String email, String avatar, String portfolioId,
		LocalDateTime createdAt, LocalDateTime updatedAt

) {

	public static UserResponse from(User user) {
		return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getAvatar(),
				user.getPortfolio() != null ? user.getPortfolio().getId() : null, user.getCreatedAt(),
				user.getUpdatedAt());
	}
}

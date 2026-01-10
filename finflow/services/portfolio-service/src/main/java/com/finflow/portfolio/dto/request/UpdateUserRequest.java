package com.finflow.portfolio.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating user profile. All fields are optional - only
 * provided fields will be updated.
 */
public record UpdateUserRequest(
		@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") String username,

		@Email(message = "Invalid email format") @Size(max = 255, message = "Email must not exceed 255 characters") String email,

		@Size(max = 500, message = "Avatar URL must not exceed 500 characters") String avatar) {
	/**
	 * Check if any field is provided for update
	 */
	public boolean hasUpdates() {
		return username != null || email != null || avatar != null;
	}
}

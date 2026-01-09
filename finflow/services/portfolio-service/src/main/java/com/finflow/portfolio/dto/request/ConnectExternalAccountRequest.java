package com.finflow.portfolio.dto.request;

import com.finflow.portfolio.domain.ExternalPlatform;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for connecting an external broker account.
 * Used after OAuth flow to store the connection details.
 */
public record ConnectExternalAccountRequest(
    @NotNull(message = "Platform is required")
    ExternalPlatform platform,

    @NotBlank(message = "Access token is required")
    String accessToken,

    String refreshToken,

    @Size(max = 100, message = "Account name must not exceed 100 characters")
    String accountName,

    @Size(max = 50, message = "Account number must not exceed 50 characters")
    String accountNumber,

    Long tokenExpiresInSeconds
) {
    /**
     * Check if the connection includes a refresh token for token renewal
     */
    public boolean hasRefreshToken() {
        return refreshToken != null && !refreshToken.isBlank();
    }

    /**
     * Check if token expiration is provided
     */
    public boolean hasTokenExpiration() {
        return tokenExpiresInSeconds != null && tokenExpiresInSeconds > 0;
    }
}

package com.finflow.portfolio.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating external account settings.
 * All fields are optional - only provided fields will be updated.
 */
public record UpdateExternalAccountRequest(
    @Size(max = 100, message = "Account name must not exceed 100 characters")
    String accountName,

    Boolean syncEnabled,

    String accessToken,

    String refreshToken,

    Long tokenExpiresInSeconds
) {
    /**
     * Check if any field is provided for update
     */
    public boolean hasUpdates() {
        return accountName != null || syncEnabled != null ||
               accessToken != null || refreshToken != null || tokenExpiresInSeconds != null;
    }

    /**
     * Check if this is a token refresh update
     */
    public boolean isTokenRefresh() {
        return accessToken != null;
    }

    /**
     * Check if sync settings are being updated
     */
    public boolean hasSyncSettingsUpdate() {
        return syncEnabled != null;
    }
}

package com.finflow.portfolio.dto.response;

import com.finflow.portfolio.domain.ConnectionStatus;
import com.finflow.portfolio.domain.ExternalAccount;
import com.finflow.portfolio.domain.ExternalPlatform;

import java.time.LocalDateTime;

/**
 * Response DTO for external broker account data.
 * Note: Sensitive fields (accessToken, refreshToken) are NOT included for security.
 */
public record ExternalAccountResponse(
    String id,
    String userId,
    ExternalPlatform platform,
    String accountName,
    String accountNumber,
    ConnectionStatus status,
    boolean syncEnabled,
    LocalDateTime lastSyncAt,
    LocalDateTime tokenExpiresAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    /**
     * Factory method to create ExternalAccountResponse from ExternalAccount entity
     * Note: Tokens are intentionally excluded for security
     */
    public static ExternalAccountResponse from(ExternalAccount account) {
        return new ExternalAccountResponse(
            account.getId(),
            account.getUserId(),
            account.getPlatform(),
            account.getAccountName(),
            account.getAccountNumber(),
            account.getStatus(),
            account.isSyncEnabled(),
            account.getLastSyncAt(),
            account.getTokenExpiresAt(),
            account.getCreatedAt(),
            account.getUpdatedAt()
        );
    }

    /**
     * Check if the account is connected and ready
     */
    public boolean isConnected() {
        return status == ConnectionStatus.CONNECTED;
    }

    /**
     * Check if the account has sync issues
     */
    public boolean hasError() {
        return status == ConnectionStatus.ERROR;
    }

    /**
     * Check if the account is currently syncing
     */
    public boolean isSyncing() {
        return status == ConnectionStatus.SYNCING;
    }
}

package com.finflow.portfolio.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "external_accounts", indexes = {
    @Index(name = "idx_external_account_user", columnList = "user_id"),
    @Index(name = "idx_external_account_platform", columnList = "platform"),
    @Index(name = "idx_external_account_status", columnList = "status")
})
public class ExternalAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ExternalPlatform platform;

    @Column(name = "account_name", nullable = false, length = 255)
    private String accountName;

    @Column(name = "account_number", length = 100)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConnectionStatus status;

    @Column(name = "last_sync_at")
    private LocalDateTime lastSyncAt;

    @Column(name = "sync_enabled", nullable = false)
    private boolean syncEnabled = true;

    @Column(name = "access_token", length = 1000)
    private String accessToken;  // Encrypted in production

    @Column(name = "refresh_token", length = 1000)
    private String refreshToken;  // Encrypted in production

    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public ExternalAccount() {
    }

    public ExternalAccount(String userId, ExternalPlatform platform, String accountName) {
        this.userId = userId;
        this.platform = platform;
        this.accountName = accountName;
        this.status = ConnectionStatus.DISCONNECTED;
        this.syncEnabled = true;
    }

    // Business logic methods
    public void connect(String accessToken, String refreshToken, LocalDateTime tokenExpiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenExpiresAt = tokenExpiresAt;
        this.status = ConnectionStatus.CONNECTED;
    }

    public void disconnect() {
        this.status = ConnectionStatus.DISCONNECTED;
        this.syncEnabled = false;
    }

    public void markSyncing() {
        this.status = ConnectionStatus.SYNCING;
    }

    public void completedSync() {
        this.status = ConnectionStatus.CONNECTED;
        this.lastSyncAt = LocalDateTime.now();
    }

    public void syncError() {
        this.status = ConnectionStatus.ERROR;
    }

    public boolean isConnected() {
        return status == ConnectionStatus.CONNECTED;
    }

    public boolean needsTokenRefresh() {
        return tokenExpiresAt != null && tokenExpiresAt.isBefore(LocalDateTime.now());
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ExternalPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(ExternalPlatform platform) {
        this.platform = platform;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(LocalDateTime lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }

    public boolean isSyncEnabled() {
        return syncEnabled;
    }

    public void setSyncEnabled(boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    public void setTokenExpiresAt(LocalDateTime tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

package com.finflow.portfolio.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finflow.portfolio.domain.ConnectionStatus;
import com.finflow.portfolio.domain.ExternalAccount;
import com.finflow.portfolio.domain.ExternalPlatform;
import com.finflow.portfolio.dto.request.ConnectExternalAccountRequest;
import com.finflow.portfolio.dto.request.UpdateExternalAccountRequest;
import com.finflow.portfolio.dto.response.ExternalAccountResponse;
import com.finflow.portfolio.exception.ConflictException;
import com.finflow.portfolio.exception.ResourceNotFoundException;
import com.finflow.portfolio.repository.ExternalAccountRepository;
import com.finflow.portfolio.repository.UserRepository;

@Service
@Transactional
public class ExternalAccountService {

    private final ExternalAccountRepository externalAccountRepository;
    private final UserRepository userRepository;

    public ExternalAccountService(ExternalAccountRepository externalAccountRepository, UserRepository userRepository) {
        this.externalAccountRepository = externalAccountRepository;
        this.userRepository = userRepository;
    }

    public ExternalAccountResponse connectAccount(String userId, ConnectExternalAccountRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }

        // Check if user already has an account with this platform
        if (externalAccountRepository.existsByUserIdAndPlatform(userId, request.platform())) {
            throw new ConflictException("External account", "platform", request.platform().name());
        }

        ExternalAccount account = new ExternalAccount();
        account.setUserId(userId);
        account.setPlatform(request.platform());
        account.setAccountName(request.accountName() != null ? request.accountName() : request.platform().name());
        account.setAccountNumber(request.accountNumber());
        account.setAccessToken(request.accessToken());
        account.setRefreshToken(request.refreshToken());
        account.setStatus(ConnectionStatus.CONNECTED);
        account.setSyncEnabled(true);

        if (request.hasTokenExpiration()) {
            account.setTokenExpiresAt(LocalDateTime.now().plusSeconds(request.tokenExpiresInSeconds()));
        }

        ExternalAccount savedAccount = externalAccountRepository.save(account);
        return ExternalAccountResponse.from(savedAccount);
    }

    @Transactional(readOnly = true)
    public ExternalAccountResponse getAccountById(String accountId) {
        ExternalAccount account = externalAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("External account", accountId));
        return ExternalAccountResponse.from(account);
    }

    @Transactional(readOnly = true)
    public List<ExternalAccountResponse> getAccountsByUserId(String userId) {
        List<ExternalAccount> accounts = externalAccountRepository.findByUserId(userId);
        return accounts.stream()
                .map(ExternalAccountResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExternalAccountResponse> getConnectedAccountsByUserId(String userId) {
        List<ExternalAccount> accounts = externalAccountRepository.findByUserIdAndStatus(userId, ConnectionStatus.CONNECTED);
        return accounts.stream()
                .map(ExternalAccountResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ExternalAccountResponse getAccountByUserIdAndPlatform(String userId, ExternalPlatform platform) {
        ExternalAccount account = externalAccountRepository.findByUserIdAndPlatform(userId, platform)
                .orElseThrow(() -> new ResourceNotFoundException("External account", "platform", platform.name()));
        return ExternalAccountResponse.from(account);
    }

    public ExternalAccountResponse updateAccount(String accountId, UpdateExternalAccountRequest request) {
        ExternalAccount account = externalAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("External account", accountId));

        if (request.accountName() != null) {
            account.setAccountName(request.accountName());
        }
        if (request.syncEnabled() != null) {
            account.setSyncEnabled(request.syncEnabled());
        }
        if (request.isTokenRefresh()) {
            account.setAccessToken(request.accessToken());
            if (request.refreshToken() != null) {
                account.setRefreshToken(request.refreshToken());
            }
            if (request.tokenExpiresInSeconds() != null) {
                account.setTokenExpiresAt(LocalDateTime.now().plusSeconds(request.tokenExpiresInSeconds()));
            }
            account.setStatus(ConnectionStatus.CONNECTED);
        }

        ExternalAccount updatedAccount = externalAccountRepository.save(account);
        return ExternalAccountResponse.from(updatedAccount);
    }

    public ExternalAccountResponse disconnectAccount(String accountId) {
        ExternalAccount account = externalAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("External account", accountId));

        account.disconnect();
        ExternalAccount updatedAccount = externalAccountRepository.save(account);
        return ExternalAccountResponse.from(updatedAccount);
    }

    public void deleteAccount(String accountId) {
        if (!externalAccountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("External account", accountId);
        }
        externalAccountRepository.deleteById(accountId);
    }

    public ExternalAccountResponse startSync(String accountId) {
        ExternalAccount account = externalAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("External account", accountId));

        if (!account.isConnected()) {
            throw new IllegalStateException("Account must be connected to sync");
        }
        if (!account.isSyncEnabled()) {
            throw new IllegalStateException("Sync is disabled for this account");
        }

        account.markSyncing();
        ExternalAccount updatedAccount = externalAccountRepository.save(account);
        return ExternalAccountResponse.from(updatedAccount);
    }

    public ExternalAccountResponse completeSync(String accountId) {
        ExternalAccount account = externalAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("External account", accountId));

        account.completedSync();
        ExternalAccount updatedAccount = externalAccountRepository.save(account);
        return ExternalAccountResponse.from(updatedAccount);
    }

    public ExternalAccountResponse markSyncError(String accountId) {
        ExternalAccount account = externalAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("External account", accountId));

        account.syncError();
        ExternalAccount updatedAccount = externalAccountRepository.save(account);
        return ExternalAccountResponse.from(updatedAccount);
    }

    @Transactional(readOnly = true)
    public List<ExternalAccountResponse> getAccountsNeedingTokenRefresh() {
        List<ExternalAccount> accounts = externalAccountRepository.findAccountsWithExpiredTokens(LocalDateTime.now());
        return accounts.stream()
                .map(ExternalAccountResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExternalAccountResponse> getAccountsReadyToSync() {
        List<ExternalAccount> accounts = externalAccountRepository.findAccountsReadyToSync();
        return accounts.stream()
                .map(ExternalAccountResponse::from)
                .toList();
    }
}

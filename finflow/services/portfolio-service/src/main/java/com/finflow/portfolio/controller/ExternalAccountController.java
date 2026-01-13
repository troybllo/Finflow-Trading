package com.finflow.portfolio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finflow.portfolio.application.ExternalAccountService;
import com.finflow.portfolio.domain.ExternalPlatform;
import com.finflow.portfolio.dto.request.ConnectExternalAccountRequest;
import com.finflow.portfolio.dto.request.UpdateExternalAccountRequest;
import com.finflow.portfolio.dto.response.ExternalAccountResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/external-accounts")
public class ExternalAccountController {

    private final ExternalAccountService externalAccountService;

    public ExternalAccountController(ExternalAccountService externalAccountService) {
        this.externalAccountService = externalAccountService;
    }

    @PostMapping("/user/{userId}/connect")
    public ResponseEntity<ExternalAccountResponse> connectAccount(
            @PathVariable String userId,
            @Valid @RequestBody ConnectExternalAccountRequest request) {
        ExternalAccountResponse account = externalAccountService.connectAccount(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ExternalAccountResponse> getAccountById(@PathVariable String accountId) {
        ExternalAccountResponse account = externalAccountService.getAccountById(accountId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExternalAccountResponse>> getAccountsByUserId(@PathVariable String userId) {
        List<ExternalAccountResponse> accounts = externalAccountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/user/{userId}/connected")
    public ResponseEntity<List<ExternalAccountResponse>> getConnectedAccountsByUserId(@PathVariable String userId) {
        List<ExternalAccountResponse> accounts = externalAccountService.getConnectedAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/user/{userId}/platform/{platform}")
    public ResponseEntity<ExternalAccountResponse> getAccountByUserIdAndPlatform(
            @PathVariable String userId,
            @PathVariable ExternalPlatform platform) {
        ExternalAccountResponse account = externalAccountService.getAccountByUserIdAndPlatform(userId, platform);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<ExternalAccountResponse> updateAccount(
            @PathVariable String accountId,
            @Valid @RequestBody UpdateExternalAccountRequest request) {
        ExternalAccountResponse account = externalAccountService.updateAccount(accountId, request);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/disconnect")
    public ResponseEntity<ExternalAccountResponse> disconnectAccount(@PathVariable String accountId) {
        ExternalAccountResponse account = externalAccountService.disconnectAccount(accountId);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        externalAccountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{accountId}/sync/start")
    public ResponseEntity<ExternalAccountResponse> startSync(@PathVariable String accountId) {
        ExternalAccountResponse account = externalAccountService.startSync(accountId);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/sync/complete")
    public ResponseEntity<ExternalAccountResponse> completeSync(@PathVariable String accountId) {
        ExternalAccountResponse account = externalAccountService.completeSync(accountId);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/sync/error")
    public ResponseEntity<ExternalAccountResponse> markSyncError(@PathVariable String accountId) {
        ExternalAccountResponse account = externalAccountService.markSyncError(accountId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/needs-token-refresh")
    public ResponseEntity<List<ExternalAccountResponse>> getAccountsNeedingTokenRefresh() {
        List<ExternalAccountResponse> accounts = externalAccountService.getAccountsNeedingTokenRefresh();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/ready-to-sync")
    public ResponseEntity<List<ExternalAccountResponse>> getAccountsReadyToSync() {
        List<ExternalAccountResponse> accounts = externalAccountService.getAccountsReadyToSync();
        return ResponseEntity.ok(accounts);
    }
}

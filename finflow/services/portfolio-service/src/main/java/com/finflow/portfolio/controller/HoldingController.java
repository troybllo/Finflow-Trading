package com.finflow.portfolio.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finflow.portfolio.application.HoldingService;
import com.finflow.portfolio.domain.AssetType;
import com.finflow.portfolio.dto.request.CreateHoldingRequest;
import com.finflow.portfolio.dto.request.UpdateHoldingRequest;
import com.finflow.portfolio.dto.response.HoldingResponse;
import com.finflow.portfolio.dto.response.PaginatedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/holdings")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<HoldingResponse> createHolding(
            @PathVariable String userId,
            @Valid @RequestBody CreateHoldingRequest request) {
        HoldingResponse holding = holdingService.createHolding(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(holding);
    }

    @GetMapping("/{holdingId}")
    public ResponseEntity<HoldingResponse> getHoldingById(@PathVariable String holdingId) {
        HoldingResponse holding = holdingService.getHoldingById(holdingId);
        return ResponseEntity.ok(holding);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HoldingResponse>> getHoldingsByUserId(@PathVariable String userId) {
        List<HoldingResponse> holdings = holdingService.getHoldingsByUserId(userId);
        return ResponseEntity.ok(holdings);
    }

    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<PaginatedResponse<HoldingResponse>> getHoldingsByUserIdPaginated(
            @PathVariable String userId,
            @PageableDefault(size = 20) Pageable pageable) {
        PaginatedResponse<HoldingResponse> holdings = holdingService.getHoldingsByUserIdPaginated(userId, pageable);
        return ResponseEntity.ok(holdings);
    }

    @GetMapping("/user/{userId}/type/{assetType}")
    public ResponseEntity<List<HoldingResponse>> getHoldingsByUserIdAndAssetType(
            @PathVariable String userId,
            @PathVariable AssetType assetType) {
        List<HoldingResponse> holdings = holdingService.getHoldingsByUserIdAndAssetType(userId, assetType);
        return ResponseEntity.ok(holdings);
    }

    @PutMapping("/{holdingId}")
    public ResponseEntity<HoldingResponse> updateHolding(
            @PathVariable String holdingId,
            @Valid @RequestBody UpdateHoldingRequest request) {
        HoldingResponse holding = holdingService.updateHolding(holdingId, request);
        return ResponseEntity.ok(holding);
    }

    @PutMapping("/{holdingId}/price")
    public ResponseEntity<HoldingResponse> updateHoldingPrice(
            @PathVariable String holdingId,
            @RequestParam BigDecimal price) {
        HoldingResponse holding = holdingService.updateHoldingPrice(holdingId, price);
        return ResponseEntity.ok(holding);
    }

    @PostMapping("/{holdingId}/sell")
    public ResponseEntity<HoldingResponse> sellHolding(
            @PathVariable String holdingId,
            @RequestParam BigDecimal quantity,
            @RequestParam BigDecimal price) {
        HoldingResponse holding = holdingService.sellHolding(holdingId, quantity, price);
        if (holding == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(holding);
    }

    @DeleteMapping("/{holdingId}")
    public ResponseEntity<Void> deleteHolding(@PathVariable String holdingId) {
        holdingService.deleteHolding(holdingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/total-value")
    public ResponseEntity<BigDecimal> getTotalMarketValue(@PathVariable String userId) {
        BigDecimal totalValue = holdingService.getTotalMarketValue(userId);
        return ResponseEntity.ok(totalValue);
    }

    @GetMapping("/user/{userId}/total-pnl")
    public ResponseEntity<BigDecimal> getTotalUnrealizedPnL(@PathVariable String userId) {
        BigDecimal totalPnL = holdingService.getTotalUnrealizedPnL(userId);
        return ResponseEntity.ok(totalPnL);
    }
}

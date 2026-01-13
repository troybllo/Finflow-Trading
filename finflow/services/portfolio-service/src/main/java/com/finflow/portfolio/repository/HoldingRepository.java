package com.finflow.portfolio.repository;

import com.finflow.portfolio.domain.AssetType;
import com.finflow.portfolio.domain.Holding;
import com.finflow.portfolio.domain.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, String> {

  // ============================================================
  // Basic Finders
  // ============================================================

  /**
   * Find all holdings in a portfolio
   *
   * @param portfolio the portfolio entity
   * @return list of holdings
   */
  List<Holding> findByPortfolio(Portfolio portfolio);

  /**
   * Find all holdings in a portfolio by ID
   *
   * @param portfolioId the portfolio ID
   * @return list of holdings
   */
  List<Holding> findByPortfolio_Id(String portfolioId);

  /**
   * Find all holdings for a user
   *
   * @param userId the user ID
   * @return list of holdings
   */
  List<Holding> findByUserId(String userId);

  /**
   * Find all holdings for a specific symbol across all portfolios
   *
   * @param symbol the asset symbol
   * @return list of holdings
   */
  List<Holding> findBySymbol(String symbol);

  /**
   * Find a specific holding in a portfolio by symbol (should be unique)
   *
   * @param portfolioId the portfolio ID
   * @param symbol      the asset symbol
   * @return Optional containing the holding if found
   */
  Optional<Holding> findByPortfolio_IdAndSymbol(String portfolioId, String symbol);

  /**
   * Find a specific holding for a user by symbol
   *
   * @param userId the user ID
   * @param symbol the asset symbol
   * @return Optional containing the holding if found
   */
  List<Holding> findByUserIdAndSymbol(String userId, String symbol);

  // ============================================================
  // Existence Checks
  // ============================================================

  /**
   * Check if portfolio has any holdings
   *
   * @param portfolio the portfolio entity
   * @return true if holdings exist
   */
  boolean existsByPortfolio(Portfolio portfolio);

  /**
   * Check if portfolio has any holdings by ID
   *
   * @param portfolioId the portfolio ID
   * @return true if holdings exist
   */
  boolean existsByPortfolio_Id(String portfolioId);

  /**
   * Check if user has any holdings
   *
   * @param userId the user ID
   * @return true if holdings exist
   */
  boolean existsByUserId(String userId);

  /**
   * Check if specific holding exists in portfolio
   *
   * @param portfolioId the portfolio ID
   * @param symbol      the asset symbol
   * @return true if holding exists
   */
  boolean existsByPortfolio_IdAndSymbol(String portfolioId, String symbol);

  // ============================================================
  // Asset Type Queries
  // ============================================================

  /**
   * Find holdings by asset type
   *
   * @param assetType the asset type (STOCK, CRYPTO, etc.)
   * @return list of holdings
   */
  List<Holding> findByAssetType(AssetType assetType);

  /**
   * Find user's holdings by asset type
   *
   * @param userId    the user ID
   * @param assetType the asset type
   * @return list of holdings
   */
  List<Holding> findByUserIdAndAssetType(String userId, AssetType assetType);

  /**
   * Find portfolio's holdings by asset type
   *
   * @param portfolioId the portfolio ID
   * @param assetType   the asset type
   * @return list of holdings
   */
  List<Holding> findByPortfolio_IdAndAssetType(String portfolioId, AssetType assetType);

  // ============================================================
  // Quantity & Value Queries
  // ============================================================

  /**
   * Find holdings with quantity greater than specified amount
   *
   * @param quantity the minimum quantity
   * @return list of holdings
   */
  List<Holding> findByQuantityGreaterThan(BigDecimal quantity);

  /**
   * Find holdings with market value greater than specified amount
   *
   * @param marketValue the minimum market value
   * @return list of holdings
   */
  List<Holding> findByMarketValueGreaterThan(BigDecimal marketValue);

  /**
   * Find holdings with market value between min and max
   *
   * @param minValue the minimum value
   * @param maxValue the maximum value
   * @return list of holdings
   */
  List<Holding> findByMarketValueBetween(BigDecimal minValue, BigDecimal maxValue);

  // ============================================================
  // P&L Queries
  // ============================================================

  /**
   * Find profitable holdings (unrealized P&L > 0)
   *
   * @param userId the user ID
   * @return list of profitable holdings
   */
  @Query("SELECT h FROM Holding h WHERE h.userId = :userId AND h.unrealizedPnL > 0")
  List<Holding> findProfitableHoldings(@Param("userId") String userId);

  /**
   * Find losing holdings (unrealized P&L < 0)
   *
   * @param userId the user ID
   * @return list of losing holdings
   */
  @Query("SELECT h FROM Holding h WHERE h.userId = :userId AND h.unrealizedPnL < 0")
  List<Holding> findLosingHoldings(@Param("userId") String userId);

  /**
   * Find holdings with unrealized P&L greater than amount
   *
   * @param pnl the minimum P&L
   * @return list of holdings
   */
  List<Holding> findByUnrealizedPnLGreaterThan(BigDecimal pnl);

  /**
   * Find holdings with unrealized P&L less than amount
   *
   * @param pnl the maximum P&L
   * @return list of holdings
   */
  List<Holding> findByUnrealizedPnLLessThan(BigDecimal pnl);

  // ============================================================
  // Exchange Queries
  // ============================================================

  /**
   * Find holdings by exchange
   *
   * @param exchange the exchange name
   * @return list of holdings
   */
  List<Holding> findByExchange(String exchange);

  /**
   * Find user's holdings by exchange
   *
   * @param userId   the user ID
   * @param exchange the exchange name
   * @return list of holdings
   */
  List<Holding> findByUserIdAndExchange(String userId, String exchange);

  // ============================================================
  // Date-based Queries
  // ============================================================

  /**
   * Find holdings created after a specific date
   *
   * @param date the date to compare against
   * @return list of holdings created after the date
   */
  List<Holding> findByCreatedAtAfter(LocalDateTime date);

  /**
   * Find holdings created before a specific date
   *
   * @param date the date to compare against
   * @return list of holdings created before the date
   */
  List<Holding> findByCreatedAtBefore(LocalDateTime date);

  /**
   * Find holdings created within a date range
   *
   * @param startDate the start date
   * @param endDate   the end date
   * @return list of holdings created within the range
   */
  List<Holding> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

  /**
   * Find holdings updated after a specific date
   *
   * @param date the date to compare against
   * @return list of holdings updated after the date
   */
  List<Holding> findByUpdatedAtAfter(LocalDateTime date);

  /**
   * Find holdings updated before a specific date
   *
   * @param date the date to compare against
   * @return list of holdings updated before the date
   */
  List<Holding> findByUpdatedAtBefore(LocalDateTime date);

  // ============================================================
  // Top Holdings Queries
  // ============================================================

  /**
   * Find top N holdings by market value for a user
   *
   * @param userId   the user ID
   * @param pageable pagination (use PageRequest.of(0, N))
   * @return page of top holdings
   */
  Page<Holding> findByUserIdOrderByMarketValueDesc(String userId, Pageable pageable);

  /**
   * Find top N holdings by unrealized P&L for a user
   *
   * @param userId   the user ID
   * @param pageable pagination
   * @return page of top holdings by P&L
   */
  Page<Holding> findByUserIdOrderByUnrealizedPnLDesc(String userId, Pageable pageable);

  // ============================================================
  // Custom Queries with Eager Loading
  // ============================================================

  /**
   * Find holdings by user ID with portfolio eagerly loaded (avoids N+1)
   *
   * @param userId the user ID
   * @return list of holdings with portfolio loaded
   */
  @Query("SELECT h FROM Holding h JOIN FETCH h.portfolio WHERE h.userId = :userId")
  List<Holding> findByUserIdWithPortfolio(@Param("userId") String userId);

  /**
   * Find all holdings with portfolio eagerly loaded
   *
   * @return list of all holdings with portfolio
   */
  @Query("SELECT h FROM Holding h JOIN FETCH h.portfolio")
  List<Holding> findAllWithPortfolio();

  // ============================================================
  // Pagination Support
  // ============================================================

  /**
   * Find user's holdings with pagination
   *
   * @param userId   the user ID
   * @param pageable pagination parameters
   * @return page of holdings
   */
  Page<Holding> findByUserId(String userId, Pageable pageable);

  /**
   * Find portfolio's holdings with pagination
   *
   * @param portfolioId the portfolio ID
   * @param pageable    pagination parameters
   * @return page of holdings
   */
  Page<Holding> findByPortfolio_Id(String portfolioId, Pageable pageable);

  // ============================================================
  // Delete Operations
  // ============================================================

  /**
   * Delete all holdings in a portfolio
   *
   * @param portfolioId the portfolio ID
   */
  @Modifying
  @Transactional
  void deleteByPortfolio_Id(String portfolioId);

  /**
   * Delete specific holding by portfolio and symbol
   *
   * @param portfolioId the portfolio ID
   * @param symbol      the asset symbol
   */
  @Modifying
  @Transactional
  void deleteByPortfolio_IdAndSymbol(String portfolioId, String symbol);

  // ============================================================
  // Statistical Queries
  // ============================================================

  /**
   * Get total market value of all holdings for a user
   *
   * @param userId the user ID
   * @return total market value
   */
  @Query("SELECT SUM(h.marketValue) FROM Holding h WHERE h.userId = :userId")
  BigDecimal getTotalMarketValueByUserId(@Param("userId") String userId);

  /**
   * Get total unrealized P&L for a user
   *
   * @param userId the user ID
   * @return total unrealized P&L
   */
  @Query("SELECT SUM(h.unrealizedPnL) FROM Holding h WHERE h.userId = :userId")
  BigDecimal getTotalUnrealizedPnLByUserId(@Param("userId") String userId);

  /**
   * Count holdings by asset type for a user
   *
   * @param userId    the user ID
   * @param assetType the asset type
   * @return count of holdings
   */
  Long countByUserIdAndAssetType(String userId, AssetType assetType);
}

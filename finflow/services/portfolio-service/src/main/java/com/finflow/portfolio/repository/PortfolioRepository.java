package com.finflow.portfolio.repository;

import com.finflow.portfolio.domain.Portfolio;
import com.finflow.portfolio.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, String> {

	// ============================================================
	// Basic Finders
	// ============================================================

	/**
	 * Find portfolio by user
	 * 
	 * @param user the user entity
	 * @return Optional containing the portfolio if found
	 */
	Optional<Portfolio> findByUser(User user);

	/**
	 * Find portfolio by user ID (navigates User relationship)
	 * 
	 * @param userId the user ID
	 * @return Optional containing the portfolio if found
	 */
	Optional<Portfolio> findByUser_Id(String userId);

	/**
	 * Find portfolio by name
	 * 
	 * @param name the portfolio name
	 * @return Optional containing the portfolio if found
	 */
	Optional<Portfolio> findByName(String name);

	/**
	 * Find all portfolios with non-null users
	 * 
	 * @return list of portfolios
	 */
	List<Portfolio> findByUserNotNull();

	// ============================================================
	// Existence Checks
	// ============================================================

	/**
	 * Check if portfolio exists for user
	 * 
	 * @param user the user entity
	 * @return true if exists
	 */
	boolean existsByUser(User user);

	/**
	 * Check if portfolio exists for user ID
	 * 
	 * @param userId the user ID
	 * @return true if exists
	 */
	boolean existsByUser_Id(String userId);

	/**
	 * Check if portfolio name exists
	 * 
	 * @param name the portfolio name
	 * @return true if exists
	 */
	boolean existsByName(String name);

	// ============================================================
	// Total Value Queries
	// ============================================================

	/**
	 * Find portfolios with total value greater than or equal to specified amount
	 * 
	 * @param value the minimum value
	 * @return list of portfolios
	 */
	List<Portfolio> findByTotalValueGreaterThanEqual(BigDecimal value);

	/**
	 * Find portfolios with total value less than or equal to specified amount
	 * 
	 * @param value the maximum value
	 * @return list of portfolios
	 */
	List<Portfolio> findByTotalValueLessThanEqual(BigDecimal value);

	/**
	 * Find portfolios with total value between min and max
	 * 
	 * @param minValue the minimum value
	 * @param maxValue the maximum value
	 * @return list of portfolios
	 */
	List<Portfolio> findByTotalValueBetween(BigDecimal minValue, BigDecimal maxValue);

	// ============================================================
	// ============================================================

	/**
	 * Find portfolios with total gain/loss greater than or equal to amount
	 * 
	 * @param gainLoss the minimum gain/loss
	 * @return list of portfolios
	 */
	List<Portfolio> findByTotalGainLossGreaterThanEqual(BigDecimal gainLoss);

	/**
	 * Find portfolios with total gain/loss less than or equal to amount
	 * 
	 * @param gainLoss the maximum gain/loss
	 * @return list of portfolios
	 */
	List<Portfolio> findByTotalGainLossLessThanEqual(BigDecimal gainLoss);

	/**
	 * Find portfolios with positive total gain (profitable portfolios)
	 * 
	 * @return list of profitable portfolios
	 */
	@Query("SELECT p FROM Portfolio p WHERE p.totalGainLoss > 0")
	List<Portfolio> findProfitablePortfolios();

	/**
	 * Find portfolios with negative total gain (losing portfolios)
	 * 
	 * @return list of losing portfolios
	 */
	@Query("SELECT p FROM Portfolio p WHERE p.totalGainLoss < 0")
	List<Portfolio> findLosingPortfolios();

	// ============================================================
	// Daily Performance Queries
	// ============================================================
	// Total Gain/Loss Queries

	/**
	 * Find portfolios with daily change greater than amount
	 * 
	 * @param change the daily change threshold
	 * @return list of portfolios
	 */
	List<Portfolio> findByDailyChangeGreaterThan(BigDecimal change);

	/**
	 * Find portfolios with daily change less than amount
	 * 
	 * @param change the daily change threshold
	 * @return list of portfolios
	 */
	List<Portfolio> findByDailyChangeLessThan(BigDecimal change);

	// ============================================================
	// Custom Queries with Holdings
	// ============================================================

	/**
	 * Find portfolio by user ID with holdings eagerly loaded (avoids N+1)
	 * 
	 * @param userId the user ID
	 * @return Optional containing portfolio with holdings
	 */
	@Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.holdings WHERE p.user.id = :userId")
	Optional<Portfolio> findByUserIdWithHoldings(@Param("userId") String userId);

	/**
	 * Find all portfolios with holdings eagerly loaded
	 * 
	 * @return list of portfolios with holdings
	 */
	@Query("SELECT DISTINCT p FROM Portfolio p LEFT JOIN FETCH p.holdings")
	List<Portfolio> findAllWithHoldings();

	/**
	 * Find portfolios with at least one holding
	 * 
	 * @return list of portfolios with holdings
	 */
	@Query("SELECT p FROM Portfolio p WHERE SIZE(p.holdings) > 0")
	List<Portfolio> findPortfoliosWithHoldings();

	/**
	 * Find empty portfolios (no holdings)
	 * 
	 * @return list of empty portfolios
	 */
	@Query("SELECT p FROM Portfolio p WHERE SIZE(p.holdings) = 0")
	List<Portfolio> findEmptyPortfolios();

	// ============================================================
	// Pagination & Sorting
	// ============================================================

	/**
	 * Find all portfolios with pagination, sorted by total value descending
	 * 
	 * @param pageable pagination parameters
	 * @return page of portfolios
	 */
	Page<Portfolio> findAllByOrderByTotalValueDesc(Pageable pageable);

	/**
	 * Find portfolios by total value range with pagination
	 * 
	 * @param minValue minimum value
	 * @param maxValue maximum value
	 * @param pageable pagination parameters
	 * @return page of portfolios
	 */
	Page<Portfolio> findByTotalValueBetween(BigDecimal minValue, BigDecimal maxValue, Pageable pageable);

	// ============================================================
	// Delete Operations
	// ============================================================

	/**
	 * Delete portfolio by user ID
	 * 
	 * @param userId the user ID
	 */
	@Modifying
	@Transactional
	void deleteByUser_Id(String userId);

	/**
	 * Delete portfolio by name
	 * 
	 * @param name the portfolio name
	 */
	@Modifying
	@Transactional
	void deleteByName(String name);

	// ============================================================
	// Statistical Queries
	// ============================================================

	/**
	 * Get total portfolio value across all portfolios
	 * 
	 * @return sum of all portfolio values
	 */
	@Query("SELECT SUM(p.totalValue) FROM Portfolio p")
	BigDecimal getTotalValueAcrossAllPortfolios();

	/**
	 * Get average portfolio value
	 * 
	 * @return average portfolio value
	 */
	@Query("SELECT AVG(p.totalValue) FROM Portfolio p")
	BigDecimal getAveragePortfolioValue();

	/**
	 * Count portfolios by value range
	 * 
	 * @param minValue minimum value
	 * @param maxValue maximum value
	 * @return count of portfolios in range
	 */
	Long countByTotalValueBetween(BigDecimal minValue, BigDecimal maxValue);
}

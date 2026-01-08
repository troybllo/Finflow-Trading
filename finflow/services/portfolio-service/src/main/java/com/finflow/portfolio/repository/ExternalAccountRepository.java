package com.finflow.portfolio.repository;

import com.finflow.portfolio.domain.ConnectionStatus;
import com.finflow.portfolio.domain.ExternalAccount;
import com.finflow.portfolio.domain.ExternalPlatform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalAccountRepository extends JpaRepository<ExternalAccount, String> {

	// ============================================================
	// Basic Finders
	// ============================================================

	/**
	 * Find all external accounts for a user
	 *
	 * @param userId the user ID
	 * @return list of external accounts
	 */
	List<ExternalAccount> findByUserId(String userId);

	/**
	 * Find external account by account number
	 *
	 * @param accountNumber the account number
	 * @return Optional containing the account if found
	 */
	Optional<ExternalAccount> findByAccountNumber(String accountNumber);

	/**
	 * Find all accounts for a specific platform
	 *
	 * @param platform the external platform (ALPACA, ROBINHOOD, etc.)
	 * @return list of external accounts
	 */
	List<ExternalAccount> findByPlatform(ExternalPlatform platform);

	/**
	 * Find account by user and platform (should be unique)
	 *
	 * @param userId   the user ID
	 * @param platform the platform
	 * @return Optional containing the account if found
	 */
	Optional<ExternalAccount> findByUserIdAndPlatform(String userId, ExternalPlatform platform);

	/**
	 * Find account by name
	 *
	 * @param accountName the account name
	 * @return Optional containing the account if found
	 */
	Optional<ExternalAccount> findByAccountName(String accountName);

	// ============================================================
	// Existence Checks
	// ============================================================

	/**
	 * Check if user has any external accounts
	 *
	 * @param userId the user ID
	 * @return true if accounts exist
	 */
	boolean existsByUserId(String userId);

	/**
	 * Check if user has account on specific platform
	 *
	 * @param userId   the user ID
	 * @param platform the platform
	 * @return true if account exists
	 */
	boolean existsByUserIdAndPlatform(String userId, ExternalPlatform platform);

	/**
	 * Check if account number exists
	 *
	 * @param accountNumber the account number
	 * @return true if exists
	 */
	boolean existsByAccountNumber(String accountNumber);

	// ============================================================
	// Connection Status Queries
	// ============================================================

	/**
	 * Find accounts by connection status
	 *
	 * @param status the connection status
	 * @return list of accounts
	 */
	List<ExternalAccount> findByStatus(ConnectionStatus status);

	/**
	 * Find user's accounts by status
	 *
	 * @param userId the user ID
	 * @param status the connection status
	 * @return list of accounts
	 */
	List<ExternalAccount> findByUserIdAndStatus(String userId, ConnectionStatus status);

	/**
	 * Find connected accounts (status = CONNECTED)
	 *
	 * @return list of connected accounts
	 */
	@Query("SELECT ea FROM ExternalAccount ea WHERE ea.status = 'CONNECTED'")
	List<ExternalAccount> findConnectedAccounts();

	/**
	 * Find user's connected accounts
	 *
	 * @param userId the user ID
	 * @return list of connected accounts
	 */
	@Query("SELECT ea FROM ExternalAccount ea WHERE ea.userId = :userId AND ea.status = 'CONNECTED'")
	List<ExternalAccount> findUserConnectedAccounts(@Param("userId") String userId);

	/**
	 * Find accounts with errors (status = ERROR)
	 *
	 * @return list of accounts with errors
	 */
	@Query("SELECT ea FROM ExternalAccount ea WHERE ea.status = 'ERROR'")
	List<ExternalAccount> findAccountsWithErrors();

	// ============================================================
	// Sync-Related Queries
	// ============================================================

	/**
	 * Find accounts by sync enabled status
	 *
	 * @param syncEnabled whether sync is enabled
	 * @return list of accounts
	 */
	List<ExternalAccount> findBySyncEnabled(boolean syncEnabled);

	/**
	 * Find user's accounts with sync enabled
	 *
	 * @param userId      the user ID
	 * @param syncEnabled whether sync is enabled
	 * @return list of accounts
	 */
	List<ExternalAccount> findByUserIdAndSyncEnabled(String userId, boolean syncEnabled);

	/**
	 * Find accounts ready to sync (connected and sync enabled)
	 *
	 * @return list of accounts ready to sync
	 */
	@Query("SELECT ea FROM ExternalAccount ea WHERE ea.status = 'CONNECTED' AND ea.syncEnabled = true")
	List<ExternalAccount> findAccountsReadyToSync();

	/**
	 * Find accounts that haven't synced recently
	 *
	 * @param since the datetime threshold
	 * @return list of accounts needing sync
	 */
	@Query("SELECT ea FROM ExternalAccount ea WHERE ea.syncEnabled = true AND " +
	       "(ea.lastSyncAt IS NULL OR ea.lastSyncAt < :since)")
	List<ExternalAccount> findAccountsNeedingSync(@Param("since") LocalDateTime since);

	/**
	 * Find accounts last synced after a specific date
	 *
	 * @param date the date threshold
	 * @return list of recently synced accounts
	 */
	List<ExternalAccount> findByLastSyncAtAfter(LocalDateTime date);

	/**
	 * Find accounts last synced before a specific date
	 *
	 * @param date the date threshold
	 * @return list of stale accounts
	 */
	List<ExternalAccount> findByLastSyncAtBefore(LocalDateTime date);

	// ============================================================
	// Token Management Queries
	// ============================================================

	/**
	 * Find accounts with expired tokens
	 *
	 * @param now the current datetime
	 * @return list of accounts with expired tokens
	 */
	@Query("SELECT ea FROM ExternalAccount ea WHERE ea.tokenExpiresAt < :now")
	List<ExternalAccount> findAccountsWithExpiredTokens(@Param("now") LocalDateTime now);

	/**
	 * Find accounts with tokens expiring soon
	 *
	 * @param threshold the datetime threshold
	 * @return list of accounts needing token refresh
	 */
	@Query("SELECT ea FROM ExternalAccount ea WHERE ea.tokenExpiresAt < :threshold AND ea.tokenExpiresAt > :now")
	List<ExternalAccount> findAccountsNeedingTokenRefresh(
		@Param("threshold") LocalDateTime threshold,
		@Param("now") LocalDateTime now
	);

	/**
	 * Find accounts with non-null refresh tokens
	 *
	 * @return list of accounts with refresh tokens
	 */
	@Query("SELECT ea FROM ExternalAccount ea WHERE ea.refreshToken IS NOT NULL")
	List<ExternalAccount> findAccountsWithRefreshTokens();

	// ============================================================
	// Platform-Specific Queries
	// ============================================================

	/**
	 * Find user's accounts by platform and status
	 *
	 * @param userId   the user ID
	 * @param platform the platform
	 * @param status   the connection status
	 * @return list of accounts
	 */
	List<ExternalAccount> findByUserIdAndPlatformAndStatus(
		String userId,
		ExternalPlatform platform,
		ConnectionStatus status
	);

	/**
	 * Count accounts by platform
	 *
	 * @param platform the platform
	 * @return count of accounts
	 */
	Long countByPlatform(ExternalPlatform platform);

	/**
	 * Count user's accounts by platform
	 *
	 * @param userId   the user ID
	 * @param platform the platform
	 * @return count of accounts
	 */
	Long countByUserIdAndPlatform(String userId, ExternalPlatform platform);

	// ============================================================
	// Date-Based Queries
	// ============================================================

	/**
	 * Find accounts created after a specific date
	 *
	 * @param date the date threshold
	 * @return list of accounts
	 */
	List<ExternalAccount> findByCreatedAtAfter(LocalDateTime date);

	/**
	 * Find accounts created before a specific date
	 *
	 * @param date the date threshold
	 * @return list of accounts
	 */
	List<ExternalAccount> findByCreatedAtBefore(LocalDateTime date);

	/**
	 * Find accounts created within a date range
	 *
	 * @param startDate the start date
	 * @param endDate   the end date
	 * @return list of accounts
	 */
	List<ExternalAccount> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Find accounts updated after a specific date
	 *
	 * @param date the date threshold
	 * @return list of accounts
	 */
	List<ExternalAccount> findByUpdatedAtAfter(LocalDateTime date);

	// ============================================================
	// Pagination Support
	// ============================================================

	/**
	 * Find user's accounts with pagination
	 *
	 * @param userId   the user ID
	 * @param pageable pagination parameters
	 * @return page of accounts
	 */
	Page<ExternalAccount> findByUserId(String userId, Pageable pageable);

	/**
	 * Find accounts by platform with pagination
	 *
	 * @param platform the platform
	 * @param pageable pagination parameters
	 * @return page of accounts
	 */
	Page<ExternalAccount> findByPlatform(ExternalPlatform platform, Pageable pageable);

	/**
	 * Find accounts by status with pagination
	 *
	 * @param status   the connection status
	 * @param pageable pagination parameters
	 * @return page of accounts
	 */
	Page<ExternalAccount> findByStatus(ConnectionStatus status, Pageable pageable);

	// ============================================================
	// Delete Operations
	// ============================================================

	/**
	 * Delete all accounts for a user
	 *
	 * @param userId the user ID
	 */
	@Modifying
	@Transactional
	void deleteByUserId(String userId);

	/**
	 * Delete account by user and platform
	 *
	 * @param userId   the user ID
	 * @param platform the platform
	 */
	@Modifying
	@Transactional
	void deleteByUserIdAndPlatform(String userId, ExternalPlatform platform);

	/**
	 * Delete accounts with specific status
	 *
	 * @param status the connection status
	 */
	@Modifying
	@Transactional
	void deleteByStatus(ConnectionStatus status);

	// ============================================================
	// Statistical Queries
	// ============================================================

	/**
	 * Count all accounts for a user
	 *
	 * @param userId the user ID
	 * @return count of accounts
	 */
	Long countByUserId(String userId);

	/**
	 * Count accounts by status
	 *
	 * @param status the connection status
	 * @return count of accounts
	 */
	Long countByStatus(ConnectionStatus status);

	/**
	 * Count sync-enabled accounts
	 *
	 * @param syncEnabled whether sync is enabled
	 * @return count of accounts
	 */
	Long countBySyncEnabled(boolean syncEnabled);

	/**
	 * Get platform distribution statistics
	 *
	 * @return list of [platform, count] pairs
	 */
	@Query("SELECT ea.platform, COUNT(ea) FROM ExternalAccount ea GROUP BY ea.platform")
	List<Object[]> getPlatformDistribution();

	/**
	 * Get status distribution statistics
	 *
	 * @return list of [status, count] pairs
	 */
	@Query("SELECT ea.status, COUNT(ea) FROM ExternalAccount ea GROUP BY ea.status")
	List<Object[]> getStatusDistribution();
}

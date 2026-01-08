package com.finflow.portfolio.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finflow.portfolio.domain.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	// ============================================================
	// Basic Finders
	// ============================================================

	/**
	 * Find user by email address
	 * @param email the email to search for
	 * @return Optional containing the user if found
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Find user by username
	 * @param username the username to search for
	 * @return Optional containing the user if found
	 */
	Optional<User> findByUsername(String username);

	/**
	 * Find user by email AND username (both must match)
	 * @param email the email
	 * @param username the username
	 * @return Optional containing the user if found
	 */
	Optional<User> findByEmailAndUsername(String email, String username);

	/**
	 * Find users by email OR username (either matches)
	 * @param email the email
	 * @param username the username
	 * @return list of matching users
	 */
	List<User> findByEmailOrUsername(String email, String username);

	/**
	 * Find user by email OR username (useful for login with single identifier)
	 * @param identifier email or username
	 * @return Optional containing the user if found
	 */
	@Query("SELECT u FROM User u WHERE u.email = :identifier OR u.username = :identifier")
	Optional<User> findByEmailOrUsernameIdentifier(@Param("identifier") String identifier);

	/**
	 * Find all users with non-null email addresses
	 * @return list of users with email
	 */
	List<User> findByEmailIsNotNull();

	// ============================================================
	// Existence Checks (for validation)
	// ============================================================

	/**
	 * Check if email already exists (for registration validation)
	 * @param email the email to check
	 * @return true if email exists, false otherwise
	 */
	boolean existsByEmail(String email);

	/**
	 * Check if username already exists (for registration validation)
	 * @param username the username to check
	 * @return true if username exists, false otherwise
	 */
	boolean existsByUsername(String username);

	// ============================================================
	// Date-based Queries
	// ============================================================

	/**
	 * Find users created after a specific date
	 * @param date the date to compare against
	 * @return list of users created after the date
	 */
	List<User> findByCreatedAtAfter(LocalDateTime date);

	/**
	 * Find users created before a specific date
	 * @param date the date to compare against
	 * @return list of users created before the date
	 */
	List<User> findByCreatedAtBefore(LocalDateTime date);

	/**
	 * Find users created within a date range
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return list of users created within the range
	 */
	List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * Find users updated after a specific date
	 * @param date the date to compare against
	 * @return list of users updated after the date
	 */
	List<User> findByUpdatedAtAfter(LocalDateTime date);

	/**
	 * Find users updated before a specific date
	 * @param date the date to compare against
	 * @return list of users updated before the date
	 */
	List<User> findByUpdatedAtBefore(LocalDateTime date);

	// ============================================================
	// Pagination Support
	// ============================================================

	/**
	 * Find users created after a date with pagination
	 * @param date the date to compare against
	 * @param pageable pagination parameters
	 * @return page of users
	 */
	Page<User> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);

	/**
	 * Search users by username or email (case-insensitive) with pagination
	 * @param search the search term
	 * @param pageable pagination parameters
	 * @return page of matching users
	 */
	@Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
	       "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
	Page<User> searchUsers(@Param("search") String search, Pageable pageable);

	// ============================================================
	// Delete Operations
	// ============================================================

	/**
	 * Delete user by email
	 * @param email the email of the user to delete
	 */
	@Modifying
	@Transactional
	void deleteByEmail(String email);

	/**
	 * Delete user by username
	 * @param username the username of the user to delete
	 */
	@Modifying
	@Transactional
	void deleteByUsername(String username);

	// ============================================================
	// Custom Queries with Relationships
	// ============================================================

	/**
	 * Find user by ID with portfolio eagerly loaded (avoids N+1 query)
	 * @param id the user ID
	 * @return Optional containing the user with portfolio if found
	 */
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.portfolio WHERE u.id = :id")
	Optional<User> findByIdWithPortfolio(@Param("id") String id);

	/**
	 * Find all users who have a portfolio
	 * @return list of users with portfolios
	 */
	@Query("SELECT u FROM User u WHERE u.portfolio IS NOT NULL")
	List<User> findUsersWithPortfolios();

	/**
	 * Find all users who don't have a portfolio yet
	 * @return list of users without portfolios
	 */
	@Query("SELECT u FROM User u WHERE u.portfolio IS NULL")
  List<User> findUsersWithoutPortfolios();
}

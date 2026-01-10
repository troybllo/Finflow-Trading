package com.finflow.portfolio.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finflow.portfolio.domain.User;
import com.finflow.portfolio.dto.request.CreateUserRequest;
import com.finflow.portfolio.dto.request.UpdateUserRequest;
import com.finflow.portfolio.dto.response.PaginatedResponse;
import com.finflow.portfolio.dto.response.UserResponse;
import com.finflow.portfolio.exception.ConflictException;
import com.finflow.portfolio.exception.ResourceNotFoundException;
import com.finflow.portfolio.repository.UserRepository;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public UserResponse createUser(CreateUserRequest request) {

		if (userRepository.existsByUsername(request.username())) {
			throw new ConflictException("User", "username", request.username());
		}

		if (userRepository.existsByEmail(request.email())) {
			throw new ConflictException("User", "email", request.email());
		}

		// 2. Convert DTO -> Entity

		User user = new User();
		user.setUsername(request.username());
		user.setEmail(request.email());

		// 3. Apply business logic (hash password)
		user.setPasswordHash(passwordEncoder.encode(request.password()));

		// 4. Save via repository

		User savedUser = userRepository.save(user);

		// 5. Convert Entity -> Response DTO

		return UserResponse.from(savedUser);
	}

	@Transactional(readOnly = true)
	public UserResponse getUserById(String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
		return UserResponse.from(user);
	}

	@Transactional(readOnly = true)
	public UserResponse getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
		return UserResponse.from(user);
	}

	@Transactional(readOnly = true)
	public UserResponse getUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
		return UserResponse.from(user);
	}

	@Transactional(readOnly = true)
	public UserResponse getUserWithPortfolio(String userId) {
		User user = userRepository.findByIdWithPortfolio(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", userId));
		return UserResponse.from(user);
	}

	public UserResponse updateUser(String userId, UpdateUserRequest request) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));

		if (request.username() != null) {
			if (!request.username().equals(user.getUsername()) && userRepository.existsByUsername(request.username())) {
				throw new ConflictException("User", "username", request.username());
			}
			user.setUsername(request.username());
		}

		if (request.email() != null) {
			if (!request.email().equals(user.getEmail()) && userRepository.existsByEmail(request.email())) {
				throw new ConflictException("User", "email", request.email());
			}
			user.setEmail(request.email());
		}

		if (request.avatar() != null) {
			user.setAvatar(request.avatar());
		}

		User updatedUser = userRepository.save(user);
		return UserResponse.from(updatedUser);

	}

	public void deleteUser(String userId) {
		if (!userRepository.existsById(userId)) {
			throw new ResourceNotFoundException("User", userId);
		}
		userRepository.deleteById(userId);
	}

	@Transactional(readOnly = true)
	public PaginatedResponse<UserResponse> searchUsers(String search, Pageable pageable) {
		Page<User> userPage = userRepository.searchUsers(search, pageable);
		return PaginatedResponse.from(userPage, UserResponse::from);
	}

}

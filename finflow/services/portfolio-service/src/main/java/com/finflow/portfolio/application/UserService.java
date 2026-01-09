package com.finflow.portfolio.application;

import com.finflow.portfolio.repository.UserRepository;

public class UserService { 

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

}

package com.finflow.portfolio.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

    @Size(min = 3, max = 50) @NotBlank(message = "Username is required") String username,

    @Size(max = 255, message = "Email must not exceed 255 characters") @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

    @Size(min = 8, message = "Password must be atleast 8 characters") @NotBlank(message = "Password is required") String password) {
}

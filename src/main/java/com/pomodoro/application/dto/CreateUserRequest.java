package com.pomodoro.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating a new User.
 */
public record CreateUserRequest(
    @NotBlank(message = "Username is required")
    String username,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email
) {}

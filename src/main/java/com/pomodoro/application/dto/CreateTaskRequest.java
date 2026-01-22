package com.pomodoro.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * DTO for creating a new Task.
 */
public record CreateTaskRequest(
    @NotBlank(message = "Title is required")
    String title,
    
    String description,
    
    @Positive(message = "Estimated pomodoros must be positive")
    int estimatedPomodoros,
    
    UUID userId
) {}

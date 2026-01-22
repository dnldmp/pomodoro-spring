package com.pomodoro.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for starting a Pomodoro Session.
 */
public record StartPomodoroRequest(
    @NotNull(message = "User ID is required")
    UUID userId,
    
    UUID taskId,
    
    String sessionType
) {}

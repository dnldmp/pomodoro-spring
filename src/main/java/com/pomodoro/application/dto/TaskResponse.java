package com.pomodoro.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Task response.
 */
public record TaskResponse(
    UUID id,
    String title,
    String description,
    boolean completed,
    int estimatedPomodoros,
    int completedPomodoros,
    double progress,
    UUID userId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

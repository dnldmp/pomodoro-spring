package com.pomodoro.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for User response.
 */
public record UserResponse(
    UUID id,
    String username,
    String email,
    int xp,
    int level,
    int xpToNextLevel,
    int totalPomodorosCompleted,
    int totalTasksCompleted,
    List<RewardResponse> rewards,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}

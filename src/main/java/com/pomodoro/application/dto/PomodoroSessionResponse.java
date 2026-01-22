package com.pomodoro.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Pomodoro Session response.
 */
public record PomodoroSessionResponse(
    UUID id,
    UUID userId,
    UUID taskId,
    String type,
    String status,
    int durationMinutes,
    LocalDateTime startedAt,
    LocalDateTime completedAt,
    LocalDateTime createdAt
) {}

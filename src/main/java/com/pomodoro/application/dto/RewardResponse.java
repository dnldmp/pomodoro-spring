package com.pomodoro.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Reward response.
 */
public record RewardResponse(
    UUID id,
    String name,
    String description,
    String type,
    int requiredXp,
    String iconUrl,
    LocalDateTime earnedAt,
    boolean earned
) {}

package com.pomodoro.application.dto;

/**
 * DTO for updating a Task.
 */
public record UpdateTaskRequest(
    String title,
    String description,
    Integer estimatedPomodoros
) {}

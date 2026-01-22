package com.pomodoro.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a Pomodoro Session.
 */
public class PomodoroSession {
    
    public enum SessionType {
        WORK, SHORT_BREAK, LONG_BREAK
    }
    
    public enum SessionStatus {
        NOT_STARTED, IN_PROGRESS, PAUSED, COMPLETED, CANCELLED
    }
    
    private UUID id;
    private UUID userId;
    private UUID taskId;
    private SessionType type;
    private SessionStatus status;
    private int durationMinutes;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    
    public PomodoroSession() {
        this.id = UUID.randomUUID();
        this.status = SessionStatus.NOT_STARTED;
        this.createdAt = LocalDateTime.now();
    }
    
    public PomodoroSession(UUID userId, UUID taskId, SessionType type, int durationMinutes) {
        this();
        this.userId = userId;
        this.taskId = taskId;
        this.type = type;
        this.durationMinutes = durationMinutes;
    }
    
    public void start() {
        if (this.status == SessionStatus.NOT_STARTED || this.status == SessionStatus.PAUSED) {
            this.status = SessionStatus.IN_PROGRESS;
            if (this.startedAt == null) {
                this.startedAt = LocalDateTime.now();
            }
        }
    }
    
    public void pause() {
        if (this.status == SessionStatus.IN_PROGRESS) {
            this.status = SessionStatus.PAUSED;
        }
    }
    
    public void complete() {
        this.status = SessionStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
    
    public void cancel() {
        this.status = SessionStatus.CANCELLED;
    }
    
    public boolean isWorkSession() {
        return this.type == SessionType.WORK;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public UUID getTaskId() {
        return taskId;
    }
    
    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
    
    public SessionType getType() {
        return type;
    }
    
    public void setType(SessionType type) {
        this.type = type;
    }
    
    public SessionStatus getStatus() {
        return status;
    }
    
    public void setStatus(SessionStatus status) {
        this.status = status;
    }
    
    public int getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

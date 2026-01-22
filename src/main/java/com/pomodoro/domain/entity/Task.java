package com.pomodoro.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a Task in the Pomodoro system.
 * Following Clean Architecture principles, this entity is independent of any framework.
 */
public class Task {
    
    private UUID id;
    private String title;
    private String description;
    private boolean completed;
    private int estimatedPomodoros;
    private int completedPomodoros;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Task() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.completed = false;
        this.completedPomodoros = 0;
    }
    
    public Task(String title, String description, int estimatedPomodoros, UUID userId) {
        this();
        this.title = title;
        this.description = description;
        this.estimatedPomodoros = estimatedPomodoros;
        this.userId = userId;
    }
    
    public void complete() {
        this.completed = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementCompletedPomodoros() {
        this.completedPomodoros++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isCompleted() {
        return completed;
    }
    
    public double getProgress() {
        if (estimatedPomodoros == 0) return 0;
        return (double) completedPomodoros / estimatedPomodoros * 100;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getEstimatedPomodoros() {
        return estimatedPomodoros;
    }
    
    public void setEstimatedPomodoros(int estimatedPomodoros) {
        this.estimatedPomodoros = estimatedPomodoros;
    }
    
    public int getCompletedPomodoros() {
        return completedPomodoros;
    }
    
    public void setCompletedPomodoros(int completedPomodoros) {
        this.completedPomodoros = completedPomodoros;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

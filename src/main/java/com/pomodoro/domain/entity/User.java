package com.pomodoro.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Domain entity representing a User in the Pomodoro system.
 * Contains XP, level, and rewards information.
 */
public class User {
    
    private UUID id;
    private String username;
    private String email;
    private int xp;
    private int level;
    private int totalPomodorosCompleted;
    private int totalTasksCompleted;
    private List<Reward> rewards;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private static final int XP_PER_LEVEL = 100;
    
    public User() {
        this.id = UUID.randomUUID();
        this.xp = 0;
        this.level = 1;
        this.totalPomodorosCompleted = 0;
        this.totalTasksCompleted = 0;
        this.rewards = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public User(String username, String email) {
        this();
        this.username = username;
        this.email = email;
    }
    
    public void addXp(int amount) {
        this.xp += amount;
        checkLevelUp();
        this.updatedAt = LocalDateTime.now();
    }
    
    private void checkLevelUp() {
        int newLevel = (xp / XP_PER_LEVEL) + 1;
        if (newLevel > this.level) {
            this.level = newLevel;
        }
    }
    
    public void incrementPomodorosCompleted() {
        this.totalPomodorosCompleted++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementTasksCompleted() {
        this.totalTasksCompleted++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addReward(Reward reward) {
        this.rewards.add(reward);
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getXpToNextLevel() {
        return (level * XP_PER_LEVEL) - xp;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getXp() {
        return xp;
    }
    
    public void setXp(int xp) {
        this.xp = xp;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public int getTotalPomodorosCompleted() {
        return totalPomodorosCompleted;
    }
    
    public void setTotalPomodorosCompleted(int totalPomodorosCompleted) {
        this.totalPomodorosCompleted = totalPomodorosCompleted;
    }
    
    public int getTotalTasksCompleted() {
        return totalTasksCompleted;
    }
    
    public void setTotalTasksCompleted(int totalTasksCompleted) {
        this.totalTasksCompleted = totalTasksCompleted;
    }
    
    public List<Reward> getRewards() {
        return rewards;
    }
    
    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
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
}

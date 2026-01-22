package com.pomodoro.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain entity representing a Reward in the Pomodoro system.
 */
public class Reward {
    
    public enum RewardType {
        BADGE, ACHIEVEMENT, MILESTONE
    }
    
    private UUID id;
    private String name;
    private String description;
    private RewardType type;
    private int requiredXp;
    private String iconUrl;
    private LocalDateTime earnedAt;
    
    public Reward() {
        this.id = UUID.randomUUID();
    }
    
    public Reward(String name, String description, RewardType type, int requiredXp) {
        this();
        this.name = name;
        this.description = description;
        this.type = type;
        this.requiredXp = requiredXp;
    }
    
    public void earn() {
        this.earnedAt = LocalDateTime.now();
    }
    
    public boolean isEarned() {
        return earnedAt != null;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public RewardType getType() {
        return type;
    }
    
    public void setType(RewardType type) {
        this.type = type;
    }
    
    public int getRequiredXp() {
        return requiredXp;
    }
    
    public void setRequiredXp(int requiredXp) {
        this.requiredXp = requiredXp;
    }
    
    public String getIconUrl() {
        return iconUrl;
    }
    
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    
    public LocalDateTime getEarnedAt() {
        return earnedAt;
    }
    
    public void setEarnedAt(LocalDateTime earnedAt) {
        this.earnedAt = earnedAt;
    }
}

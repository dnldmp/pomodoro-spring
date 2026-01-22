package com.pomodoro.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for User domain entity.
 */
class UserTest {
    
    @Test
    void shouldCreateUserWithDefaultValues() {
        User user = new User();
        
        assertNotNull(user.getId());
        assertEquals(0, user.getXp());
        assertEquals(1, user.getLevel());
        assertEquals(0, user.getTotalPomodorosCompleted());
        assertEquals(0, user.getTotalTasksCompleted());
        assertNotNull(user.getRewards());
        assertTrue(user.getRewards().isEmpty());
    }
    
    @Test
    void shouldCreateUserWithParameters() {
        User user = new User("testuser", "test@example.com");
        
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }
    
    @Test
    void shouldAddXp() {
        User user = new User("testuser", "test@example.com");
        
        user.addXp(50);
        
        assertEquals(50, user.getXp());
    }
    
    @Test
    void shouldLevelUpWhenXpReachesThreshold() {
        User user = new User("testuser", "test@example.com");
        
        user.addXp(100);
        
        assertEquals(100, user.getXp());
        assertEquals(2, user.getLevel());
    }
    
    @Test
    void shouldLevelUpMultipleTimes() {
        User user = new User("testuser", "test@example.com");
        
        user.addXp(250);
        
        assertEquals(250, user.getXp());
        assertEquals(3, user.getLevel());
    }
    
    @Test
    void shouldCalculateXpToNextLevel() {
        User user = new User("testuser", "test@example.com");
        
        assertEquals(100, user.getXpToNextLevel());
        
        user.addXp(30);
        
        assertEquals(70, user.getXpToNextLevel());
    }
    
    @Test
    void shouldIncrementPomodorosCompleted() {
        User user = new User("testuser", "test@example.com");
        
        user.incrementPomodorosCompleted();
        user.incrementPomodorosCompleted();
        
        assertEquals(2, user.getTotalPomodorosCompleted());
    }
    
    @Test
    void shouldIncrementTasksCompleted() {
        User user = new User("testuser", "test@example.com");
        
        user.incrementTasksCompleted();
        
        assertEquals(1, user.getTotalTasksCompleted());
    }
    
    @Test
    void shouldAddReward() {
        User user = new User("testuser", "test@example.com");
        Reward reward = new Reward("First Pomodoro", "Complete your first pomodoro", 
            Reward.RewardType.BADGE, 10);
        
        user.addReward(reward);
        
        assertEquals(1, user.getRewards().size());
        assertEquals("First Pomodoro", user.getRewards().get(0).getName());
    }
}

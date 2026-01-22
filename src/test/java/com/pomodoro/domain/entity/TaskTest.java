package com.pomodoro.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Task domain entity.
 */
class TaskTest {
    
    @Test
    void shouldCreateTaskWithDefaultValues() {
        Task task = new Task();
        
        assertNotNull(task.getId());
        assertFalse(task.isCompleted());
        assertEquals(0, task.getCompletedPomodoros());
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
    }
    
    @Test
    void shouldCreateTaskWithParameters() {
        UUID userId = UUID.randomUUID();
        Task task = new Task("Test Task", "Description", 4, userId);
        
        assertEquals("Test Task", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertEquals(4, task.getEstimatedPomodoros());
        assertEquals(userId, task.getUserId());
        assertFalse(task.isCompleted());
    }
    
    @Test
    void shouldCompleteTask() {
        Task task = new Task("Test Task", "Description", 4, UUID.randomUUID());
        
        task.complete();
        
        assertTrue(task.isCompleted());
    }
    
    @Test
    void shouldIncrementCompletedPomodoros() {
        Task task = new Task("Test Task", "Description", 4, UUID.randomUUID());
        
        task.incrementCompletedPomodoros();
        task.incrementCompletedPomodoros();
        
        assertEquals(2, task.getCompletedPomodoros());
    }
    
    @Test
    void shouldCalculateProgressCorrectly() {
        Task task = new Task("Test Task", "Description", 4, UUID.randomUUID());
        
        assertEquals(0, task.getProgress());
        
        task.incrementCompletedPomodoros();
        task.incrementCompletedPomodoros();
        
        assertEquals(50.0, task.getProgress());
    }
    
    @Test
    void shouldReturnZeroProgressWhenNoEstimatedPomodoros() {
        Task task = new Task("Test Task", "Description", 0, UUID.randomUUID());
        
        assertEquals(0, task.getProgress());
    }
}

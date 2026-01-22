package com.pomodoro.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PomodoroSession domain entity.
 */
class PomodoroSessionTest {
    
    @Test
    void shouldCreateSessionWithDefaultValues() {
        PomodoroSession session = new PomodoroSession();
        
        assertNotNull(session.getId());
        assertEquals(PomodoroSession.SessionStatus.NOT_STARTED, session.getStatus());
        assertNotNull(session.getCreatedAt());
    }
    
    @Test
    void shouldCreateSessionWithParameters() {
        UUID userId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        
        PomodoroSession session = new PomodoroSession(
            userId, taskId, PomodoroSession.SessionType.WORK, 25);
        
        assertEquals(userId, session.getUserId());
        assertEquals(taskId, session.getTaskId());
        assertEquals(PomodoroSession.SessionType.WORK, session.getType());
        assertEquals(25, session.getDurationMinutes());
    }
    
    @Test
    void shouldStartSession() {
        PomodoroSession session = new PomodoroSession(
            UUID.randomUUID(), UUID.randomUUID(), PomodoroSession.SessionType.WORK, 25);
        
        session.start();
        
        assertEquals(PomodoroSession.SessionStatus.IN_PROGRESS, session.getStatus());
        assertNotNull(session.getStartedAt());
    }
    
    @Test
    void shouldPauseSession() {
        PomodoroSession session = new PomodoroSession(
            UUID.randomUUID(), UUID.randomUUID(), PomodoroSession.SessionType.WORK, 25);
        session.start();
        
        session.pause();
        
        assertEquals(PomodoroSession.SessionStatus.PAUSED, session.getStatus());
    }
    
    @Test
    void shouldResumeSession() {
        PomodoroSession session = new PomodoroSession(
            UUID.randomUUID(), UUID.randomUUID(), PomodoroSession.SessionType.WORK, 25);
        session.start();
        session.pause();
        
        session.start(); // resume
        
        assertEquals(PomodoroSession.SessionStatus.IN_PROGRESS, session.getStatus());
    }
    
    @Test
    void shouldCompleteSession() {
        PomodoroSession session = new PomodoroSession(
            UUID.randomUUID(), UUID.randomUUID(), PomodoroSession.SessionType.WORK, 25);
        session.start();
        
        session.complete();
        
        assertEquals(PomodoroSession.SessionStatus.COMPLETED, session.getStatus());
        assertNotNull(session.getCompletedAt());
    }
    
    @Test
    void shouldCancelSession() {
        PomodoroSession session = new PomodoroSession(
            UUID.randomUUID(), UUID.randomUUID(), PomodoroSession.SessionType.WORK, 25);
        session.start();
        
        session.cancel();
        
        assertEquals(PomodoroSession.SessionStatus.CANCELLED, session.getStatus());
    }
    
    @Test
    void shouldIdentifyWorkSession() {
        PomodoroSession workSession = new PomodoroSession(
            UUID.randomUUID(), UUID.randomUUID(), PomodoroSession.SessionType.WORK, 25);
        PomodoroSession breakSession = new PomodoroSession(
            UUID.randomUUID(), null, PomodoroSession.SessionType.SHORT_BREAK, 5);
        
        assertTrue(workSession.isWorkSession());
        assertFalse(breakSession.isWorkSession());
    }
}

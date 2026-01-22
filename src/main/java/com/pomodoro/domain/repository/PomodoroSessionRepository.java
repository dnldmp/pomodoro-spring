package com.pomodoro.domain.repository;

import com.pomodoro.domain.entity.PomodoroSession;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for PomodoroSession entity following Clean Architecture principles.
 */
public interface PomodoroSessionRepository {
    
    PomodoroSession save(PomodoroSession session);
    
    Optional<PomodoroSession> findById(UUID id);
    
    List<PomodoroSession> findByUserId(UUID userId);
    
    List<PomodoroSession> findByTaskId(UUID taskId);
    
    List<PomodoroSession> findByUserIdAndStatus(UUID userId, PomodoroSession.SessionStatus status);
    
    void deleteById(UUID id);
    
    boolean existsById(UUID id);
}

package com.pomodoro.application.usecase;

import com.pomodoro.application.dto.PomodoroSessionResponse;
import com.pomodoro.application.dto.StartPomodoroRequest;
import com.pomodoro.domain.entity.PomodoroSession;
import com.pomodoro.domain.entity.PomodoroSession.SessionStatus;
import com.pomodoro.domain.entity.PomodoroSession.SessionType;
import com.pomodoro.domain.exception.BusinessException;
import com.pomodoro.domain.exception.ResourceNotFoundException;
import com.pomodoro.domain.repository.PomodoroSessionRepository;
import com.pomodoro.domain.repository.TaskRepository;
import com.pomodoro.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Use case for managing Pomodoro Sessions.
 * Handles starting, pausing, completing, and cancelling pomodoro sessions.
 */
@Service
@Transactional
public class PomodoroSessionUseCase {
    
    private final PomodoroSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserUseCase userUseCase;
    private final TaskUseCase taskUseCase;
    
    @Value("${pomodoro.default-work-minutes:25}")
    private int defaultWorkMinutes;
    
    @Value("${pomodoro.default-break-minutes:5}")
    private int defaultBreakMinutes;
    
    @Value("${pomodoro.default-long-break-minutes:15}")
    private int defaultLongBreakMinutes;
    
    public PomodoroSessionUseCase(
            PomodoroSessionRepository sessionRepository,
            UserRepository userRepository,
            TaskRepository taskRepository,
            UserUseCase userUseCase,
            TaskUseCase taskUseCase) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userUseCase = userUseCase;
        this.taskUseCase = taskUseCase;
    }
    
    public PomodoroSessionResponse startSession(StartPomodoroRequest request) {
        if (!userRepository.existsById(request.userId())) {
            throw new ResourceNotFoundException("User", "id", request.userId());
        }
        
        if (request.taskId() != null && !taskRepository.existsById(request.taskId())) {
            throw new ResourceNotFoundException("Task", "id", request.taskId());
        }
        
        // Check for active sessions
        List<PomodoroSession> activeSessions = sessionRepository.findByUserIdAndStatus(
            request.userId(), SessionStatus.IN_PROGRESS);
        if (!activeSessions.isEmpty()) {
            throw new BusinessException("User already has an active session");
        }
        
        SessionType type = parseSessionType(request.sessionType());
        int duration = getDurationForType(type);
        
        PomodoroSession session = new PomodoroSession(
            request.userId(),
            request.taskId(),
            type,
            duration
        );
        session.start();
        
        PomodoroSession savedSession = sessionRepository.save(session);
        return toResponse(savedSession);
    }
    
    public PomodoroSessionResponse pauseSession(UUID sessionId) {
        PomodoroSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Session", "id", sessionId));
        
        session.pause();
        PomodoroSession updatedSession = sessionRepository.save(session);
        return toResponse(updatedSession);
    }
    
    public PomodoroSessionResponse resumeSession(UUID sessionId) {
        PomodoroSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Session", "id", sessionId));
        
        session.start();
        PomodoroSession updatedSession = sessionRepository.save(session);
        return toResponse(updatedSession);
    }
    
    public PomodoroSessionResponse completeSession(UUID sessionId) {
        PomodoroSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Session", "id", sessionId));
        
        session.complete();
        PomodoroSession updatedSession = sessionRepository.save(session);
        
        // Award XP for work sessions
        if (session.isWorkSession()) {
            userUseCase.addPomodoroXp(session.getUserId());
            
            // Increment task pomodoro count if associated
            if (session.getTaskId() != null) {
                taskUseCase.incrementPomodoro(session.getTaskId());
            }
        }
        
        return toResponse(updatedSession);
    }
    
    public PomodoroSessionResponse cancelSession(UUID sessionId) {
        PomodoroSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Session", "id", sessionId));
        
        session.cancel();
        PomodoroSession updatedSession = sessionRepository.save(session);
        return toResponse(updatedSession);
    }
    
    @Transactional(readOnly = true)
    public PomodoroSessionResponse getSessionById(UUID id) {
        PomodoroSession session = sessionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Session", "id", id));
        return toResponse(session);
    }
    
    @Transactional(readOnly = true)
    public List<PomodoroSessionResponse> getSessionsByUserId(UUID userId) {
        return sessionRepository.findByUserId(userId).stream()
            .map(this::toResponse)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public List<PomodoroSessionResponse> getSessionsByTaskId(UUID taskId) {
        return sessionRepository.findByTaskId(taskId).stream()
            .map(this::toResponse)
            .toList();
    }
    
    private SessionType parseSessionType(String type) {
        if (type == null || type.isBlank()) {
            return SessionType.WORK;
        }
        return switch (type.toUpperCase()) {
            case "WORK" -> SessionType.WORK;
            case "SHORT_BREAK", "BREAK" -> SessionType.SHORT_BREAK;
            case "LONG_BREAK" -> SessionType.LONG_BREAK;
            default -> SessionType.WORK;
        };
    }
    
    private int getDurationForType(SessionType type) {
        return switch (type) {
            case WORK -> defaultWorkMinutes;
            case SHORT_BREAK -> defaultBreakMinutes;
            case LONG_BREAK -> defaultLongBreakMinutes;
        };
    }
    
    private PomodoroSessionResponse toResponse(PomodoroSession session) {
        return new PomodoroSessionResponse(
            session.getId(),
            session.getUserId(),
            session.getTaskId(),
            session.getType().name(),
            session.getStatus().name(),
            session.getDurationMinutes(),
            session.getStartedAt(),
            session.getCompletedAt(),
            session.getCreatedAt()
        );
    }
}

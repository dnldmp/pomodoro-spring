package com.pomodoro.infrastructure.persistence.mapper;

import com.pomodoro.domain.entity.PomodoroSession;
import com.pomodoro.domain.entity.PomodoroSession.SessionStatus;
import com.pomodoro.domain.entity.PomodoroSession.SessionType;
import com.pomodoro.infrastructure.persistence.entity.PomodoroSessionJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between PomodoroSession domain entity and JPA entity.
 */
@Component
public class PomodoroSessionMapper {
    
    public PomodoroSessionJpaEntity toJpaEntity(PomodoroSession session) {
        PomodoroSessionJpaEntity entity = new PomodoroSessionJpaEntity();
        entity.setId(session.getId());
        entity.setUserId(session.getUserId());
        entity.setTaskId(session.getTaskId());
        entity.setType(session.getType().name());
        entity.setStatus(session.getStatus().name());
        entity.setDurationMinutes(session.getDurationMinutes());
        entity.setStartedAt(session.getStartedAt());
        entity.setCompletedAt(session.getCompletedAt());
        entity.setCreatedAt(session.getCreatedAt());
        return entity;
    }
    
    public PomodoroSession toDomainEntity(PomodoroSessionJpaEntity entity) {
        PomodoroSession session = new PomodoroSession();
        session.setId(entity.getId());
        session.setUserId(entity.getUserId());
        session.setTaskId(entity.getTaskId());
        session.setType(SessionType.valueOf(entity.getType()));
        session.setStatus(SessionStatus.valueOf(entity.getStatus()));
        session.setDurationMinutes(entity.getDurationMinutes());
        session.setStartedAt(entity.getStartedAt());
        session.setCompletedAt(entity.getCompletedAt());
        session.setCreatedAt(entity.getCreatedAt());
        return session;
    }
}

package com.pomodoro.infrastructure.persistence.repository;

import com.pomodoro.domain.entity.PomodoroSession;
import com.pomodoro.domain.entity.PomodoroSession.SessionStatus;
import com.pomodoro.domain.repository.PomodoroSessionRepository;
import com.pomodoro.infrastructure.persistence.mapper.PomodoroSessionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of PomodoroSessionRepository using Spring Data JPA.
 */
@Repository
public class PomodoroSessionRepositoryImpl implements PomodoroSessionRepository {
    
    private final PomodoroSessionJpaRepository jpaRepository;
    private final PomodoroSessionMapper mapper;
    
    public PomodoroSessionRepositoryImpl(PomodoroSessionJpaRepository jpaRepository, 
                                          PomodoroSessionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public PomodoroSession save(PomodoroSession session) {
        var jpaEntity = mapper.toJpaEntity(session);
        var savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(savedEntity);
    }
    
    @Override
    public Optional<PomodoroSession> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public List<PomodoroSession> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
            .map(mapper::toDomainEntity)
            .toList();
    }
    
    @Override
    public List<PomodoroSession> findByTaskId(UUID taskId) {
        return jpaRepository.findByTaskId(taskId).stream()
            .map(mapper::toDomainEntity)
            .toList();
    }
    
    @Override
    public List<PomodoroSession> findByUserIdAndStatus(UUID userId, SessionStatus status) {
        return jpaRepository.findByUserIdAndStatus(userId, status.name()).stream()
            .map(mapper::toDomainEntity)
            .toList();
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}

package com.pomodoro.infrastructure.persistence.repository;

import com.pomodoro.infrastructure.persistence.entity.PomodoroSessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for Pomodoro Session entities.
 */
@Repository
public interface PomodoroSessionJpaRepository extends JpaRepository<PomodoroSessionJpaEntity, UUID> {
    
    List<PomodoroSessionJpaEntity> findByUserId(UUID userId);
    
    List<PomodoroSessionJpaEntity> findByTaskId(UUID taskId);
    
    List<PomodoroSessionJpaEntity> findByUserIdAndStatus(UUID userId, String status);
}

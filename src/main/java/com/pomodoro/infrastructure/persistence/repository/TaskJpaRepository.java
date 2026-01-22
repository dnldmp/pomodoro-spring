package com.pomodoro.infrastructure.persistence.repository;

import com.pomodoro.infrastructure.persistence.entity.TaskJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for Task entities.
 */
@Repository
public interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, UUID> {
    
    List<TaskJpaEntity> findByUserId(UUID userId);
    
    List<TaskJpaEntity> findByUserIdAndCompleted(UUID userId, boolean completed);
}

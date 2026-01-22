package com.pomodoro.domain.repository;

import com.pomodoro.domain.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Task entity following Clean Architecture principles.
 * This interface is part of the domain layer and is implemented by the infrastructure layer.
 */
public interface TaskRepository {
    
    Task save(Task task);
    
    Optional<Task> findById(UUID id);
    
    List<Task> findByUserId(UUID userId);
    
    List<Task> findByUserIdAndCompleted(UUID userId, boolean completed);
    
    void deleteById(UUID id);
    
    boolean existsById(UUID id);
}

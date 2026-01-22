package com.pomodoro.infrastructure.persistence.repository;

import com.pomodoro.domain.entity.Task;
import com.pomodoro.domain.repository.TaskRepository;
import com.pomodoro.infrastructure.persistence.mapper.TaskMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of TaskRepository using Spring Data JPA.
 * Follows the Dependency Inversion Principle by implementing the domain interface.
 */
@Repository
public class TaskRepositoryImpl implements TaskRepository {
    
    private final TaskJpaRepository jpaRepository;
    private final TaskMapper mapper;
    
    public TaskRepositoryImpl(TaskJpaRepository jpaRepository, TaskMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Task save(Task task) {
        var jpaEntity = mapper.toJpaEntity(task);
        var savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(savedEntity);
    }
    
    @Override
    public Optional<Task> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public List<Task> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
            .map(mapper::toDomainEntity)
            .toList();
    }
    
    @Override
    public List<Task> findByUserIdAndCompleted(UUID userId, boolean completed) {
        return jpaRepository.findByUserIdAndCompleted(userId, completed).stream()
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

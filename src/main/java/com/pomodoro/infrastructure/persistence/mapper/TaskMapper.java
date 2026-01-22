package com.pomodoro.infrastructure.persistence.mapper;

import com.pomodoro.domain.entity.Task;
import com.pomodoro.infrastructure.persistence.entity.TaskJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Task domain entity and JPA entity.
 */
@Component
public class TaskMapper {
    
    public TaskJpaEntity toJpaEntity(Task task) {
        TaskJpaEntity entity = new TaskJpaEntity();
        entity.setId(task.getId());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setCompleted(task.isCompleted());
        entity.setEstimatedPomodoros(task.getEstimatedPomodoros());
        entity.setCompletedPomodoros(task.getCompletedPomodoros());
        entity.setUserId(task.getUserId());
        entity.setCreatedAt(task.getCreatedAt());
        entity.setUpdatedAt(task.getUpdatedAt());
        return entity;
    }
    
    public Task toDomainEntity(TaskJpaEntity entity) {
        Task task = new Task();
        task.setId(entity.getId());
        task.setTitle(entity.getTitle());
        task.setDescription(entity.getDescription());
        task.setCompleted(entity.isCompleted());
        task.setEstimatedPomodoros(entity.getEstimatedPomodoros());
        task.setCompletedPomodoros(entity.getCompletedPomodoros());
        task.setUserId(entity.getUserId());
        task.setCreatedAt(entity.getCreatedAt());
        task.setUpdatedAt(entity.getUpdatedAt());
        return task;
    }
}

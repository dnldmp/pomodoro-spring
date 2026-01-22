package com.pomodoro.application.usecase;

import com.pomodoro.application.dto.CreateTaskRequest;
import com.pomodoro.application.dto.TaskResponse;
import com.pomodoro.application.dto.UpdateTaskRequest;
import com.pomodoro.domain.entity.Task;
import com.pomodoro.domain.exception.ResourceNotFoundException;
import com.pomodoro.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Use case for managing Tasks.
 * Implements the Single Responsibility Principle by handling only Task-related operations.
 */
@Service
@Transactional
public class TaskUseCase {
    
    private final TaskRepository taskRepository;
    
    public TaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task(
            request.title(),
            request.description(),
            request.estimatedPomodoros(),
            request.userId()
        );
        
        Task savedTask = taskRepository.save(task);
        return toResponse(savedTask);
    }
    
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        return toResponse(task);
    }
    
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByUserId(UUID userId) {
        return taskRepository.findByUserId(userId).stream()
            .map(this::toResponse)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public List<TaskResponse> getPendingTasks(UUID userId) {
        return taskRepository.findByUserIdAndCompleted(userId, false).stream()
            .map(this::toResponse)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public List<TaskResponse> getCompletedTasks(UUID userId) {
        return taskRepository.findByUserIdAndCompleted(userId, true).stream()
            .map(this::toResponse)
            .toList();
    }
    
    public TaskResponse updateTask(UUID id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        
        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.estimatedPomodoros() != null) {
            task.setEstimatedPomodoros(request.estimatedPomodoros());
        }
        
        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }
    
    public TaskResponse completeTask(UUID id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        
        task.complete();
        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }
    
    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }
        taskRepository.deleteById(id);
    }
    
    public TaskResponse incrementPomodoro(UUID id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        
        task.incrementCompletedPomodoros();
        Task updatedTask = taskRepository.save(task);
        return toResponse(updatedTask);
    }
    
    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.isCompleted(),
            task.getEstimatedPomodoros(),
            task.getCompletedPomodoros(),
            task.getProgress(),
            task.getUserId(),
            task.getCreatedAt(),
            task.getUpdatedAt()
        );
    }
}

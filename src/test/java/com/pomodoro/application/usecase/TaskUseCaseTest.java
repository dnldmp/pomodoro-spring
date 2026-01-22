package com.pomodoro.application.usecase;

import com.pomodoro.application.dto.CreateTaskRequest;
import com.pomodoro.application.dto.TaskResponse;
import com.pomodoro.application.dto.UpdateTaskRequest;
import com.pomodoro.domain.entity.Task;
import com.pomodoro.domain.exception.ResourceNotFoundException;
import com.pomodoro.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaskUseCase.
 */
@ExtendWith(MockitoExtension.class)
class TaskUseCaseTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @InjectMocks
    private TaskUseCase taskUseCase;
    
    private UUID userId;
    private UUID taskId;
    private Task task;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        taskId = UUID.randomUUID();
        task = new Task("Test Task", "Description", 4, userId);
        task.setId(taskId);
    }
    
    @Test
    void shouldCreateTask() {
        CreateTaskRequest request = new CreateTaskRequest("Test Task", "Description", 4, userId);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        TaskResponse response = taskUseCase.createTask(request);
        
        assertNotNull(response);
        assertEquals("Test Task", response.title());
        assertEquals("Description", response.description());
        assertEquals(4, response.estimatedPomodoros());
        verify(taskRepository).save(any(Task.class));
    }
    
    @Test
    void shouldGetTaskById() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        
        TaskResponse response = taskUseCase.getTaskById(taskId);
        
        assertNotNull(response);
        assertEquals("Test Task", response.title());
    }
    
    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> taskUseCase.getTaskById(taskId));
    }
    
    @Test
    void shouldGetTasksByUserId() {
        when(taskRepository.findByUserId(userId)).thenReturn(List.of(task));
        
        List<TaskResponse> responses = taskUseCase.getTasksByUserId(userId);
        
        assertEquals(1, responses.size());
        assertEquals("Test Task", responses.get(0).title());
    }
    
    @Test
    void shouldUpdateTask() {
        UpdateTaskRequest request = new UpdateTaskRequest("Updated Task", null, null);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        TaskResponse response = taskUseCase.updateTask(taskId, request);
        
        assertEquals("Updated Task", response.title());
    }
    
    @Test
    void shouldCompleteTask() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        TaskResponse response = taskUseCase.completeTask(taskId);
        
        assertTrue(response.completed());
    }
    
    @Test
    void shouldDeleteTask() {
        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);
        
        assertDoesNotThrow(() -> taskUseCase.deleteTask(taskId));
        verify(taskRepository).deleteById(taskId);
    }
    
    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        when(taskRepository.existsById(taskId)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> taskUseCase.deleteTask(taskId));
    }
    
    @Test
    void shouldIncrementPomodoro() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        TaskResponse response = taskUseCase.incrementPomodoro(taskId);
        
        assertEquals(1, response.completedPomodoros());
    }
}

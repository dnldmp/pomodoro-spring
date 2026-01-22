package com.pomodoro.presentation.controller;

import com.pomodoro.application.dto.CreateTaskRequest;
import com.pomodoro.application.dto.TaskResponse;
import com.pomodoro.application.dto.UpdateTaskRequest;
import com.pomodoro.application.usecase.TaskUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Task operations.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    private final TaskUseCase taskUseCase;
    
    public TaskController(TaskUseCase taskUseCase) {
        this.taskUseCase = taskUseCase;
    }
    
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse response = taskUseCase.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id) {
        TaskResponse response = taskUseCase.getTaskById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable UUID userId) {
        List<TaskResponse> response = taskUseCase.getTasksByUserId(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<List<TaskResponse>> getPendingTasks(@PathVariable UUID userId) {
        List<TaskResponse> response = taskUseCase.getPendingTasks(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}/completed")
    public ResponseEntity<List<TaskResponse>> getCompletedTasks(@PathVariable UUID userId) {
        List<TaskResponse> response = taskUseCase.getCompletedTasks(userId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @RequestBody UpdateTaskRequest request) {
        TaskResponse response = taskUseCase.updateTask(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable UUID id) {
        TaskResponse response = taskUseCase.completeTask(id);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskUseCase.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

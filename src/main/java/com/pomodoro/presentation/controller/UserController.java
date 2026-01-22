package com.pomodoro.presentation.controller;

import com.pomodoro.application.dto.CreateUserRequest;
import com.pomodoro.application.dto.UserResponse;
import com.pomodoro.application.usecase.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for User operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserUseCase userUseCase;
    
    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }
    
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        UserResponse response = userUseCase.getUserById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse response = userUseCase.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/add-pomodoro-xp")
    public ResponseEntity<UserResponse> addPomodoroXp(@PathVariable UUID id) {
        UserResponse response = userUseCase.addPomodoroXp(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/add-task-xp")
    public ResponseEntity<UserResponse> addTaskCompletionXp(@PathVariable UUID id) {
        UserResponse response = userUseCase.addTaskCompletionXp(id);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

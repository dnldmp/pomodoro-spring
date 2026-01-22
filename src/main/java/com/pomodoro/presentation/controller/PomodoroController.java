package com.pomodoro.presentation.controller;

import com.pomodoro.application.dto.PomodoroSessionResponse;
import com.pomodoro.application.dto.StartPomodoroRequest;
import com.pomodoro.application.usecase.PomodoroSessionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Pomodoro Session operations.
 */
@RestController
@RequestMapping("/api/pomodoro")
public class PomodoroController {
    
    private final PomodoroSessionUseCase sessionUseCase;
    
    public PomodoroController(PomodoroSessionUseCase sessionUseCase) {
        this.sessionUseCase = sessionUseCase;
    }
    
    @PostMapping("/start")
    public ResponseEntity<PomodoroSessionResponse> startSession(
            @Valid @RequestBody StartPomodoroRequest request) {
        PomodoroSessionResponse response = sessionUseCase.startSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/{id}/pause")
    public ResponseEntity<PomodoroSessionResponse> pauseSession(@PathVariable UUID id) {
        PomodoroSessionResponse response = sessionUseCase.pauseSession(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/resume")
    public ResponseEntity<PomodoroSessionResponse> resumeSession(@PathVariable UUID id) {
        PomodoroSessionResponse response = sessionUseCase.resumeSession(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<PomodoroSessionResponse> completeSession(@PathVariable UUID id) {
        PomodoroSessionResponse response = sessionUseCase.completeSession(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<PomodoroSessionResponse> cancelSession(@PathVariable UUID id) {
        PomodoroSessionResponse response = sessionUseCase.cancelSession(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PomodoroSessionResponse> getSession(@PathVariable UUID id) {
        PomodoroSessionResponse response = sessionUseCase.getSessionById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PomodoroSessionResponse>> getSessionsByUser(@PathVariable UUID userId) {
        List<PomodoroSessionResponse> response = sessionUseCase.getSessionsByUserId(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<PomodoroSessionResponse>> getSessionsByTask(@PathVariable UUID taskId) {
        List<PomodoroSessionResponse> response = sessionUseCase.getSessionsByTaskId(taskId);
        return ResponseEntity.ok(response);
    }
}

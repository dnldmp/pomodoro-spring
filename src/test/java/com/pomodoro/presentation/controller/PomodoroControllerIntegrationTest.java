package com.pomodoro.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomodoro.application.dto.CreateTaskRequest;
import com.pomodoro.application.dto.CreateUserRequest;
import com.pomodoro.application.dto.StartPomodoroRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Pomodoro Controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PomodoroControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private String createTestUser(String username) throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest(username, username + "@example.com");
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        return objectMapper.readTree(result.getResponse().getContentAsString())
            .get("id").asText();
    }
    
    private String createTestTask(String userId, String title) throws Exception {
        CreateTaskRequest taskRequest = new CreateTaskRequest(title, "Description", 4, UUID.fromString(userId));
        MvcResult result = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        return objectMapper.readTree(result.getResponse().getContentAsString())
            .get("id").asText();
    }
    
    @Test
    void shouldStartWorkSession() throws Exception {
        String userId = createTestUser("pomodorouser1");
        String taskId = createTestTask(userId, "Pomodoro Task 1");
        
        StartPomodoroRequest request = new StartPomodoroRequest(
            UUID.fromString(userId), UUID.fromString(taskId), "WORK");
        
        mockMvc.perform(post("/api/pomodoro/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.type").value("WORK"))
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.durationMinutes").value(25));
    }
    
    @Test
    void shouldStartBreakSession() throws Exception {
        String userId = createTestUser("breakuser");
        
        StartPomodoroRequest request = new StartPomodoroRequest(
            UUID.fromString(userId), null, "SHORT_BREAK");
        
        mockMvc.perform(post("/api/pomodoro/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.type").value("SHORT_BREAK"))
            .andExpect(jsonPath("$.durationMinutes").value(5));
    }
    
    @Test
    void shouldCompleteSessionAndAwardXp() throws Exception {
        String userId = createTestUser("completeuser2");
        String taskId = createTestTask(userId, "Complete Task 2");
        
        // Start a session
        StartPomodoroRequest request = new StartPomodoroRequest(
            UUID.fromString(userId), UUID.fromString(taskId), "WORK");
        
        MvcResult sessionResult = mockMvc.perform(post("/api/pomodoro/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String sessionId = objectMapper.readTree(sessionResult.getResponse().getContentAsString())
            .get("id").asText();
        
        // Complete the session
        mockMvc.perform(post("/api/pomodoro/" + sessionId + "/complete"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"));
        
        // Verify XP was awarded
        mockMvc.perform(get("/api/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.xp").value(10))
            .andExpect(jsonPath("$.totalPomodorosCompleted").value(1));
        
        // Verify task pomodoro count was incremented
        mockMvc.perform(get("/api/tasks/" + taskId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.completedPomodoros").value(1));
    }
    
    @Test
    void shouldPauseAndResumeSession() throws Exception {
        String userId = createTestUser("pauseuser");
        
        StartPomodoroRequest request = new StartPomodoroRequest(
            UUID.fromString(userId), null, "WORK");
        
        MvcResult sessionResult = mockMvc.perform(post("/api/pomodoro/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String sessionId = objectMapper.readTree(sessionResult.getResponse().getContentAsString())
            .get("id").asText();
        
        // Pause
        mockMvc.perform(post("/api/pomodoro/" + sessionId + "/pause"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("PAUSED"));
        
        // Resume
        mockMvc.perform(post("/api/pomodoro/" + sessionId + "/resume"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }
    
    @Test
    void shouldCancelSession() throws Exception {
        String userId = createTestUser("canceluser");
        
        StartPomodoroRequest request = new StartPomodoroRequest(
            UUID.fromString(userId), null, "WORK");
        
        MvcResult sessionResult = mockMvc.perform(post("/api/pomodoro/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String sessionId = objectMapper.readTree(sessionResult.getResponse().getContentAsString())
            .get("id").asText();
        
        mockMvc.perform(post("/api/pomodoro/" + sessionId + "/cancel"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
    
    @Test
    void shouldPreventMultipleActiveSessions() throws Exception {
        String userId = createTestUser("multiuser");
        
        StartPomodoroRequest request1 = new StartPomodoroRequest(
            UUID.fromString(userId), null, "WORK");
        
        mockMvc.perform(post("/api/pomodoro/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isCreated());
        
        // Try to start another session
        StartPomodoroRequest request2 = new StartPomodoroRequest(
            UUID.fromString(userId), null, "WORK");
        
        mockMvc.perform(post("/api/pomodoro/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
            .andExpect(status().isBadRequest());
    }
}

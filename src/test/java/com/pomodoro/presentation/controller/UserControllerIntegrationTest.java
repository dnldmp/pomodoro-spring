package com.pomodoro.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomodoro.application.dto.CreateUserRequest;
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
 * Integration tests for User Controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void shouldCreateAndRetrieveUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("newuser", "newuser@example.com");
        
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("newuser"))
            .andExpect(jsonPath("$.email").value("newuser@example.com"))
            .andExpect(jsonPath("$.xp").value(0))
            .andExpect(jsonPath("$.level").value(1))
            .andReturn();
        
        String userId = objectMapper.readTree(result.getResponse().getContentAsString())
            .get("id").asText();
        
        mockMvc.perform(get("/api/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newuser"));
    }
    
    @Test
    void shouldReturnNotFoundForNonExistentUser() throws Exception {
        UUID randomId = UUID.randomUUID();
        
        mockMvc.perform(get("/api/users/" + randomId))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void shouldAddPomodoroXp() throws Exception {
        CreateUserRequest request = new CreateUserRequest("xpuser", "xpuser@example.com");
        
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String userId = objectMapper.readTree(result.getResponse().getContentAsString())
            .get("id").asText();
        
        mockMvc.perform(post("/api/users/" + userId + "/add-pomodoro-xp"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.xp").value(10))
            .andExpect(jsonPath("$.totalPomodorosCompleted").value(1));
    }
    
    @Test
    void shouldAddTaskCompletionXp() throws Exception {
        CreateUserRequest request = new CreateUserRequest("taskxpuser", "taskxp@example.com");
        
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String userId = objectMapper.readTree(result.getResponse().getContentAsString())
            .get("id").asText();
        
        mockMvc.perform(post("/api/users/" + userId + "/add-task-xp"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.xp").value(25))
            .andExpect(jsonPath("$.totalTasksCompleted").value(1));
    }
    
    @Test
    void shouldLevelUpAfterMultiplePomodoros() throws Exception {
        CreateUserRequest request = new CreateUserRequest("leveluser", "level@example.com");
        
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String userId = objectMapper.readTree(result.getResponse().getContentAsString())
            .get("id").asText();
        
        // Add 10 pomodoros (10 XP each = 100 XP = Level 2)
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/api/users/" + userId + "/add-pomodoro-xp"))
                .andExpect(status().isOk());
        }
        
        mockMvc.perform(get("/api/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.xp").value(100))
            .andExpect(jsonPath("$.level").value(2));
    }
    
    @Test
    void shouldRejectDuplicateUsername() throws Exception {
        CreateUserRequest request1 = new CreateUserRequest("dupuser", "dup1@example.com");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isCreated());
        
        CreateUserRequest request2 = new CreateUserRequest("dupuser", "dup2@example.com");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
            .andExpect(status().isBadRequest());
    }
}

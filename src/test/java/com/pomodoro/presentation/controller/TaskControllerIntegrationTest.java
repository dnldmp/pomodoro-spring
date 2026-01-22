package com.pomodoro.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomodoro.application.dto.CreateTaskRequest;
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
 * Integration tests for Task Controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void shouldCreateAndRetrieveTask() throws Exception {
        // First create a user
        CreateUserRequest userRequest = new CreateUserRequest("taskuser", "taskuser@example.com");
        MvcResult userResult = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String userResponse = userResult.getResponse().getContentAsString();
        String userId = objectMapper.readTree(userResponse).get("id").asText();
        
        // Create a task
        CreateTaskRequest taskRequest = new CreateTaskRequest(
            "Test Task", "Test Description", 4, UUID.fromString(userId));
        
        MvcResult taskResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Test Task"))
            .andExpect(jsonPath("$.description").value("Test Description"))
            .andExpect(jsonPath("$.estimatedPomodoros").value(4))
            .andExpect(jsonPath("$.completed").value(false))
            .andReturn();
        
        String taskResponse = taskResult.getResponse().getContentAsString();
        String taskId = objectMapper.readTree(taskResponse).get("id").asText();
        
        // Retrieve the task
        mockMvc.perform(get("/api/tasks/" + taskId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Test Task"));
    }
    
    @Test
    void shouldReturnNotFoundForNonExistentTask() throws Exception {
        UUID randomId = UUID.randomUUID();
        
        mockMvc.perform(get("/api/tasks/" + randomId))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void shouldCompleteTask() throws Exception {
        // Create a user first
        CreateUserRequest userRequest = new CreateUserRequest("completeuser", "complete@example.com");
        MvcResult userResult = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String userId = objectMapper.readTree(userResult.getResponse().getContentAsString())
            .get("id").asText();
        
        // Create a task
        CreateTaskRequest taskRequest = new CreateTaskRequest(
            "Complete Task", "Description", 2, UUID.fromString(userId));
        
        MvcResult taskResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String taskId = objectMapper.readTree(taskResult.getResponse().getContentAsString())
            .get("id").asText();
        
        // Complete the task
        mockMvc.perform(post("/api/tasks/" + taskId + "/complete"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.completed").value(true));
    }
    
    @Test
    void shouldDeleteTask() throws Exception {
        // Create a user first
        CreateUserRequest userRequest = new CreateUserRequest("deleteuser", "delete@example.com");
        MvcResult userResult = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String userId = objectMapper.readTree(userResult.getResponse().getContentAsString())
            .get("id").asText();
        
        // Create a task
        CreateTaskRequest taskRequest = new CreateTaskRequest(
            "Delete Task", "Description", 1, UUID.fromString(userId));
        
        MvcResult taskResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        String taskId = objectMapper.readTree(taskResult.getResponse().getContentAsString())
            .get("id").asText();
        
        // Delete the task
        mockMvc.perform(delete("/api/tasks/" + taskId))
            .andExpect(status().isNoContent());
        
        // Verify it's deleted
        mockMvc.perform(get("/api/tasks/" + taskId))
            .andExpect(status().isNotFound());
    }
}

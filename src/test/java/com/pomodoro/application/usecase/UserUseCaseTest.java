package com.pomodoro.application.usecase;

import com.pomodoro.application.dto.CreateUserRequest;
import com.pomodoro.application.dto.UserResponse;
import com.pomodoro.domain.entity.User;
import com.pomodoro.domain.exception.BusinessException;
import com.pomodoro.domain.exception.ResourceNotFoundException;
import com.pomodoro.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserUseCase.
 */
@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserUseCase userUseCase;
    
    private UUID userId;
    private User user;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User("testuser", "test@example.com");
        user.setId(userId);
        
        ReflectionTestUtils.setField(userUseCase, "xpPerPomodoro", 10);
        ReflectionTestUtils.setField(userUseCase, "xpPerTaskCompleted", 25);
    }
    
    @Test
    void shouldCreateUser() {
        CreateUserRequest request = new CreateUserRequest("testuser", "test@example.com");
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        UserResponse response = userUseCase.createUser(request);
        
        assertNotNull(response);
        assertEquals("testuser", response.username());
        assertEquals("test@example.com", response.email());
        assertEquals(0, response.xp());
        assertEquals(1, response.level());
    }
    
    @Test
    void shouldThrowExceptionWhenUsernameExists() {
        CreateUserRequest request = new CreateUserRequest("testuser", "test@example.com");
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        
        assertThrows(BusinessException.class, () -> userUseCase.createUser(request));
    }
    
    @Test
    void shouldThrowExceptionWhenEmailExists() {
        CreateUserRequest request = new CreateUserRequest("testuser", "test@example.com");
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        
        assertThrows(BusinessException.class, () -> userUseCase.createUser(request));
    }
    
    @Test
    void shouldGetUserById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        UserResponse response = userUseCase.getUserById(userId);
        
        assertNotNull(response);
        assertEquals("testuser", response.username());
    }
    
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> userUseCase.getUserById(userId));
    }
    
    @Test
    void shouldAddPomodoroXp() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        UserResponse response = userUseCase.addPomodoroXp(userId);
        
        assertEquals(10, response.xp());
        assertEquals(1, response.totalPomodorosCompleted());
    }
    
    @Test
    void shouldAddTaskCompletionXp() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        UserResponse response = userUseCase.addTaskCompletionXp(userId);
        
        assertEquals(25, response.xp());
        assertEquals(1, response.totalTasksCompleted());
    }
    
    @Test
    void shouldDeleteUser() {
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);
        
        assertDoesNotThrow(() -> userUseCase.deleteUser(userId));
        verify(userRepository).deleteById(userId);
    }
}

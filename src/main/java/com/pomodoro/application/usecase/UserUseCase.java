package com.pomodoro.application.usecase;

import com.pomodoro.application.dto.CreateUserRequest;
import com.pomodoro.application.dto.RewardResponse;
import com.pomodoro.application.dto.UserResponse;
import com.pomodoro.domain.entity.Reward;
import com.pomodoro.domain.entity.User;
import com.pomodoro.domain.exception.BusinessException;
import com.pomodoro.domain.exception.ResourceNotFoundException;
import com.pomodoro.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Use case for managing Users.
 * Handles user creation, XP management, and rewards.
 */
@Service
@Transactional
public class UserUseCase {
    
    private final UserRepository userRepository;
    
    @Value("${pomodoro.xp-per-pomodoro:10}")
    private int xpPerPomodoro;
    
    @Value("${pomodoro.xp-per-task-completed:25}")
    private int xpPerTaskCompleted;
    
    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already exists");
        }
        
        User user = new User(request.username(), request.email());
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return toResponse(user);
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return toResponse(user);
    }
    
    public UserResponse addPomodoroXp(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.addXp(xpPerPomodoro);
        user.incrementPomodorosCompleted();
        
        User updatedUser = userRepository.save(user);
        return toResponse(updatedUser);
    }
    
    public UserResponse addTaskCompletionXp(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        user.addXp(xpPerTaskCompleted);
        user.incrementTasksCompleted();
        
        User updatedUser = userRepository.save(user);
        return toResponse(updatedUser);
    }
    
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }
    
    private UserResponse toResponse(User user) {
        List<RewardResponse> rewardResponses = user.getRewards().stream()
            .map(this::toRewardResponse)
            .toList();
        
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getXp(),
            user.getLevel(),
            user.getXpToNextLevel(),
            user.getTotalPomodorosCompleted(),
            user.getTotalTasksCompleted(),
            rewardResponses,
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    
    private RewardResponse toRewardResponse(Reward reward) {
        return new RewardResponse(
            reward.getId(),
            reward.getName(),
            reward.getDescription(),
            reward.getType().name(),
            reward.getRequiredXp(),
            reward.getIconUrl(),
            reward.getEarnedAt(),
            reward.isEarned()
        );
    }
}

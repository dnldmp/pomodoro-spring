package com.pomodoro.domain.repository;

import com.pomodoro.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity following Clean Architecture principles.
 */
public interface UserRepository {
    
    User save(User user);
    
    Optional<User> findById(UUID id);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    void deleteById(UUID id);
    
    boolean existsById(UUID id);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}

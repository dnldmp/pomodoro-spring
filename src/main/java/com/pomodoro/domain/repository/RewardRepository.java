package com.pomodoro.domain.repository;

import com.pomodoro.domain.entity.Reward;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Reward entity following Clean Architecture principles.
 */
public interface RewardRepository {
    
    Reward save(Reward reward);
    
    Optional<Reward> findById(UUID id);
    
    List<Reward> findAll();
    
    List<Reward> findByRequiredXpLessThanEqual(int xp);
    
    void deleteById(UUID id);
    
    boolean existsById(UUID id);
}

package com.pomodoro.infrastructure.persistence.repository;

import com.pomodoro.infrastructure.persistence.entity.RewardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for Reward entities.
 */
@Repository
public interface RewardJpaRepository extends JpaRepository<RewardJpaEntity, UUID> {
    
    List<RewardJpaEntity> findByRequiredXpLessThanEqual(int xp);
}

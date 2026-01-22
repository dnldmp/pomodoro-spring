package com.pomodoro.infrastructure.persistence.mapper;

import com.pomodoro.domain.entity.Reward;
import com.pomodoro.domain.entity.Reward.RewardType;
import com.pomodoro.infrastructure.persistence.entity.RewardJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Reward domain entity and JPA entity.
 */
@Component
public class RewardMapper {
    
    public RewardJpaEntity toJpaEntity(Reward reward) {
        RewardJpaEntity entity = new RewardJpaEntity();
        entity.setId(reward.getId());
        entity.setName(reward.getName());
        entity.setDescription(reward.getDescription());
        entity.setType(reward.getType().name());
        entity.setRequiredXp(reward.getRequiredXp());
        entity.setIconUrl(reward.getIconUrl());
        entity.setEarnedAt(reward.getEarnedAt());
        return entity;
    }
    
    public Reward toDomainEntity(RewardJpaEntity entity) {
        Reward reward = new Reward();
        reward.setId(entity.getId());
        reward.setName(entity.getName());
        reward.setDescription(entity.getDescription());
        reward.setType(RewardType.valueOf(entity.getType()));
        reward.setRequiredXp(entity.getRequiredXp());
        reward.setIconUrl(entity.getIconUrl());
        reward.setEarnedAt(entity.getEarnedAt());
        return reward;
    }
}

package com.pomodoro.infrastructure.persistence.mapper;

import com.pomodoro.domain.entity.User;
import com.pomodoro.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Mapper for converting between User domain entity and JPA entity.
 */
@Component
public class UserMapper {
    
    public UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setXp(user.getXp());
        entity.setLevel(user.getLevel());
        entity.setTotalPomodorosCompleted(user.getTotalPomodorosCompleted());
        entity.setTotalTasksCompleted(user.getTotalTasksCompleted());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }
    
    public User toDomainEntity(UserJpaEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setXp(entity.getXp());
        user.setLevel(entity.getLevel());
        user.setTotalPomodorosCompleted(entity.getTotalPomodorosCompleted());
        user.setTotalTasksCompleted(entity.getTotalTasksCompleted());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setRewards(new ArrayList<>());
        return user;
    }
}

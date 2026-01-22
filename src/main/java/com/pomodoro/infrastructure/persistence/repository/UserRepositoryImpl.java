package com.pomodoro.infrastructure.persistence.repository;

import com.pomodoro.domain.entity.User;
import com.pomodoro.domain.repository.UserRepository;
import com.pomodoro.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserRepository using Spring Data JPA.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;
    
    public UserRepositoryImpl(UserJpaRepository jpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public User save(User user) {
        var jpaEntity = mapper.toJpaEntity(user);
        var savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(savedEntity);
    }
    
    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}

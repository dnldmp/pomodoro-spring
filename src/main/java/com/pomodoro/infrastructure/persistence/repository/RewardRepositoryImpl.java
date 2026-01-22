package com.pomodoro.infrastructure.persistence.repository;

import com.pomodoro.domain.entity.Reward;
import com.pomodoro.domain.repository.RewardRepository;
import com.pomodoro.infrastructure.persistence.mapper.RewardMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of RewardRepository using Spring Data JPA.
 */
@Repository
public class RewardRepositoryImpl implements RewardRepository {
    
    private final RewardJpaRepository jpaRepository;
    private final RewardMapper mapper;
    
    public RewardRepositoryImpl(RewardJpaRepository jpaRepository, RewardMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Reward save(Reward reward) {
        var jpaEntity = mapper.toJpaEntity(reward);
        var savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(savedEntity);
    }
    
    @Override
    public Optional<Reward> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomainEntity);
    }
    
    @Override
    public List<Reward> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomainEntity)
            .toList();
    }
    
    @Override
    public List<Reward> findByRequiredXpLessThanEqual(int xp) {
        return jpaRepository.findByRequiredXpLessThanEqual(xp).stream()
            .map(mapper::toDomainEntity)
            .toList();
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}

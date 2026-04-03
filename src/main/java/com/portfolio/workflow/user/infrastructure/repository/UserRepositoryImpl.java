package com.portfolio.workflow.user.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.portfolio.workflow.user.domain.model.User;
import com.portfolio.workflow.user.domain.repository.UserRepository;
import com.portfolio.workflow.user.infrastructure.mapper.UserPersistenceMapper;

/**
 * Implementierung des fachlichen UserRepository.
 *
 * <p>
 * Diese Klasse verbindet Domain und JPA-Schicht und übernimmt das Mapping
 * zwischen Domain-Modell und Persistence-Entity.
 * </p>
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        return UserPersistenceMapper.toDomain(
                userJpaRepository.save(UserPersistenceMapper.toEntity(user))
        );
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return userJpaRepository.existsById(id);
    }
}
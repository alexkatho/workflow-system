package com.portfolio.workflow.user.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portfolio.workflow.user.domain.model.User;

/**
 * Fachliches Repository-Interface für Benutzer.
 *
 * <p>
 * Dieses Interface gehört zur Domain-Schicht und kennt keine technischen
 * Persistence-Details wie JPA oder Entities.
 * </p>
 */
public interface UserRepository {

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findAll();

    User save(User user);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
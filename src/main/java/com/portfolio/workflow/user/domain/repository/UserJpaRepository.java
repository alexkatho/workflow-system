package com.portfolio.workflow.user.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.workflow.user.infrastructure.persistence.UserEntity;

/**
 * Spring Data JPA Repository für UserEntity.
 * <p>
 * Bietet CRUD-Methoden automatisch, inklusive findById, save, delete.
 * </p>
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);
}
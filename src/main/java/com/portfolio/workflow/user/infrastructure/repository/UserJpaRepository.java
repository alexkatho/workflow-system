package com.portfolio.workflow.user.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.workflow.user.infrastructure.persistence.UserEntity;
/**
 * Spring Data JPA Repository für UserEntity.
 */
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Sucht einen Benutzer anhand seiner E-Mail-Adresse.
     *
     * @param email E-Mail-Adresse
     * @return optionaler Benutzer
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Prüft, ob bereits ein Benutzer mit der E-Mail existiert.
     *
     * @param email E-Mail-Adresse
     * @return true, wenn ein Benutzer mit dieser E-Mail existiert
     */
    boolean existsByEmail(String email);

    /**
     * Prüft, ob bereits ein Benutzer mit dem Username existiert.
     *
     * @param username Benutzername
     * @return true, wenn ein Benutzer mit diesem Username existiert
     */
    boolean existsByUsername(String username);
    
}
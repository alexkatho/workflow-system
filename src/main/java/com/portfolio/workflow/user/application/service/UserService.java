package com.portfolio.workflow.user.application.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.workflow.user.application.exception.DuplicateEmailException;
import com.portfolio.workflow.user.application.exception.DuplicateUsernameException;
import com.portfolio.workflow.user.application.exception.UserNotFoundException;
import com.portfolio.workflow.user.domain.model.AccountStatus;
import com.portfolio.workflow.user.domain.model.Permission;
import com.portfolio.workflow.user.domain.model.Role;
import com.portfolio.workflow.user.domain.model.User;
import com.portfolio.workflow.user.domain.repository.UserJpaRepository;
import com.portfolio.workflow.user.infrastructure.mapper.UserPersistenceMapper;
import com.portfolio.workflow.user.infrastructure.persistence.UserEntity;

/**
 * Service für Benutzerverwaltung.
 *
 * <p>
 * Verantwortlich für Create, Update, Find und Delete.
 * Security-relevante Logik wie Passwort-Hashing wird ebenfalls hier behandelt.
 * </p>
 */
@Service
public class UserService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Erstellt einen neuen Benutzer.
     *
     * @param username Benutzername
     * @param email E-Mail-Adresse
     * @param rawPassword Klartext-Passwort
     * @param role Rolle des Benutzers
     * @return erstellter Benutzer
     * @throws DuplicateEmailException wenn die E-Mail bereits vergeben ist
     * @throws DuplicateUsernameException wenn der Username bereits vergeben ist
     */
    public User createUser(String username, String email, String rawPassword, Role role) {

        validateUniqueEmail(email);
        validateUniqueUsername(username);

        String hashed = passwordEncoder.encode(rawPassword);

        Set<Permission> permissions = derivePermissions(role);

        User userDomain = new User(
                username,
                email,
                hashed,
                role,
                permissions,
                AccountStatus.ACTIVE
        );

        UserEntity savedEntity = userRepository.save(UserPersistenceMapper.toEntity(userDomain));
        return UserPersistenceMapper.toDomain(savedEntity);
    }

    /**
     * Aktualisiert einen bestehenden Benutzer.
     *
     * @param userId ID des Benutzers
     * @param newRole neue Rolle, optional
     * @param status neuer Status, optional
     * @param newRawPassword neues Passwort, optional
     * @param newEmail neue E-Mail, optional
     * @param newUsername neuer Username, optional
     * @return aktualisierter Benutzer
     */
    public User updateUser(UUID userId,
                           Role newRole,
                           AccountStatus status,
                           String newRawPassword,
                           String newEmail,
                           String newUsername) {

        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (newEmail != null && !newEmail.equalsIgnoreCase(entity.getEmail())) {
            validateUniqueEmail(newEmail);
            entity.setEmail(newEmail);
        }

        if (newUsername != null && !newUsername.equalsIgnoreCase(entity.getUsername())) {
            validateUniqueUsername(newUsername);
            entity.setUsername(newUsername);
        }

        if (newRole != null) {
            entity.setRole(newRole);
        }

        if (status != null) {
            entity.setStatus(status);
        }

        if (newRawPassword != null && !newRawPassword.isBlank()) {
            entity.setPasswordHash(passwordEncoder.encode(newRawPassword));
        }

        UserEntity updatedEntity = userRepository.save(entity);
        return UserPersistenceMapper.toDomain(updatedEntity);
    }

    /**
     * Sucht einen Benutzer anhand seiner E-Mail-Adresse.
     *
     * @param email E-Mail-Adresse
     * @return optionaler Benutzer
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserPersistenceMapper::toDomain);
    }

    /**
     * Sucht einen Benutzer anhand seiner ID.
     *
     * @param userId Benutzer-ID
     * @return optionaler Benutzer
     */
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId)
                .map(UserPersistenceMapper::toDomain);
    }

    /**
     * Löscht einen Benutzer anhand seiner ID.
     *
     * @param userId Benutzer-ID
     */
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
    }

    /**
     * Leitet Berechtigungen aus der Rolle ab.
     *
     * @param role Rolle des Benutzers
     * @return Menge an Berechtigungen
     */
    private Set<Permission> derivePermissions(Role role) {
        return switch (role) {
            case USER -> Set.of(Permission.CREATE_REQUEST);
            case MANAGER -> Set.of(
                    Permission.CREATE_REQUEST,
                    Permission.APPROVE_REQUEST
            );
            case ADMIN -> Set.of(
                    Permission.CREATE_REQUEST,
                    Permission.APPROVE_REQUEST,
                    Permission.MANAGE_USERS,
                    Permission.VIEW_AUDIT_LOG
            );
        };
    }

    /**
     * Prüft, ob die E-Mail-Adresse bereits vergeben ist.
     *
     * @param email E-Mail-Adresse
     * @throws DuplicateEmailException wenn die E-Mail bereits existiert
     */
    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
    }

    /**
     * Prüft, ob der Username bereits vergeben ist.
     *
     * @param username Benutzername
     * @throws DuplicateUsernameException wenn der Username bereits existiert
     */
    private void validateUniqueUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }
    }
}
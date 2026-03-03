package com.portfolio.workflow.user.application.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.workflow.user.domain.model.AccountStatus;
import com.portfolio.workflow.user.domain.model.Permission;
import com.portfolio.workflow.user.domain.model.Role;
import com.portfolio.workflow.user.domain.model.User;
import com.portfolio.workflow.user.domain.repository.UserJpaRepository;
import com.portfolio.workflow.user.infrastructure.mapper.UserPersistenceMapper;
import com.portfolio.workflow.user.infrastructure.persistence.UserEntity;

/**
 * Service für Benutzerverwaltung.
 * <p>
 * Verantwortlich für Create, Update, Find. Password-Hashing & Status-Logik
 * bleiben im Service. Mapper bleibt „dumm“.
 * </p>
 */
@Service
public class UserService {

	private final UserJpaRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserService(UserJpaRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder; 
	}

	/**
	 * Erstellt einen neuen User, inklusive Passwort Hash und DEFAULT Status ACTIVE
	 */
	public User createUser(String username, String email, String rawPassword, Role role) {
		String hashed = passwordEncoder.encode(rawPassword);

		// Permissions aus Role ableiten
		Set<Permission> perms = switch (role) {
		case USER -> Set.of(Permission.CREATE_REQUEST);
		case MANAGER -> Set.of(Permission.CREATE_REQUEST, Permission.APPROVE_REQUEST);
		case ADMIN -> Set.of(Permission.CREATE_REQUEST, Permission.APPROVE_REQUEST, Permission.MANAGE_USERS,
				Permission.VIEW_AUDIT_LOG);
		};

		User userDomain = new User(username, email, hashed, role, perms, AccountStatus.ACTIVE);
		UserEntity entity = UserPersistenceMapper.toEntity(userDomain);
		UserEntity saved = userRepository.save(entity);
		return UserPersistenceMapper.toDomain(saved);
	}

	/** Aktualisiert bestehenden User: Rolle, Status, Passwortänderung */
	public User updateUser(UUID userId, Role newRole, AccountStatus status, String newRawPassword) {
		UserEntity entity = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User nicht gefunden"));

		if (newRole != null)
			entity.setRole(newRole);
		if (status != null)
			entity.setStatus(status);
		if (newRawPassword != null && !newRawPassword.isBlank()) {
			entity.setPasswordHash(passwordEncoder.encode(newRawPassword));
		}

		UserEntity updated = userRepository.save(entity);
		return UserPersistenceMapper.toDomain(updated);
	}

	/** Findet User nach Email */
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email).map(UserPersistenceMapper::toDomain);
	}

	/** Findet User nach ID */
	public Optional<User> findById(UUID userId) {
		return userRepository.findById(userId).map(UserPersistenceMapper::toDomain);
	}

	/** Löscht User */
	public void deleteUser(UUID userId) {
		userRepository.deleteById(userId);
	}
}
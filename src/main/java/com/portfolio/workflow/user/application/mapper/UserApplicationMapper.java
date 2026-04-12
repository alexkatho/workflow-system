package com.portfolio.workflow.user.application.mapper;

import java.util.Set;

import com.portfolio.workflow.user.application.dto.CreateUserRequestDto;
import com.portfolio.workflow.user.application.dto.UpdateUserRequestDto;
import com.portfolio.workflow.user.application.dto.UserResponseDto;
import com.portfolio.workflow.user.domain.model.AccountStatus;
import com.portfolio.workflow.user.domain.model.Permission;
import com.portfolio.workflow.user.domain.model.Role;
import com.portfolio.workflow.user.domain.model.User;

/**
 * Mapper zwischen Application Layer DTOs und Domain User.
 * <p>
 * Konvertiert Create/Update DTOs in Domain-Objekte und Domain → Response DTO.
 * </p>
 */
public class UserApplicationMapper {

	/** CreateUserRequestDto → Domain User (ohne ID, Passwort noch raw) */
	public static User toDomain(CreateUserRequestDto dto, String hashedPassword) {
		Set<Permission> perms = switch (dto.role()) {
		case USER -> Set.of(Permission.CREATE_REQUEST);
		case MANAGER -> Set.of(Permission.CREATE_REQUEST, Permission.APPROVE_REQUEST);
		case ADMIN -> Set.of(Permission.CREATE_REQUEST, Permission.APPROVE_REQUEST, Permission.MANAGE_USERS,
				Permission.VIEW_AUDIT_LOG);
		};
		return new User(dto.username(), dto.email(), hashedPassword, dto.role(), perms,
				AccountStatus.ACTIVE);
	}

	/**
	 * UpdateUserRequestDto → Domain User (Teilupdate möglich, Passwort optional)
	 */
	public static User updateDomain(User existingUser, UpdateUserRequestDto dto, String hashedPassword) {
		return new User(existingUser.getId(), dto.username() != null ? dto.username() : existingUser.getUsername(),
				dto.email() != null ? dto.email() : existingUser.getEmail(),
				hashedPassword != null ? hashedPassword : existingUser.getPasswordHash(),
				dto.role() != null ? dto.role() : existingUser.getRole(), existingUser.getPermissions(), 
				dto.status() != null ? dto.status() : existingUser.getStatus());
	}

	/** Domain User → UserResponseDto */
	public static UserResponseDto toResponseDto(User user) {
		return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
				user.getStatus());
	}
}
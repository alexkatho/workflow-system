package com.portfolio.workflow.user.application.dto;

import java.util.UUID;

import com.portfolio.workflow.user.domain.model.AccountStatus;
import com.portfolio.workflow.user.domain.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO für die Rückgabe von User-Daten an den Client.
 * <p>
 * Password wird niemals zurückgegeben!
 * </p>
 */
public record UserResponseDto(

		@Schema(description = "Eindeutige Benutzer-ID", example = "424878a9-a6c8-4b12-81c2-f0acbeb74d77") UUID id,

		@Schema(description = "Benutzername", example = "admin") String username,

		@Schema(description = "E-Mail-Adresse", example = "admin@workflow.local") String email,

		@Schema(description = "Rolle des Benutzers", example = "ADMIN") Role role,

		@Schema(description = "Account Status", example = "ACTIVE") AccountStatus status

) {
}
package com.portfolio.workflow.user.application.dto;

import com.portfolio.workflow.user.domain.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO für die Erstellung eines neuen Users.
 * <p>
 * Wird vom Controller als Request Body verwendet. Bean Validation stellt
 * sicher, dass ungültige Daten frühzeitig abgelehnt werden.
 * </p>
 */
public record CreateUserRequestDto(
		@Schema(description = "Username", example = "user") @NotBlank(message = "Username darf nicht leer sein") @Size(min = 3, max = 50, message = "Username muss zwischen 3 und 50 Zeichen sein") String username,
		@Schema(description = "E-Mail", example = "user@workflow.local") @Email(message = "Email muss gültig sein") @NotBlank(message = "Email darf nicht leer sein") String email,
		@Schema(description = "Passwort (min. 8 Zeichen)", example = "securePassword123") @NotBlank(message = "Passwort darf nicht leer sein") @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen haben") String password,
		@Schema(description = "Rolle", example = "USER") @NotNull(message = "Rolle muss angegeben werden") Role role) {
}
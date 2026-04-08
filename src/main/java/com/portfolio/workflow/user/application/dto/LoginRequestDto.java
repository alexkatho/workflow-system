package com.portfolio.workflow.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO für die Authentifizierung eines Benutzers.
 *
 * @param email eindeutige Login-E-Mail des Benutzers
 * @param password Klartext-Passwort, das serverseitig geprüft wird
 */
public record LoginRequestDto(
		
		@Schema(
                description = "E-Mail-Adresse des Benutzers",
                example = "admin@workflow.local"
        )
        @NotBlank(message = "Email darf nicht leer sein")
        @Email(message = "Email muss gültig sein")
        String email,

        @Schema(
                description = "Klartext-Passwort des Benutzers",
                example = "admin123"
        )
        @NotBlank(message = "Passwort darf nicht leer sein")
        String password
) {
} 
package com.portfolio.workflow.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO für die Authentifizierung eines Benutzers.
 *
 * @param email eindeutige Login-E-Mail des Benutzers
 * @param password Klartext-Passwort, das serverseitig geprüft wird
 */
public record LoginRequestDto(

        @NotBlank(message = "Email darf nicht leer sein")
        @Email(message = "Email muss gültig sein")
        String email,

        @NotBlank(message = "Passwort darf nicht leer sein")
        String password
) {
} 
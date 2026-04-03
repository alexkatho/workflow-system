package com.portfolio.workflow.user.application.dto;
import com.portfolio.workflow.user.domain.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO für die Erstellung eines neuen Users.
 * <p>
 * Wird vom Controller als Request Body verwendet.
 * Bean Validation stellt sicher, dass ungültige Daten frühzeitig abgelehnt werden.
 * </p>
 */
public record CreateUserRequestDto(
        @NotBlank(message = "Username darf nicht leer sein")
        @Size(min = 3, max = 50, message = "Username muss zwischen 3 und 50 Zeichen sein")
        String username,

        @Email(message = "Email muss gültig sein")
        @NotBlank(message = "Email darf nicht leer sein")
        String email,

        @NotBlank(message = "Passwort darf nicht leer sein")
        @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen haben")
        String password,

        @NotNull(message = "Rolle muss angegeben werden")
        Role role
) {}
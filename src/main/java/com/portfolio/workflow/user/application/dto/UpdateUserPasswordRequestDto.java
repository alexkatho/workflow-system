package com.portfolio.workflow.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO für das Zurücksetzen oder Ändern eines Benutzerpassworts.
 */
public record UpdateUserPasswordRequestDto(

        @NotBlank(message = "Passwort darf nicht leer sein")
        @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen haben")
        String password

) {
}
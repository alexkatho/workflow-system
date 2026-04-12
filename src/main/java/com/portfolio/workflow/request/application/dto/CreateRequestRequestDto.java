package com.portfolio.workflow.request.application.dto;

import com.portfolio.workflow.request.domain.model.RequestType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO für das Erstellen eines neuen fachlichen Requests.
 */
public record CreateRequestRequestDto(

        @Schema(
                description = "Kurzer Titel des Requests",
                example = "Laptop für neuen Mitarbeiter"
        )
        @NotBlank(message = "Titel darf nicht leer sein")
        @Size(max = 150, message = "Titel darf maximal 150 Zeichen lang sein")
        String title,

        @Schema(
                description = "Detaillierte Beschreibung des Requests",
                example = "Es wird ein Standard-Notebook für den neuen Mitarbeiter im Customer Support benötigt."
        )
        @NotBlank(message = "Beschreibung darf nicht leer sein")
        String description,

        @Schema(
                description = "Fachlicher Typ des Requests",
                example = "HARDWARE_REQUEST"
        )
        @NotNull(message = "Request-Typ muss angegeben werden")
        RequestType type
) {
}
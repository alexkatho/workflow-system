package com.portfolio.workflow.request.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.portfolio.workflow.request.domain.model.RequestStatus;
import com.portfolio.workflow.request.domain.model.RequestType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO für fachliche Requests.
 */
public record RequestResponseDto(

        @Schema(
                description = "Eindeutige Request-ID",
                example = "f0f6d3e3-6c89-4b2b-8df3-b0b43e1eb111"
        )
        UUID id,

        @Schema(
                description = "Titel des Requests",
                example = "Laptop für neuen Mitarbeiter"
        )
        String title,

        @Schema(
                description = "Beschreibung des Requests",
                example = "Es wird ein Standard-Notebook für den neuen Mitarbeiter im Customer Support benötigt."
        )
        String description,

        @Schema(
                description = "Fachlicher Typ des Requests",
                example = "HARDWARE_REQUEST"
        )
        RequestType type,

        @Schema(
                description = "Aktueller Status des Requests",
                example = "PENDING"
        )
        RequestStatus status,

        @Schema(
                description = "ID des antragstellenden Benutzers",
                example = "424878a9-a6c8-4b12-81c2-f0acbeb74d77"
        )
        UUID createdBy,

        @Schema(
                description = "ID des entscheidenden Benutzers",
                example = "1c9b56de-72fd-4c80-a3f2-b802d82f8901",
                nullable = true
        )
        UUID decidedBy,

        @Schema(
                description = "Optionaler Entscheidungskommentar",
                example = "Budget wurde freigegeben."
        )
        String decisionComment,

        @Schema(
                description = "Zeitpunkt der Entscheidung",
                example = "2026-04-12T11:30:00"
        )
        LocalDateTime decidedAt,

        @Schema(
                description = "Erstellungszeitpunkt",
                example = "2026-04-12T10:15:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "Zeitpunkt der letzten Aktualisierung",
                example = "2026-04-12T10:15:00"
        )
        LocalDateTime updatedAt
) {
}
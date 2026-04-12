package com.portfolio.workflow.request.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO für die Ablehnung eines Requests.
 */
public record RejectRequestDto(

        @Schema(
                description = "Begründung für die Ablehnung",
                example = "Budget für diesen Monat ist bereits ausgeschöpft."
        )
        @NotBlank(message = "Ablehnungskommentar darf nicht leer sein")
        @Size(max = 500, message = "Kommentar darf maximal 500 Zeichen lang sein")
        String decisionComment
) {
}
package com.portfolio.workflow.request.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Request DTO für die Genehmigung eines Requests.
 */
public record ApproveRequestDto(

        @Schema(
                description = "Optionaler Kommentar zur Genehmigung",
                example = "Hardware-Budget wurde freigegeben."
        )
        @Size(max = 500, message = "Kommentar darf maximal 500 Zeichen lang sein")
        String decisionComment
) {
}
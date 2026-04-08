package com.portfolio.workflow.user.application.dto;

import com.portfolio.workflow.user.domain.model.AccountStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO für die Änderung des Benutzerstatus.
 */
public record UpdateUserStatusRequestDto(
		
		@Schema(
                description = "Neuer Status des Benutzers",
                example = "DISABLED"
        )
        @NotNull(message = "Status muss angegeben werden")
        AccountStatus status

) {
}
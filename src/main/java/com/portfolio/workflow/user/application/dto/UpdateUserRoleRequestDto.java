package com.portfolio.workflow.user.application.dto;

import com.portfolio.workflow.user.domain.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO für die Änderung der Rolle.
 */
public record UpdateUserRoleRequestDto(
		@Schema(
                description = "Neue Rolle",
                example = "MANAGER"
        )
		@NotNull(message = "Rolle muss angegeben werden") Role role

) {
}
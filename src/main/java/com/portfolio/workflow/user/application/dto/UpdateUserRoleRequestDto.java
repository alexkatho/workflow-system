package com.portfolio.workflow.user.application.dto;

import com.portfolio.workflow.user.domain.model.Role;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO für die Änderung der Rolle.
 */
public record UpdateUserRoleRequestDto(

		@NotNull(message = "Rolle muss angegeben werden") Role role

) {
}
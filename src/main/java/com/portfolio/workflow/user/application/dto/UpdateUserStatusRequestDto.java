package com.portfolio.workflow.user.application.dto;

import com.portfolio.workflow.user.domain.model.AccountStatus;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO für die Änderung des Benutzerstatus.
 */
public record UpdateUserStatusRequestDto(

        @NotNull(message = "Status muss angegeben werden")
        AccountStatus status

) {
}
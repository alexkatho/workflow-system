package com.portfolio.workflow.user.application.dto;

import java.util.UUID;

/**
 * DTO für die Rückgabe von User-Daten an den Client.
 * <p>
 * Password wird niemals zurückgegeben!
 * </p>
 */
public record UserResponseDto(
        UUID id,
        String username,
        String email,
        String role,
        String status
) {}
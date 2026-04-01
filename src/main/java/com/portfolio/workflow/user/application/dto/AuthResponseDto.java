package com.portfolio.workflow.user.application.dto;

/**
 * Response DTO nach erfolgreicher Authentifizierung.
 *
 * @param token JWT Access Token
 */
public record AuthResponseDto(
        String token
) {
}
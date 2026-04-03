package com.portfolio.workflow.user.infrastructure.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Immutable JWT Konfiguration.
 */
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secret,
        long expiration
) {
}
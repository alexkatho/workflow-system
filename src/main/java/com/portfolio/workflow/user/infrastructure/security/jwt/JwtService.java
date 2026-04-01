package com.portfolio.workflow.user.infrastructure.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Service für Erstellung und Validierung von JWT Tokens.
 *
 * <p>
 * Verantwortlich für:
 * - Token generieren
 * - Token validieren
 * - Claims extrahieren
 * </p>
 */
@Service
public class JwtService {

    // 🔐 Secret Key (später in application.yml auslagern!)
	private static final String SECRET =
	        "my-super-secret-key-my-super-secret-key";

    private final long EXPIRATION = 1000 * 60 * 60; // 1 Stunde

    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }
    /**
     * Generiert ein JWT Token für einen User
     */
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .subject(email) // "sub"
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrahiert Username (Email) aus Token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrahiert Rolle aus Token
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /**
     * Validiert Token (Signatur + Ablauf)
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token); // wirft Exception wenn ungültig
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Parsed alle Claims
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
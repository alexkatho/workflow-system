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
 */
@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Generiert ein JWT für einen Benutzer.
     *
     * @param email E-Mail des Benutzers
     * @param role Rolle des Benutzers
     * @return signiertes JWT
     */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.expiration()))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrahiert den Benutzernamen bzw. die E-Mail aus dem JWT.
     *
     * @param token JWT
     * @return Subject des Tokens
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrahiert die Rolle aus dem JWT.
     *
     * @param token JWT
     * @return Rolle als String
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /**
     * Prüft, ob ein JWT gültig ist.
     *
     * @param token JWT
     * @return true, wenn Signatur und Ablaufzeit gültig sind
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Liest alle Claims aus einem signierten JWT.
     *
     * @param token JWT
     * @return Claims des Tokens
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Erstellt den HMAC Signing Key aus der konfigurierten Secret-Property.
     *
     * @return SecretKey für JWT Signatur und Verifikation
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }
}
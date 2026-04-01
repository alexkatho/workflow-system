package com.portfolio.workflow.user.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.workflow.user.application.dto.AuthResponseDto;
import com.portfolio.workflow.user.application.dto.LoginRequestDto;
import com.portfolio.workflow.user.domain.repository.UserJpaRepository;
import com.portfolio.workflow.user.infrastructure.security.jwt.JwtService;

/**
 * Service für Authentifizierung und JWT-Erzeugung.
 *
 * <p>
 * Verantwortlich für:
 * <ul>
 *   <li>Benutzer anhand der Email laden</li>
 *   <li>Passwort gegen den gespeicherten Hash prüfen</li>
 *   <li>JWT Token erzeugen</li>
 * </ul>
 * </p>
 */
@Service
public class AuthService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserJpaRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Authentifiziert einen Benutzer mit Email und Passwort.
     *
     * @param request Login-Daten
     * @return JWT Token als Response DTO
     * @throws RuntimeException wenn Benutzer nicht existiert oder Passwort ungültig ist
     */
    public AuthResponseDto login(LoginRequestDto request) {

        var userEntity = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Ungültige Email oder Passwort"));

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                userEntity.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Ungültige Email oder Passwort");
        }

        if (!userEntity.getStatus().isActive()) {
            throw new RuntimeException("Benutzerkonto ist nicht aktiv");
        }

        String token = jwtService.generateToken(
                userEntity.getEmail(),
                userEntity.getRole().name()
        );

        return new AuthResponseDto(token);
    }
}
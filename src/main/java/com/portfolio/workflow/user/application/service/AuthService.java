package com.portfolio.workflow.user.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.workflow.user.application.dto.AuthResponseDto;
import com.portfolio.workflow.user.application.dto.LoginRequestDto;
import com.portfolio.workflow.user.application.exception.InactiveUserException;
import com.portfolio.workflow.user.application.exception.InvalidCredentialsException;
import com.portfolio.workflow.user.domain.repository.UserRepository;
import com.portfolio.workflow.user.infrastructure.security.jwt.JwtService;

/**
 * Service für Authentifizierung und JWT-Erzeugung.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Authentifiziert einen Benutzer und erzeugt ein JWT.
     *
     * @param request Login-Request mit Email und Passwort
     * @return JWT Response
     */
    public AuthResponseDto login(LoginRequestDto request) {

        var userEntity = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                userEntity.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new InvalidCredentialsException();
        }

        if (!userEntity.getStatus().isActive()) {
            throw new InactiveUserException(userEntity.getEmail());
        }

        String token = jwtService.generateToken(
                userEntity.getEmail(),
                userEntity.getRole().name()
        );

        return new AuthResponseDto(token);
    }
}
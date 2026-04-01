package com.portfolio.workflow.user.presentation;

import com.portfolio.workflow.user.application.dto.AuthResponseDto;
import com.portfolio.workflow.user.application.dto.LoginRequestDto;
import com.portfolio.workflow.user.application.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller für Authentifizierung.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authentifiziert einen Benutzer und gibt ein JWT zurück.
     *
     * @param request Login Request mit Email und Passwort
     * @return JWT Token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
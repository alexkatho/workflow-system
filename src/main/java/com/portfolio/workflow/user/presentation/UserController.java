package com.portfolio.workflow.user.presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.workflow.user.application.dto.CreateUserRequestDto;
import com.portfolio.workflow.user.application.dto.UpdateUserPasswordRequestDto;
import com.portfolio.workflow.user.application.dto.UpdateUserRequestDto;
import com.portfolio.workflow.user.application.dto.UpdateUserRoleRequestDto;
import com.portfolio.workflow.user.application.dto.UpdateUserStatusRequestDto;
import com.portfolio.workflow.user.application.dto.UserResponseDto;
import com.portfolio.workflow.user.application.mapper.UserApplicationMapper;
import com.portfolio.workflow.user.application.service.UserService;
import com.portfolio.workflow.user.domain.model.User;

import jakarta.validation.Valid;

/**
 * REST Controller für Benutzerverwaltung.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto dto) {
        User user = userService.createUser(
                dto.username(),
                dto.email(),
                dto.password(),
                dto.role()
        );
        return ResponseEntity.ok(UserApplicationMapper.toResponseDto(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id,
                                                      @Valid @RequestBody UpdateUserRequestDto dto) {
        User updatedUser = userService.updateUser(
                id,
                dto.role(),
                dto.status(),
                dto.password(),
                dto.email(),
                dto.username()
        );

        return ResponseEntity.ok(UserApplicationMapper.toResponseDto(updatedUser));
    }
    
    /**
     * Aktualisiert den Status eines Benutzers (nur für ADMIN).
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUserStatus(@PathVariable UUID id,
                                                            @Valid @RequestBody UpdateUserStatusRequestDto dto) {
        User updatedUser = userService.updateUserStatus(id, dto.status());
        return ResponseEntity.ok(UserApplicationMapper.toResponseDto(updatedUser));
    }
    
    /**
     * Aktualisiert das Passwort eines Benutzers (nur für ADMIN).
     */
    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUserPassword(@PathVariable UUID id,
                                                              @Valid @RequestBody UpdateUserPasswordRequestDto dto) {
        User updatedUser = userService.updateUserPassword(id, dto.password());
        return ResponseEntity.ok(UserApplicationMapper.toResponseDto(updatedUser));
    }
    
    /**
     * Aktualisiert die Rolle eines Benutzers (nur für ADMIN).
     */
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUserRole(@PathVariable UUID id,
                                                          @Valid @RequestBody UpdateUserRoleRequestDto dto) {
        User updatedUser = userService.updateUserRole(id, dto.role());
        return ResponseEntity.ok(UserApplicationMapper.toResponseDto(updatedUser));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        return userService.findById(id)
                .map(UserApplicationMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Gibt alle User zurück (nur für ADMIN).
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserApplicationMapper::toResponseDto)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(UserApplicationMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
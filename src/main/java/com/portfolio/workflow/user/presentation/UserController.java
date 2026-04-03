package com.portfolio.workflow.user.presentation;

import com.portfolio.workflow.user.application.dto.CreateUserRequestDto;
import com.portfolio.workflow.user.application.dto.UpdateUserRequestDto;
import com.portfolio.workflow.user.application.dto.UserResponseDto;
import com.portfolio.workflow.user.application.mapper.UserApplicationMapper;
import com.portfolio.workflow.user.application.service.UserService;
import com.portfolio.workflow.user.domain.model.AccountStatus;
import com.portfolio.workflow.user.domain.model.Role;
import com.portfolio.workflow.user.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        return userService.findById(id)
                .map(UserApplicationMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
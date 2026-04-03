package com.portfolio.workflow.user.application.dto;

import com.portfolio.workflow.user.domain.model.AccountStatus;
import com.portfolio.workflow.user.domain.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO für Updates an einem bestehenden User.
 * <p>
 * Alle Felder sind optional. Null bedeutet: Feld wird nicht geändert.
 * Password: Klartext vom Request, wird im Service gehasht.
 * </p>
 */
public record UpdateUserRequestDto(

        @Size(min = 3, max = 50, message = "Username muss zwischen 3 und 50 Zeichen sein")
        String username,

        @Email(message = "Email muss gültig sein")
        String email,

        @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen haben")
        String password,

        Role role,      // optional: USER, MANAGER, ADMIN
        AccountStatus status     // optional: ACTIVE, INACTIVE, etc.
) {}
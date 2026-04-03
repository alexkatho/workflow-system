package com.portfolio.workflow.user.infrastructure.security;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.portfolio.workflow.user.domain.model.AccountStatus;
import com.portfolio.workflow.user.domain.model.Role;
import com.portfolio.workflow.user.domain.repository.UserRepository;
import com.portfolio.workflow.user.infrastructure.persistence.UserEntity;
import com.portfolio.workflow.user.infrastructure.repository.UserJpaRepository;

/**
 * Initialisiert einen Default-Admin Benutzer beim Start der Anwendung.
 *
 * Wird nur ausgeführt, wenn noch kein Admin existiert.
 */
@Component
public class AdminDataInitializer implements CommandLineRunner {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataInitializer(UserJpaRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@workflow.local").isEmpty()) {

            UserEntity admin = UserEntity.builder()
                    .id(UUID.randomUUID())
                    .username("admin")
                    .email("admin@workflow.local")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .status(AccountStatus.ACTIVE)
                    .build();

            userRepository.save(admin);
        }
    }
}
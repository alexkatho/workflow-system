package com.portfolio.workflow.user.domain.model;

import java.util.Set;
import java.util.UUID;

/**
 * Repräsentiert einen Benutzer im System mit Rolle, Berechtigungen, Status und Passwort-Hash.
 * <p>
 * Die Klasse ist Domain-getrieben, unabhängig von Persistence-Details.
 * </p>
 */
public class User {

    private final UUID id;
    private final String username;
    private final String email;
    private final String passwordHash; // nur Hash, nie Klartext
    private final Role role;
    private final Set<Permission> permissions;
    private final AccountStatus status;

    /**
     * Konstruktor für neue User (UUID automatisch generiert)
     */
    public User(String username, String email, String passwordHash, Role role, Set<Permission> permissions, AccountStatus status) {
        this(UUID.randomUUID(), username, email, passwordHash, role, permissions, status);
    }

    /**
     * Konstruktor für bestehende User (UUID aus DB)
     */
    public User(UUID id, String username, String email, String passwordHash, Role role, Set<Permission> permissions, AccountStatus status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.permissions = permissions;
        this.status = status;
    }

    // Getter
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public Set<Permission> getPermissions() { return permissions; }
    public AccountStatus getStatus() { return status; }

    // Business-Methods
    public boolean hasPermission(Permission permission) { return permissions.contains(permission); }
    public boolean isActive() { return status.isActive(); }
    public boolean isAdmin() { return role.isAdmin(); }
    public boolean isManager() { return role.isManager(); }
}
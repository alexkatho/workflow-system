package com.portfolio.workflow.user.domain.model;

import java.util.Set;

public enum Role {

    USER(Set.of(Permission.CREATE_REQUEST)),

    MANAGER(Set.of(
        Permission.CREATE_REQUEST,
        Permission.APPROVE_REQUEST
    )),

    ADMIN(Set.of(
        Permission.CREATE_REQUEST,
        Permission.APPROVE_REQUEST,
        Permission.MANAGE_USERS,
        Permission.VIEW_AUDIT_LOG
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isManager() {
        return this == MANAGER;
    }

    public boolean isUser() {
        return this == USER;
    }
}
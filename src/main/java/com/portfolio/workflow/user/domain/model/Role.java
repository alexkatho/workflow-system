package com.portfolio.workflow.user.domain.model;

public enum Role {
    USER,
    MANAGER,
    ADMIN;

    public boolean isAdmin() { return this == ADMIN; }
    public boolean isManager() { return this == MANAGER; }
    public boolean isUser() { return this == USER; }
}
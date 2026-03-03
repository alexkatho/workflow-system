package com.portfolio.workflow.user.domain.model;

public enum AccountStatus {
    ACTIVE,
    DISABLED;

    public boolean isActive() { return this == ACTIVE; }
}
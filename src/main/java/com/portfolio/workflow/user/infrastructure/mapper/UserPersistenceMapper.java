package com.portfolio.workflow.user.infrastructure.mapper;


import java.util.Set;

import com.portfolio.workflow.user.domain.model.Permission;
import com.portfolio.workflow.user.domain.model.User;
import com.portfolio.workflow.user.infrastructure.persistence.UserEntity;

/**
 * Mapper zwischen Domain User und UserEntity für Persistence.
 */
public class UserPersistenceMapper {

    public static User toDomain(UserEntity entity) {
        Set<Permission> perms = switch(entity.getRole()) {
            case USER -> Set.of(Permission.CREATE_REQUEST);
            case MANAGER -> Set.of(Permission.CREATE_REQUEST, Permission.APPROVE_REQUEST);
            case ADMIN -> Set.of(Permission.CREATE_REQUEST, Permission.APPROVE_REQUEST, Permission.MANAGE_USERS, Permission.VIEW_AUDIT_LOG);
        };
        return new User(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getPasswordHash(), entity.getRole(), perms, entity.getStatus());
    }

    public static UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPasswordHash(domain.getPasswordHash());
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatus());
        return entity;
    }
}
package com.portfolio.workflow.request.infrastructure.mapper;

import com.portfolio.workflow.request.domain.model.Request;
import com.portfolio.workflow.request.infrastructure.persistence.RequestEntity;

/**
 * Mapper zwischen Domain Request und RequestEntity.
 */
public final class RequestPersistenceMapper {

    private RequestPersistenceMapper() {
    }

    public static Request toDomain(RequestEntity entity) {
        return new Request(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getType(),
                entity.getStatus(),
                entity.getCreatedBy(),
                entity.getDecidedBy(),
                entity.getDecisionComment(),
                entity.getDecidedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static RequestEntity toEntity(Request request) {
        return RequestEntity.builder()
                .id(request.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .status(request.getStatus())
                .createdBy(request.getCreatedBy())
                .decidedBy(request.getDecidedBy())
                .decisionComment(request.getDecisionComment())
                .decidedAt(request.getDecidedAt())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}
package com.portfolio.workflow.request.application.mapper;

import com.portfolio.workflow.request.application.dto.RequestResponseDto;
import com.portfolio.workflow.request.domain.model.Request;

/**
 * Mapper zwischen Request-Domain-Modell und Response DTO.
 */
public final class RequestApplicationMapper {

    private RequestApplicationMapper() {
    }

    public static RequestResponseDto toResponseDto(Request request) {
        return new RequestResponseDto(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getType(),
                request.getStatus(),
                request.getCreatedBy(),
                request.getDecidedBy(),
                request.getDecisionComment(),
                request.getDecidedAt(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}
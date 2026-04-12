package com.portfolio.workflow.request.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.workflow.request.domain.model.RequestStatus;
import com.portfolio.workflow.request.infrastructure.persistence.RequestEntity;

/**
 * Technisches Spring Data JPA Repository für RequestEntity.
 */
public interface RequestJpaRepository extends JpaRepository<RequestEntity, UUID> {

    List<RequestEntity> findByCreatedBy(UUID createdBy);

    List<RequestEntity> findByStatus(RequestStatus status);
}
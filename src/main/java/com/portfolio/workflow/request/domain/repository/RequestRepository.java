package com.portfolio.workflow.request.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portfolio.workflow.request.domain.model.Request;
import com.portfolio.workflow.request.domain.model.RequestStatus;

/**
 * Fachliches Repository-Interface für Requests.
 */
public interface RequestRepository {

    Optional<Request> findById(UUID id);

    List<Request> findAll();

    List<Request> findByCreatedBy(UUID createdBy);

    List<Request> findByStatus(RequestStatus status);

    Request save(Request request);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
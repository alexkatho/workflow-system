package com.portfolio.workflow.request.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.portfolio.workflow.request.domain.model.Request;
import com.portfolio.workflow.request.domain.model.RequestStatus;
import com.portfolio.workflow.request.domain.repository.RequestRepository;
import com.portfolio.workflow.request.infrastructure.mapper.RequestPersistenceMapper;
import com.portfolio.workflow.request.infrastructure.persistence.RequestEntity;

/**
 * Implementierung des fachlichen RequestRepository.
 */
@Repository
public class RequestRepositoryImpl implements RequestRepository {

    private final RequestJpaRepository requestJpaRepository;

    public RequestRepositoryImpl(RequestJpaRepository requestJpaRepository) {
        this.requestJpaRepository = requestJpaRepository;
    }

    @Override
    public Optional<Request> findById(UUID id) {
        return requestJpaRepository.findById(id)
                .map(RequestPersistenceMapper::toDomain);
    }

    @Override
    public List<Request> findAll() {
        return requestJpaRepository.findAll()
                .stream()
                .map(RequestPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Request> findByCreatedBy(UUID createdBy) {
        return requestJpaRepository.findByCreatedBy(createdBy)
                .stream()
                .map(RequestPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Request> findByStatus(RequestStatus status) {
        return requestJpaRepository.findByStatus(status)
                .stream()
                .map(RequestPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Request save(Request request) {
        RequestEntity entity;

        Optional<RequestEntity> existingEntityOpt = requestJpaRepository.findById(request.getId());

        if (existingEntityOpt.isPresent()) {
            entity = existingEntityOpt.get();

            entity.setTitle(request.getTitle());
            entity.setDescription(request.getDescription());
            entity.setType(request.getType());
            entity.setStatus(request.getStatus());
            entity.setCreatedBy(request.getCreatedBy());
            entity.setDecidedBy(request.getDecidedBy());
            entity.setDecisionComment(request.getDecisionComment());
            entity.setDecidedAt(request.getDecidedAt());

        } else {
            entity = RequestPersistenceMapper.toEntity(request);
        }

        RequestEntity savedEntity = requestJpaRepository.save(entity);
        return RequestPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(UUID id) {
        requestJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return requestJpaRepository.existsById(id);
    }
}
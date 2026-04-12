package com.portfolio.workflow.request.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portfolio.workflow.request.application.exception.InvalidRequestStateException;
import com.portfolio.workflow.request.application.exception.RequestAccessDeniedException;
import com.portfolio.workflow.request.application.exception.RequestNotFoundException;
import com.portfolio.workflow.request.domain.model.Request;
import com.portfolio.workflow.request.domain.model.RequestStatus;
import com.portfolio.workflow.request.domain.model.RequestType;
import com.portfolio.workflow.request.domain.repository.RequestRepository;
import com.portfolio.workflow.user.domain.model.User;

/**
 * Service für fachliche Request-Use-Cases.
 */
@Service
public class RequestService {

    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public Request createRequest(String title,
                                 String description,
                                 RequestType type,
                                 UUID createdBy) {

        Request request = new Request(
                title,
                description,
                type,
                createdBy
        );

        return requestRepository.save(request);
    }

    public Optional<Request> findById(UUID id) {
        return requestRepository.findById(id);
    }

    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    public List<Request> findByCreatedBy(UUID createdBy) {
        return requestRepository.findByCreatedBy(createdBy);
    }

    public List<Request> findByStatus(RequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    /**
     * Gibt sichtbare Requests abhängig von der Rolle des aktuellen Benutzers zurück.
     */
    public List<Request> findVisibleRequests(User currentUser, RequestStatus status) {
        if (currentUser.isAdmin() || currentUser.isManager()) {
            return (status != null)
                    ? requestRepository.findByStatus(status)
                    : requestRepository.findAll();
        }

        List<Request> ownRequests = requestRepository.findByCreatedBy(currentUser.getId());

        if (status == null) {
            return ownRequests;
        }

        return ownRequests.stream()
                .filter(request -> request.getStatus() == status)
                .toList();
    }

    /**
     * Gibt einen einzelnen sichtbaren Request zurück.
     * USER darf nur eigene Requests sehen.
     * MANAGER und ADMIN dürfen alle sehen.
     */
    public Optional<Request> findVisibleById(UUID requestId, User currentUser) {
        Optional<Request> requestOpt = requestRepository.findById(requestId);

        if (requestOpt.isEmpty()) {
            return Optional.empty();
        }

        Request request = requestOpt.get();

        if (currentUser.isAdmin() || currentUser.isManager()) {
            return Optional.of(request);
        }

        if (request.getCreatedBy().equals(currentUser.getId())) {
            return Optional.of(request);
        }

        return Optional.empty();
    }

    public Request approveRequest(UUID requestId, UUID decidedBy, String decisionComment) {
        Request existingRequest = findExistingRequest(requestId);
        ensurePendingRequest(requestId, existingRequest, "genehmigt");

        Request updatedRequest = applyDecision(
                existingRequest,
                decidedBy,
                decisionComment,
                RequestStatus.APPROVED
        );

        return requestRepository.save(updatedRequest);
    }

    public Request rejectRequest(UUID requestId, UUID decidedBy, String decisionComment) {
        Request existingRequest = findExistingRequest(requestId);
        ensurePendingRequest(requestId, existingRequest, "abgelehnt");

        Request updatedRequest = applyDecision(
                existingRequest,
                decidedBy,
                decisionComment,
                RequestStatus.REJECTED
        );

        return requestRepository.save(updatedRequest);
    }
    
    /**
     * Storniert einen offenen Request.
     *
     * <p>
     * Nur der Ersteller des Requests darf den Request stornieren.
     * Ein Cancel ist nur im Status PENDING erlaubt.
     * </p>
     *
     * @param requestId ID des Requests
     * @param currentUser aktueller Benutzer
     * @return aktualisierter Request
     */
    public Request cancelRequest(UUID requestId, User currentUser) {
        Request existingRequest = findExistingRequest(requestId);

        ensureRequestOwner(existingRequest, currentUser);
        ensurePendingRequest(requestId, existingRequest, "storniert");

        Request updatedRequest = new Request(
                existingRequest.getId(),
                existingRequest.getTitle(),
                existingRequest.getDescription(),
                existingRequest.getType(),
                RequestStatus.CANCELLED,
                existingRequest.getCreatedBy(),
                existingRequest.getDecidedBy(),
                existingRequest.getDecisionComment(),
                existingRequest.getDecidedAt(),
                existingRequest.getCreatedAt(),
                LocalDateTime.now()
        );

        return requestRepository.save(updatedRequest);
    }
    
    private void ensureRequestOwner(Request request, User currentUser) {
        if (!request.getCreatedBy().equals(currentUser.getId())) {
            throw new RequestAccessDeniedException(request.getId());
        }
    }

    private Request findExistingRequest(UUID requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    private void ensurePendingRequest(UUID requestId, Request request, String action) {
        if (!request.getStatus().isPending()) {
            throw new InvalidRequestStateException(requestId, request.getStatus(), action);
        }
    }

    private Request applyDecision(Request existingRequest,
                                  UUID decidedBy,
                                  String decisionComment,
                                  RequestStatus targetStatus) {

        return new Request(
                existingRequest.getId(),
                existingRequest.getTitle(),
                existingRequest.getDescription(),
                existingRequest.getType(),
                targetStatus,
                existingRequest.getCreatedBy(),
                decidedBy,
                decisionComment,
                LocalDateTime.now(),
                existingRequest.getCreatedAt(),
                LocalDateTime.now()
        );
    }
}
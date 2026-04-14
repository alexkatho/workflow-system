package com.portfolio.workflow.request.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portfolio.workflow.request.application.event.RequestCancelledEvent;
import com.portfolio.workflow.request.application.event.RequestCreatedEvent;
import com.portfolio.workflow.request.application.event.RequestDecisionEvent;
import com.portfolio.workflow.request.application.event.RequestEventPublisher;
import com.portfolio.workflow.request.application.event.RequestEventType;
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
 *
 * <p>
 * Verantwortlich für:
 * <ul>
 *   <li>Erstellung von Requests</li>
 *   <li>Approval / Rejection</li>
 *   <li>Cancel-Operationen</li>
 *   <li>rollenbasierte Sichtbarkeit</li>
 *   <li>Veröffentlichung fachlicher Events</li>
 * </ul>
 * </p>
 */
@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestEventPublisher requestEventPublisher;

    public RequestService(RequestRepository requestRepository,
                          RequestEventPublisher requestEventPublisher) {
        this.requestRepository = requestRepository;
        this.requestEventPublisher = requestEventPublisher;
    }

    /**
     * Erstellt einen neuen Request und veröffentlicht anschließend ein Created-Event.
     *
     * @param title Titel des Requests
     * @param description Beschreibung des Requests
     * @param type fachlicher Typ
     * @param createdBy ID des antragstellenden Benutzers
     * @return gespeicherter Request
     */
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

        Request savedRequest = requestRepository.save(request);

        requestEventPublisher.publishRequestCreated(
                new RequestCreatedEvent(
                        UUID.randomUUID(),
                        RequestEventType.CREATED,
                        savedRequest.getId(),
                        savedRequest.getType(),
                        savedRequest.getStatus(),
                        savedRequest.getCreatedBy(),
                        LocalDateTime.now()
                )
        );

        return savedRequest;
    }

    /**
     * Sucht einen Request anhand seiner ID.
     *
     * @param id Request-ID
     * @return optionaler Request
     */
    public Optional<Request> findById(UUID id) {
        return requestRepository.findById(id);
    }

    /**
     * Gibt alle Requests zurück.
     *
     * @return Liste aller Requests
     */
    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    /**
     * Gibt alle Requests eines bestimmten Benutzers zurück.
     *
     * @param createdBy Benutzer-ID des Erstellers
     * @return Liste eigener Requests
     */
    public List<Request> findByCreatedBy(UUID createdBy) {
        return requestRepository.findByCreatedBy(createdBy);
    }

    /**
     * Gibt alle Requests mit einem bestimmten Status zurück.
     *
     * @param status Request-Status
     * @return Liste passender Requests
     */
    public List<Request> findByStatus(RequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    /**
     * Gibt sichtbare Requests abhängig von der Rolle des aktuellen Benutzers zurück.
     *
     * <p>
     * USER sieht nur eigene Requests.
     * MANAGER und ADMIN sehen alle Requests.
     * </p>
     *
     * @param currentUser aktuell eingeloggter Benutzer
     * @param status optionaler Statusfilter
     * @return sichtbare Requests
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
     *
     * <p>
     * USER darf nur eigene Requests sehen.
     * MANAGER und ADMIN dürfen alle Requests sehen.
     * </p>
     *
     * @param requestId Request-ID
     * @param currentUser aktuell eingeloggter Benutzer
     * @return optional sichtbarer Request
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

    /**
     * Genehmigt einen offenen Request und veröffentlicht anschließend ein Decision-Event.
     *
     * @param requestId Request-ID
     * @param decidedBy ID des entscheidenden Benutzers
     * @param decisionComment optionaler Entscheidungskommentar
     * @return aktualisierter Request
     */
    public Request approveRequest(UUID requestId, UUID decidedBy, String decisionComment) {
        Request existingRequest = findExistingRequest(requestId);
        ensurePendingRequest(requestId, existingRequest, "genehmigt");

        Request updatedRequest = applyDecision(
                existingRequest,
                decidedBy,
                decisionComment,
                RequestStatus.APPROVED
        );

        Request savedRequest = requestRepository.save(updatedRequest);

        requestEventPublisher.publishRequestDecision(
                new RequestDecisionEvent(
                        UUID.randomUUID(),
                        RequestEventType.APPROVED,
                        savedRequest.getId(),
                        savedRequest.getType(),
                        savedRequest.getStatus(),
                        savedRequest.getCreatedBy(),
                        savedRequest.getDecidedBy(),
                        savedRequest.getDecisionComment(),
                        LocalDateTime.now()
                )
        );

        return savedRequest;
    }

    /**
     * Lehnt einen offenen Request ab und veröffentlicht anschließend ein Decision-Event.
     *
     * @param requestId Request-ID
     * @param decidedBy ID des entscheidenden Benutzers
     * @param decisionComment Ablehnungskommentar
     * @return aktualisierter Request
     */
    public Request rejectRequest(UUID requestId, UUID decidedBy, String decisionComment) {
        Request existingRequest = findExistingRequest(requestId);
        ensurePendingRequest(requestId, existingRequest, "abgelehnt");

        Request updatedRequest = applyDecision(
                existingRequest,
                decidedBy,
                decisionComment,
                RequestStatus.REJECTED
        );

        Request savedRequest = requestRepository.save(updatedRequest);

        requestEventPublisher.publishRequestDecision(
                new RequestDecisionEvent(
                        UUID.randomUUID(),
                        RequestEventType.REJECTED,
                        savedRequest.getId(),
                        savedRequest.getType(),
                        savedRequest.getStatus(),
                        savedRequest.getCreatedBy(),
                        savedRequest.getDecidedBy(),
                        savedRequest.getDecisionComment(),
                        LocalDateTime.now()
                )
        );

        return savedRequest;
    }

    /**
     * Storniert einen offenen Request und veröffentlicht anschließend ein Cancelled-Event.
     *
     * <p>
     * Nur der Ersteller darf seinen eigenen Request stornieren.
     * </p>
     *
     * @param requestId Request-ID
     * @param currentUser aktuell eingeloggter Benutzer
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

        Request savedRequest = requestRepository.save(updatedRequest);

        requestEventPublisher.publishRequestCancelled(
                new RequestCancelledEvent(
                        UUID.randomUUID(),
                        RequestEventType.CANCELLED,
                        savedRequest.getId(),
                        savedRequest.getType(),
                        savedRequest.getStatus(),
                        savedRequest.getCreatedBy(),
                        LocalDateTime.now()
                )
        );

        return savedRequest;
    }

    /**
     * Lädt einen bestehenden Request oder wirft eine fachliche Not-Found-Exception.
     *
     * @param requestId Request-ID
     * @return bestehender Request
     */
    private Request findExistingRequest(UUID requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    /**
     * Stellt sicher, dass ein Request noch offen ist.
     *
     * @param requestId Request-ID
     * @param request Request
     * @param action fachliche Aktion für die Fehlermeldung
     */
    private void ensurePendingRequest(UUID requestId, Request request, String action) {
        if (!request.getStatus().isPending()) {
            throw new InvalidRequestStateException(requestId, request.getStatus(), action);
        }
    }

    /**
     * Stellt sicher, dass der aktuelle Benutzer Eigentümer des Requests ist.
     *
     * @param request Request
     * @param currentUser aktuell eingeloggter Benutzer
     */
    private void ensureRequestOwner(Request request, User currentUser) {
        if (!request.getCreatedBy().equals(currentUser.getId())) {
            throw new RequestAccessDeniedException(request.getId());
        }
    }

    /**
     * Baut eine neue Request-Instanz für Approval-/Rejection-Entscheidungen.
     *
     * @param existingRequest bestehender Request
     * @param decidedBy ID des entscheidenden Benutzers
     * @param decisionComment optionaler Entscheidungskommentar
     * @param targetStatus Zielstatus
     * @return neue Request-Instanz mit Entscheidung
     */
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
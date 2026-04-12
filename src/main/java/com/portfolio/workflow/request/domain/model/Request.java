package com.portfolio.workflow.request.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Repräsentiert einen fachlichen Request innerhalb des Workflow- und Approval-Systems.
 *
 * <p>
 * Ein Request wird von einem Benutzer erstellt und durch einen Manager
 * genehmigt oder abgelehnt.
 * </p>
 */
public class Request {

    private final UUID id;
    private final String title;
    private final String description;
    private final RequestType type;
    private final RequestStatus status;
    private final UUID createdBy;
    private final UUID decidedBy;
    private final String decisionComment;
    private final LocalDateTime decidedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /**
     * Konstruktor für neue Requests.
     *
     * <p>
     * Neue Requests starten immer im Status {@link RequestStatus#PENDING}.
     * Erstellungs- und Aktualisierungszeitpunkt werden automatisch gesetzt.
     * </p>
     *
     * @param title Titel des Requests
     * @param description Beschreibung des Requests
     * @param type fachlicher Typ des Requests
     * @param createdBy ID des antragstellenden Benutzers
     */
    public Request(String title,
                   String description,
                   RequestType type,
                   UUID createdBy) {
        this(
                UUID.randomUUID(),
                title,
                description,
                type,
                RequestStatus.PENDING,
                createdBy,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    /**
     * Vollständiger Konstruktor für bestehende Requests.
     *
     * @param id ID des Requests
     * @param title Titel des Requests
     * @param description Beschreibung des Requests
     * @param type fachlicher Typ des Requests
     * @param status aktueller Status
     * @param createdBy ID des antragstellenden Benutzers
     * @param decidedBy ID des entscheidenden Benutzers
     * @param decisionComment optionaler Entscheidungskommentar
     * @param decidedAt Zeitpunkt der Entscheidung
     * @param createdAt Erstellungszeitpunkt
     * @param updatedAt letzter Änderungszeitpunkt
     */
    public Request(UUID id,
                   String title,
                   String description,
                   RequestType type,
                   RequestStatus status,
                   UUID createdBy,
                   UUID decidedBy,
                   String decisionComment,
                   LocalDateTime decidedAt,
                   LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.status = status;
        this.createdBy = createdBy;
        this.decidedBy = decidedBy;
        this.decisionComment = decisionComment;
        this.decidedAt = decidedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public RequestType getType() {
        return type;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public UUID getDecidedBy() {
        return decidedBy;
    }

    public String getDecisionComment() {
        return decisionComment;
    }

    public LocalDateTime getDecidedAt() {
        return decidedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
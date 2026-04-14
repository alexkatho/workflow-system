package com.portfolio.workflow.request.application.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.portfolio.workflow.request.domain.model.RequestStatus;
import com.portfolio.workflow.request.domain.model.RequestType;

/**
 * Gemeinsamer Vertrag für fachliche Request-Events.
 *
 * <p>
 * Das Interface definiert die gemeinsamen Metadaten aller Request-Events.
 * Es speichert selbst keine Felder, sondern beschreibt nur die nach außen
 * verfügbaren Eigenschaften.
 * </p>
 */
public interface RequestEvent {

    /**
     * Eindeutige ID des Events.
     *
     * @return Event-ID
     */
    UUID eventId();

    /**
     * Typ des fachlichen Events.
     *
     * @return Event-Typ
     */
    RequestEventType eventType();

    /**
     * ID des betroffenen Requests.
     *
     * @return Request-ID
     */
    UUID requestId();

    /**
     * Fachlicher Typ des Requests.
     *
     * @return Request-Typ
     */
    RequestType requestType();

    /**
     * Aktueller Status des Requests zum Zeitpunkt des Events.
     *
     * @return Request-Status
     */
    RequestStatus requestStatus();

    /**
     * ID des antragstellenden Benutzers.
     *
     * @return Benutzer-ID des Erstellers
     */
    UUID createdBy();

    /**
     * Zeitpunkt, an dem das Event fachlich aufgetreten ist.
     *
     * @return Event-Zeitpunkt
     */
    LocalDateTime occurredAt();
}
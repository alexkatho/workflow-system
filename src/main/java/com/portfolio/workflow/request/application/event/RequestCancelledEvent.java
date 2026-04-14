package com.portfolio.workflow.request.application.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.portfolio.workflow.request.domain.model.RequestStatus;
import com.portfolio.workflow.request.domain.model.RequestType;

/**
 * Fachliches Event, das nach der Stornierung eines Requests veröffentlicht wird.
 *
 * @param eventId eindeutige ID des Events
 * @param eventType Typ des Events
 * @param requestId ID des Requests
 * @param requestType fachlicher Typ des Requests
 * @param requestStatus aktueller Status des Requests
 * @param createdBy ID des antragstellenden Benutzers
 * @param occurredAt Zeitpunkt des Events
 */
public record RequestCancelledEvent(
        UUID eventId,
        RequestEventType eventType,
        UUID requestId,
        RequestType requestType,
        RequestStatus requestStatus,
        UUID createdBy,
        LocalDateTime occurredAt
) implements RequestEvent {
}
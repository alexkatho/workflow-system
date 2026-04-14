package com.portfolio.workflow.request.application.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.portfolio.workflow.request.domain.model.RequestStatus;
import com.portfolio.workflow.request.domain.model.RequestType;

/**
 * Fachliches Event für Entscheidungen über Requests.
 *
 * <p>
 * Dieses Event wird sowohl für Genehmigungen als auch für Ablehnungen verwendet.
 * Der konkrete Entscheidungstyp wird über {@link RequestEventType} abgebildet.
 * Zulässige Event-Typen sind APPROVED und REJECTED.
 * </p>
 *
 * @param eventId eindeutige ID des Events
 * @param eventType Typ des Events (APPROVED oder REJECTED)
 * @param requestId ID des Requests
 * @param requestType fachlicher Typ des Requests
 * @param requestStatus aktueller Status des Requests
 * @param createdBy ID des antragstellenden Benutzers
 * @param decidedBy ID des entscheidenden Benutzers
 * @param decisionComment optionaler Entscheidungskommentar
 * @param occurredAt Zeitpunkt des Events
 */
public record RequestDecisionEvent(
        UUID eventId,
        RequestEventType eventType,
        UUID requestId,
        RequestType requestType,
        RequestStatus requestStatus,
        UUID createdBy,
        UUID decidedBy,
        String decisionComment,
        LocalDateTime occurredAt
) implements RequestEvent {

    /**
     * Validiert, dass nur APPROVED oder REJECTED als Event-Typ verwendet werden.
     */
    public RequestDecisionEvent {
        if (eventType != RequestEventType.APPROVED && eventType != RequestEventType.REJECTED) {
            throw new IllegalArgumentException(
                    "RequestDecisionEvent unterstützt nur APPROVED oder REJECTED, aber war: " + eventType
            );
        }
    }
}
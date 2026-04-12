package com.portfolio.workflow.request.domain.model;

/**
 * Status eines fachlichen Requests innerhalb des Approval-Prozesses.
 */
public enum RequestStatus {

    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED;

    /**
     * Prüft, ob der Request noch offen ist.
     *
     * @return true, wenn Status = PENDING
     */
    public boolean isPending() {
        return this == PENDING;
    }

    /**
     * Prüft, ob der Request bereits entschieden wurde.
     *
     * @return true, wenn Status = APPROVED oder REJECTED
     */
    public boolean isDecided() {
        return this == APPROVED || this == REJECTED;
    }

    /**
     * Prüft, ob der Request einen finalen Endstatus erreicht hat.
     *
     * @return true, wenn Status final ist
     */
    public boolean isFinal() {
        return this == APPROVED || this == REJECTED || this == CANCELLED;
    }
    
    /**
     * Prüft, ob der Request bereits geschlossen ist.
     *
     * @return true, wenn Status = CANCELLED
     */
    public boolean isCancelled() {
        return this == CANCELLED;
    }
}
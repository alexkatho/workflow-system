package com.portfolio.workflow.request.application.exception;

import java.util.UUID;

import com.portfolio.workflow.request.domain.model.RequestStatus;

/**
 * Wird geworfen, wenn ein Request nicht im erwarteten Status ist.
 */
public class InvalidRequestStateException extends RuntimeException {

    public InvalidRequestStateException(UUID requestId, RequestStatus currentStatus, String action) {
        super("Request " + requestId + " kann nicht " + action + " werden, aktueller Status: " + currentStatus);
    }
}
package com.portfolio.workflow.request.application.exception;

import java.util.UUID;

/**
 * Wird geworfen, wenn ein Request nicht gefunden wurde.
 */
public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(UUID requestId) {
        super("Request nicht gefunden: " + requestId);
    }
}
package com.portfolio.workflow.request.application.exception;

import java.util.UUID;

/**
 * Wird geworfen, wenn ein Benutzer keinen Zugriff auf einen Request hat.
 */
public class RequestAccessDeniedException extends RuntimeException {

    public RequestAccessDeniedException(UUID requestId) {
        super("Kein Zugriff auf Request: " + requestId);
    }
}
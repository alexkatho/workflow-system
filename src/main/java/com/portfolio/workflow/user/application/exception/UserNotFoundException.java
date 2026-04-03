package com.portfolio.workflow.user.application.exception;

import java.util.UUID;

/**
 * Exception, wenn ein Benutzer nicht gefunden wurde.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID userId) {
        super("User nicht gefunden: " + userId);
    }
}
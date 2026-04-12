package com.portfolio.workflow.user.application.exception;

/**
 * Wird geworfen, wenn der aktuell authentifizierte Benutzer nicht gefunden wurde.
 */
public class CurrentUserNotFoundException extends RuntimeException {

    public CurrentUserNotFoundException(String email) {
        super("Aktueller Benutzer nicht gefunden: " + email);
    }
}
package com.portfolio.workflow.user.application.exception;

/**
 * Wird geworfen, wenn sich ein deaktivierter Benutzer anmelden möchte.
 */
public class InactiveUserException extends RuntimeException {

    public InactiveUserException(String email) {
        super("Benutzerkonto ist nicht aktiv: " + email);
    }
}
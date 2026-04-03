package com.portfolio.workflow.user.application.exception;

/**
 * Wird geworfen, wenn Login-Daten ungültig sind.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Ungültige Email oder Passwort");
    }
}
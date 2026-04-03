package com.portfolio.workflow.user.application.exception;

/**
 * Exception, wenn ein Benutzername bereits vergeben ist.
 */
public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super("Username ist bereits vergeben: " + username);
    }
}
package com.portfolio.workflow.user.application.exception;

/**
 * Exception, wenn eine E-Mail-Adresse bereits vergeben ist.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Email ist bereits vergeben: " + email);
    }
}
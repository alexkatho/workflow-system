package com.portfolio.workflow.shared.presentation.exception;

import java.time.LocalDateTime;

/**
 * Standardisierte Fehlerantwort für REST APIs.
 *
 * @param timestamp Zeitpunkt des Fehlers
 * @param status HTTP-Statuscode
 * @param error HTTP-Fehlerbezeichnung
 * @param message fachliche Fehlermeldung
 * @param path aufgerufener Pfad
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
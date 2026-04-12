package com.portfolio.workflow.request.domain.model;

/**
 * Fachlicher Typ eines Requests.
 */
public enum RequestType {

    /**
     * Antrag auf Zugriff oder Berechtigung.
     */
    ACCESS_REQUEST,

    /**
     * Antrag auf Hardware.
     */
    HARDWARE_REQUEST,

    /**
     * Antrag auf Software.
     */
    SOFTWARE_REQUEST,

    /**
     * Allgemeiner Genehmigungsantrag.
     */
    GENERAL_APPROVAL
}
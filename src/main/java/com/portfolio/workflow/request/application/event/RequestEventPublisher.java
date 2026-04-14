package com.portfolio.workflow.request.application.event;

/**
 * Abstraktion für das Veröffentlichen fachlicher Request-Events.
 *
 * <p>
 * Die Application-Schicht kennt nur dieses Interface und bleibt damit
 * unabhängig von der konkreten technischen Messaging-Lösung.
 * </p>
 */
public interface RequestEventPublisher {

    /**
     * Veröffentlicht ein Event nach Erstellung eines Requests.
     *
     * @param event fachliches Created-Event
     */
    void publishRequestCreated(RequestCreatedEvent event);

    /**
     * Veröffentlicht ein Event nach einer Entscheidung über einen Request.
     *
     * <p>
     * Das Event kann fachlich entweder APPROVED oder REJECTED repräsentieren.
     * </p>
     *
     * @param event fachliches Decision-Event
     */
    void publishRequestDecision(RequestDecisionEvent event);

    /**
     * Veröffentlicht ein Event nach Stornierung eines Requests.
     *
     * @param event fachliches Cancelled-Event
     */
    void publishRequestCancelled(RequestCancelledEvent event);
}
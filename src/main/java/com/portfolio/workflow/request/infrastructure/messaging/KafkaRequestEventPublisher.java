package com.portfolio.workflow.request.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.portfolio.workflow.request.application.event.RequestCancelledEvent;
import com.portfolio.workflow.request.application.event.RequestCreatedEvent;
import com.portfolio.workflow.request.application.event.RequestDecisionEvent;
import com.portfolio.workflow.request.application.event.RequestEventPublisher;

/**
 * Kafka-basierte Implementierung des RequestEventPublisher.
 */
@Component
public class KafkaRequestEventPublisher implements RequestEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaRequestEventPublisher.class);

    private static final String REQUEST_TOPIC = "workflow.requests";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaRequestEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishRequestCreated(RequestCreatedEvent event) {
        log.info("Publishing RequestCreatedEvent for requestId={}", event.requestId());
        kafkaTemplate.send(REQUEST_TOPIC, event.requestId().toString(), event);
    }

    @Override
    public void publishRequestDecision(RequestDecisionEvent event) {
        log.info("Publishing RequestDecisionEvent type={} for requestId={}", event.eventType(), event.requestId());
        kafkaTemplate.send(REQUEST_TOPIC, event.requestId().toString(), event);
    }

    @Override
    public void publishRequestCancelled(RequestCancelledEvent event) {
        log.info("Publishing RequestCancelledEvent for requestId={}", event.requestId());
        kafkaTemplate.send(REQUEST_TOPIC, event.requestId().toString(), event);
    }
}
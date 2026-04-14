package com.portfolio.workflow.request.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Einfacher Kafka-Consumer zum Loggen eingehender Request-Events.
 *
 * <p>
 * Dieser Consumer dient zunächst der lokalen Entwicklung und Verifikation
 * des Event-Flows. Eingehende Nachrichten werden als JSON-String empfangen
 * und ins Log geschrieben.
 * </p>
 */
@Component
public class RequestEventLoggingConsumer {

    private static final Logger log = LoggerFactory.getLogger(RequestEventLoggingConsumer.class);

    /**
     * Empfängt Request-Events aus Kafka und loggt deren Inhalt.
     *
     * @param payload Event-Payload als JSON-String
     */
    @KafkaListener(topics = "workflow.requests", groupId = "workflow-group")
    public void consume(String payload) {
        log.info("Received Kafka message from topic workflow.requests: {}", payload);
    }
}
package com.finflow.portfolio.infrastructure.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.finflow.portfolio.infrastructure.kafka.event.PortfolioEvent;

@Component
public class PortfolioEventProducer {

    private static final Logger log = LoggerFactory.getLogger(PortfolioEventProducer.class);

    private final KafkaTemplate<String, PortfolioEvent> kafkaTemplate;

    @Value("${app.kafka.topics.portfolio-updated}")
    private String topic;

    public PortfolioEventProducer(KafkaTemplate<String, PortfolioEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPortfolioUpdated(String userId, String portfolioId, String symbol, String action) {
        PortfolioEvent event = PortfolioEvent.of(userId, portfolioId, symbol, action);
        kafkaTemplate.send(topic, userId, event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send event for user {}: {}", userId, ex.getMessage());
                } else {
                    log.info("Sent {} event for {} to partition {}",
                        action, symbol, result.getRecordMetadata().partition());
                }
            });
    }
}

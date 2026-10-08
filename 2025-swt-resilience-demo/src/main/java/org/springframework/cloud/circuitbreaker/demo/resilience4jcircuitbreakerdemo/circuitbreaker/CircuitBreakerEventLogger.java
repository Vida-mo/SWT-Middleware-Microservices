package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class CircuitBreakerEventLogger {

    private static final Logger LOG = LoggerFactory.getLogger(CircuitBreakerEventLogger.class);

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerEventLogger(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @PostConstruct
    public void registerEventConsumers() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(this::registerEventConsumer);

        circuitBreakerRegistry.getEventPublisher()
                .onEntryAdded(event -> registerEventConsumer(event.getAddedEntry()));
    }

    private void registerEventConsumer(CircuitBreaker circuitBreaker) {
        circuitBreaker.getEventPublisher()
                .onStateTransition(this::logStateTransition)
                .onSuccess(event -> LOG.info("Successful Request"))
                .onError(event -> LOG.info("Error Request"))
                .onCallNotPermitted(event -> LOG.info("Denied Request"))
                .onFailureRateExceeded(event -> LOG.info("Failure rate exceeded!"))
                .onReset(event -> LOG.info("CB manually or programmatically reset"))
        ;
    }

    private void logStateTransition(CircuitBreakerOnStateTransitionEvent event) {
        LOG.info("CircuitBreaker '{}' changed state from {} to {}",
                event.getCircuitBreakerName(),
                event.getStateTransition().getFromState(),
                event.getStateTransition().getToState());
    }
}
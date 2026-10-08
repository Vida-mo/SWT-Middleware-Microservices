package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.resilient;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResilientController {

    private final ResilientService resilientTaskService;

    private final CircuitBreaker circuitBreaker;
    private final Bulkhead bulkhead;

    @Autowired
    public ResilientController(CircuitBreakerFactory circuitBreakerFactory, BulkheadRegistry bulkheadRegistry, ResilientService resilientTaskService) {
        this.resilientTaskService = resilientTaskService;
        this.circuitBreaker = circuitBreakerFactory.create("taskCB");

        BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
                .maxConcurrentCalls(5)
                .maxWaitDuration(java.time.Duration.ofMillis(500))
                .build();
        this.bulkhead = bulkheadRegistry.bulkhead("taskBH", bulkheadConfig);
    }

    @GetMapping("/resilient-task")
    public String performTask() {
        try {
            return bulkhead.executeSupplier(() ->
                    // Simuliere eine aufwendige Aufgabe
                    circuitBreaker.run(resilientTaskService::performTask, throwable -> "Circuit Breaker failed")
            );
        } catch (Exception e) {
            // Allgemeine Fehlerbehandlung, fängt Fehler von Bulkhead, Circuit Breaker und anderen Ursachen ab
            return "Failed to perform task because of Bulkhead: " + e.getMessage();
        }
    }
}

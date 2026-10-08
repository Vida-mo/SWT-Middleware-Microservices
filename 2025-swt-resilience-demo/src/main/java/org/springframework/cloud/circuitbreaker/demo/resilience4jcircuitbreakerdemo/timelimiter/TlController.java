package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.timelimiter;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TlController {

    Logger LOG = LoggerFactory.getLogger(TlController.class);

    private CircuitBreaker circuitBreaker;

    private TlService tlService;

    public TlController(CircuitBreakerFactory circuitBreakerFactory, TlService tlService) {
        this.tlService = tlService;
        this.circuitBreaker = circuitBreakerFactory.create("timedelay");
    }

    @GetMapping("/time-limited")
    public String getTimeLimitedOperation() {
        return circuitBreaker.run(() -> tlService.performTimeLimitedOperation(), throwable -> {
            LOG.info("Operation is timed out");
            return "Operation is timed out";
        });

    }

}

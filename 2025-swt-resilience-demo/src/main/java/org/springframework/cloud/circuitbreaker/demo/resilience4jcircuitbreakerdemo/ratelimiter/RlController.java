package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.ratelimiter;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class RlController {

    Logger LOG = LoggerFactory.getLogger(RlController.class);

    private RlService rlService;

    private RateLimiter rateLimiter;

    public RlController(RateLimiterRegistry rateLimiterRegistry, RlService rlService) {
        this.rlService = rlService;
        this.rateLimiter = rateLimiterRegistry.rateLimiter("myRateLimiter");

        rateLimiter.getEventPublisher()
                .onSuccess(event -> LOG.info("Permit GRANTED at {}, available permissons: {}", Instant.now(), rateLimiter.getMetrics().getAvailablePermissions()))
                .onFailure(event -> LOG.warn("Permit DENIED at {}, available permissons {}", Instant.now(), rateLimiter.getMetrics().getAvailablePermissions()));
    }

    @GetMapping("/rate-limited")
    public String callLimitedMethod() {
        LOG.info("Anfrage angekommen");
        try {
            String response = RateLimiter.decorateSupplier(rateLimiter, rlService::performTask).get();
            LOG.info("Anfrage erfolgreich bearbeitet");
            return response;
        } catch (RequestNotPermitted ex) {
            LOG.info("Anfrage abgelehnt");
            // Geeignete Maßnahmen treffen oder eine benutzerdefinierte Nachricht zurückgeben
            return "Die maximale Anzahl an Anfragen wurde erreicht. Bitte versuchen Sie es später erneut.";
        }
    }

}

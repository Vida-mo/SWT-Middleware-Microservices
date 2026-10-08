package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.bulkhead;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class BhController {

    Logger LOG = LoggerFactory.getLogger(BhController.class);

    private BhService bhService;

    private Bulkhead bulkhead;

    public BhController(BulkheadRegistry bulkheadRegistry, BhService bhService) {
        this.bulkhead = bulkheadRegistry.bulkhead("myBulkhead");
        this.bhService = bhService;
    }

    @GetMapping("/bulkhead-perform")
    public String perform() {
        int randomId = 10 + new Random().nextInt(90);
        LOG.info("Id: {}, Anfrage angekommen", randomId);

        try {
            return Bulkhead.decorateSupplier(bulkhead, () -> bhService.performBulkheadTask(randomId)).get();
        } catch (BulkheadFullException ex) {
            LOG.info("Id: {}, Anfrage abgelehnt", randomId);
            // Geeignete Maßnahmen treffen oder eine benutzerdefinierte Nachricht zurückgeben
            return "Bulkhead sagt nein";
        }
    }
}
package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.timelimiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TlService {

    Logger LOG = LoggerFactory.getLogger(TlService.class);

    public String performTimeLimitedOperation() {
        int sleepTime = new Random().nextInt(3000);

        LOG.info("Sleeping for {}", sleepTime);

        try {
            Thread.sleep(sleepTime);  // Simuliert eine lang andauernde Operation (max 3 Sekunden)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Operation erfolgreich!!!";
    }
}

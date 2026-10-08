package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.bulkhead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BhService {

    private static final Logger LOG = LoggerFactory.getLogger(BhService.class);


    public String performBulkheadTask(int id) {
        LOG.info("Id: {}, Abarbeitung beginnt", id);
        try {
            Thread.sleep(500);
            LOG.info("Id: {}, Abarbeitung erfolgreich abgeschlossen", id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Bulkhead sagt danke";
    }

}
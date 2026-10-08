package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.resilient;

import org.springframework.stereotype.Service;

@Service
public class ResilientService {

    public String performTask() {
        // Simuliere eine aufwendige Aufgabe
        try {
            Thread.sleep(1000); // Simuliert eine Aufgabe, die 1 Sekunde dauert
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Task completed successfully";
    }
}

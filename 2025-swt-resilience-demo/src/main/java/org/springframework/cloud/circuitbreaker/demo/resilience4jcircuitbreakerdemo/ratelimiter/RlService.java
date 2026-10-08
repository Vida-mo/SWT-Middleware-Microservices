package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.ratelimiter;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.stereotype.Service;

@Service
public class RlService {

    public String performTask() {
        return "Request erfolgreich durchgeführt";
    }


}

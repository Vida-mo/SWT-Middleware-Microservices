/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.circuitbreaker.demo.resilience4jcircuitbreakerdemo.circuitbreaker;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Ryan Baxter
 */
@RestController
public class CbController {

    Logger LOG = LoggerFactory.getLogger(CbController.class);

    private HttpBinService httpBin;

    private CircuitBreaker circuitBreaker;

    public CbController(CircuitBreakerFactory circuitBreakerFactory, HttpBinService httpBinService) {
        this.httpBin = httpBinService;
        this.circuitBreaker = circuitBreakerFactory.create("randomCode");
    }

    @GetMapping("/get")
    public Map delay() {
        return circuitBreaker.run(httpBin.randomOkOrInternalServerError(), throwable -> {
//			hier fallback-Methode aufrufen
            if (throwable instanceof ExecutionException) {  // Server dahinter liefert Error
                Map<String, String> response = new HashMap<>();
                response.put("message", "Operation threw error, please try again later.");
                return response;
            } else {
                // CircuitBreaker OPEN
                Map<String, String> fallback = new HashMap<>();
                fallback.put("message", "Request failed, please try again later.");
                return fallback;
            }
        });
    }

}

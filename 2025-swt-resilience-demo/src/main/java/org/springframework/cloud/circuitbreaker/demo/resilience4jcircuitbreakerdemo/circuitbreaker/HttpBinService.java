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

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ryan Baxter
 */
@Service
public class HttpBinService {

	Logger LOG = LoggerFactory.getLogger(HttpBinService.class);

	private RestTemplate rest;

	public HttpBinService(RestTemplate rest) {
		this.rest = rest;
	}

	public Map get() {
		return rest.getForObject("https://httpbin.org/get", Map.class);

	}

	public Supplier<Map> randomOkOrInternalServerError() {
		return () -> {
			Random random = new Random();
			boolean randomBool = random.nextBoolean();
			int statusCode = randomBool ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();

			int id = 10 + random.nextInt(90);

			LOG.info("Id: {}: Send request with status code: {}", id, statusCode);

			Map response = rest.getForObject("https://httpbin.org/status/" + statusCode, Map.class);

			LOG.info("Id: {}: Response received with status code: {}", id, statusCode);
			return response;
		};
	}
}

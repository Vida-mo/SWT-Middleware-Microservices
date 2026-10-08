package my.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	//	Step Simple Gateway
//	@Bean
//	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//		return builder.routes()
//				// Add a simple re-route from: /get to: http://httpbin.org:80
//				// Add a simple "Hello:World" HTTP Header
//				.route(p -> p
//						.path("/get") // intercept calls to the /get path
//						.filters(f -> f.addRequestHeader("Hello", "World")) // add header
//						.uri("http://httpbin.org:80")) // forward to httpbin
//
//
//				.route(p -> p
//						.path("/get-wo-headers")
//						.filters(f -> f
//								.rewritePath("/get-wo-headers", "/headers")
//								.addRequestHeader("Hello", "World") // Fügt den Header hinzu
//								.removeRequestHeader("Hello")) // Entfernt den Header
//						.uri("http://httpbin.org:80"))
//
//				.route(p -> p
//						.path("/response-headers")
//						.filters(f -> f
////                                .setRequestHeader("Hello", "World") // Setzt den Header
//								.rewritePath("/response-headers", "/response-headers?Hello=World")) // Wandelt ihn in Query-Parameter um
//						.uri("http://httpbin.org:80"))
//
//				.build();
//	}

}

package my.example.mygateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    private static Logger logger = LoggerFactory.getLogger(GatewaySecurityConfig.class);

    @Value("${spring.security.oauth2.client.registration.gateway.authorization-grant-type}")
    private String grantType;


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())  // CSRF in APIs meistens nicht benötigt
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/public/**", "/check", "/show-token", "/say-hello", "/opening").permitAll()  // Öffentliche Endpunkte
                        // 🌟 Client Credentials verwennden
//                        .pathMatchers("/api/private/**", "/private/user", "/logout").authenticated()
                        .anyExchange()
                        .authenticated());

       // 🌟 Authorization Code → Redirect-Login aktivieren || "client_credentials".equals(grantType)
        if ("authorization_code".equals(grantType) ) {
            http.oauth2Login(Customizer.withDefaults())
                .oidcLogout(Customizer.withDefaults());
        }

        // 🌟 Client Credentials → Nur Token-Validierung (kein Redirect-Login)
        if ("client_credentials".equals(grantType)) {
            http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        }

        // 🌟 Implicit
        if ("implicit".equals(grantType)) {
            http.oauth2Login(Customizer.withDefaults());
            http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        }

        // Logout für alle Grant-Typen aktivieren
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
            )
            .oidcLogout(Customizer.withDefaults());;

        return http.build();
    }



//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf(csrf -> csrf.disable())  // CSRF in APIs meistens nicht benötigt
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/api/public/**", "/check", "/show-token", "/say-hello", "/opening").permitAll()  // Öffentliche Endpunkte
//                        .pathMatchers("/api/private/**", "private/user", "/logout").hasAuthority("SCOPE_uaa.user")  // Geschützte Endpunkte
//                        .anyExchange().authenticated())  // Standardmäßig erfordern alle anderen Anfragen Authentifizierung
//                .oauth2Login(Customizer.withDefaults())
//                .logout((logout) -> logout
//                        .logoutUrl("/logout") // definiert den Logout-Endpunkt
//                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
//                )
//                .oidcLogout(Customizer.withDefaults());
//        return http.build();
//    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/public/**")
                        .filters(f -> f.setPath("/"))
                        .uri("http://httpbin.org:80"))  // Weiterleitung zu einem öffentlichen Testservice
                .route(r -> r.path("/api/private/**")
                        .filters(f -> f.tokenRelay())
                        .uri("http://resource:9000/api/private"))  // Weiterleitung zu einem geschützten Testservice
                .route(r -> r.path("/private/user")
                        .filters(f -> f.tokenRelay())
                        .uri("http://resource:9000/private/user"))  // Weiterleitung zu einem geschützten Testservice
                .build();
    }
}


package my.example.mygateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class TokenController {

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Value("${spring.security.oauth2.client.registration.gateway.authorization-grant-type}")
    private String grantType;

    private final WebClient webClient;

    public TokenController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://uaa:8080").build(); // Passe die URL für deinen UAA-Server an
    }

    @GetMapping("/opening")
    public Mono<String> defaultPage() {
        return Mono.just("Hier geht es los !!! Guten Tag Sie haben alles richtig gemacht");
    }

    @GetMapping
    public Mono<String> defaultPage(@AuthenticationPrincipal OidcUser principal) {
        String email = principal.getEmail();
        Map<String, Object> attributes = principal.getAttributes();
        return Mono.just("Hallo ::::  " + attributes);
    }

    @RequestMapping("/say-hello")
    public Mono<String> myLogout(ServerWebExchange exchange, @RequestParam String s) {
        return Mono.just("say ::::  " + s);
    }

    @GetMapping("/check")
    public Mono<String> checkAuthType(@AuthenticationPrincipal Object principal) {
        return Mono.just("AuthenticationPrincipal Type: " + principal.getClass().getName());
    }

    @GetMapping("/show-token")
    public Mono<String> showToken(@AuthenticationPrincipal DefaultOidcUser oidcUser) {
        if (oidcUser != null) {
            OidcIdToken idToken = oidcUser.getIdToken();
            if (idToken != null) {
                return Mono.just("Current ID Token: " + idToken.getTokenValue());
            } else {
                return Mono.just("No ID token found in DefaultOidcUser");
            }
        } else {
            return Mono.just("No DefaultOidcUser found in AuthenticationPrincipal");
        }
    }

    /**
     * 🌟 Für Client Credentials oder Resource Owner Password Grant:
     * Fordert ein Access Token direkt vom UAA-Server an.
     */
    @GetMapping("/get-token")
    public Mono<String> getToken() {
        if ("client_credentials".equals(grantType)) {
            return requestToken("client_credentials");
        }
        return Mono.just("Dieser Endpoint wird nur für Client Credentials benötigt.");
    }

    /**
     * Sendet eine Anfrage an den UAA-Server, um ein Access-Token zu erhalten.
     */
    private Mono<String> requestToken(String grantType) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", grantType);
        formData.add("client_id", "gateway");
        formData.add("client_secret", "secret");

        return webClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class);
    }

}

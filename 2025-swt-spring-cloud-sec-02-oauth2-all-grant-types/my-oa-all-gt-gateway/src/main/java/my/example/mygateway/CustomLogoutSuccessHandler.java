package my.example.mygateway;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.URI;

public class CustomLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private final RedirectServerLogoutSuccessHandler redirectHandler = new RedirectServerLogoutSuccessHandler();

    public CustomLogoutSuccessHandler() {
        this.redirectHandler.setLogoutSuccessUrl(URI.create("http://localhost:8080/opening"));
    }

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        // Zuerst die Cookies löschen
        clearCookies(exchange);

        // Anschließend die Weiterleitung ausführen
        return redirectHandler.onLogoutSuccess(exchange, authentication);
    }

    private void clearCookies(WebFilterExchange exchange) {
        ServerWebExchange webExchange = exchange.getExchange();
        webExchange.getResponse().getHeaders().add("Set-Cookie", "SESSION=; Max-Age=0; Path=/; HttpOnly");
        webExchange.getResponse().getHeaders().add("Set-Cookie", "JSESSIONID=; Max-Age=0; Path=/; HttpOnly");
        webExchange.getResponse().getHeaders().add("Set-Cookie", "Current-User=; Max-Age=0; Path=/; HttpOnly");
        webExchange.getResponse().getHeaders().add("Set-Cookie", "X-Uaa-Csrf=; Max-Age=0; Path=/; HttpOnly");
    }
}


package my.example.myresource;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Öffentliche Daten";
    }

    @GetMapping("/api/private")
    public String privateEndpoint() {
        return "Geschützte Daten";
    }

    @GetMapping("/api/private/2")
    public String privateEndpoint2() {
        return "Geschützte Daten 2";
    }

    @RequestMapping("/private/user")
    public String userEndpoint(HttpServletRequest request) {
            String method = request.getMethod();
            String requestURI = request.getRequestURI();
            String headers = request.getHeader("Authorization");
            return "Request Method: " + method + ", URI: " + requestURI + ", Some-Header: " + headers;
    }
}

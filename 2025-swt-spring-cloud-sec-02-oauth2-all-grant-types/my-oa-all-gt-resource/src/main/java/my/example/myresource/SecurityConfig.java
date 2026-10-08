package my.example.myresource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll()
                    // Authorization Code
                    .requestMatchers("/api/private/**", "/private/user", "/logout").hasAuthority("SCOPE_uaa.user")
                    // CLient Credentials
//                    .requestMatchers("/api/private/**").hasAuthority("SCOPE_resource.read") // 💡 Scope geändert
                    // Implict
//                    .requestMatchers("/api/private/**").hasAuthority("SCOPE_resource.write") // 💡 Scope geändert
                    .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));;
    return http.build();
  }
}
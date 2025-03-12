package CGI.flightApplication.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    configuration.setAllowCredentials(true); // Allow credentials for CORS
                    return configuration;
                }))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("api/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("api/flights/**").permitAll()
                        .requestMatchers("api/seats/book").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}

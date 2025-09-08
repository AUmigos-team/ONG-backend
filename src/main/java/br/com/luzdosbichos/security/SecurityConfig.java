package br.com.luzdosbichos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/ping",
                                "/actuator/health",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/storage/**",
                                "/actuator/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var c = new CorsConfiguration();
        c.setAllowedOrigins(List.of(
                "https://app.santuarioluzdosbichos.org",
                "http://localhost:4200"
        ));
        c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setAllowCredentials(true);

        var s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return s;
    }
}

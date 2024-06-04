package com.example.bookserver.config;



import com.example.bookserver.security.jwt.JwtTokenAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {


    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter) {
        this.jwtTokenAuthenticationFilter = jwtTokenAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS and disable CSRF
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                // Set permissions on endpoints
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/api/v1/books/public",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html"// Allow access to public endpoints
                                )
                                .permitAll()
                                .anyRequest() // Require authentication for other endpoints
                                .authenticated()
                )
                // Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                // Set unauthorized requests exception handler
                .exceptionHandling(
                        (exceptions) ->
                                exceptions

                                        .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                                        .accessDeniedHandler((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")))
                // Add JWT token filter
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }







    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }




}

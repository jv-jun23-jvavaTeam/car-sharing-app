package com.jvavateam.carsharingapp.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.jvavateam.carsharingapp.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private static final String AUTHENTICATION_ENDPOINT = "/auth/**";
    private static final String SWAGGER_UI_ENDPOINT = "/swagger-ui/**";
    private static final String SWAGGER_API_ENDPOINT = "/v3/api-docs/**";
    private static final String SUCCESS_PAYMENT_ENDPOINT = "/payments/success/**";
    private static final String CANCEL_PAYMENT_ENDPOINT = "/payments/cancel";
    private static final String HEALTH_CHECK_ENDPOINT = "/health/**";

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(AUTHENTICATION_ENDPOINT,
                                        SWAGGER_UI_ENDPOINT,
                                        SWAGGER_API_ENDPOINT,
                                        HEALTH_CHECK_ENDPOINT,
                                        SUCCESS_PAYMENT_ENDPOINT,
                                        CANCEL_PAYMENT_ENDPOINT)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .httpBasic(withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}

package com.jvavateam.carsharingapp.config;

import com.jvavateam.carsharingapp.model.User;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

@RequiredArgsConstructor
@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        Map<String, User> users = new HashMap<>();

        return users::get;
    }
}

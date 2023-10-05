package com.jvavateam.carsharingapp.config;

import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        Role customerRole = new Role();
        customerRole.setName(Role.RoleName.CUSTOMER);

        User oleh = new User();
        oleh.setId(100L);
        oleh.setEmail("wylo@ua.com");
        oleh.setPassword("$2a$12$2gWx8fCmINQ1EZ9cNrMG0.uNl7d63gmb/zTwj6yCdgsPXn5WD4tcW");
        oleh.setFirstName("Oleh");
        oleh.setLastName("Lyashko");
        oleh.setRoles(Set.of(customerRole));


        Role managerRole = new Role();
        managerRole.setName(Role.RoleName.MANAGER);

        User admin = new User();
        admin.setId(101L);
        admin.setEmail("super_manager@gmail.com");
        admin.setPassword("$2a$12$2gWx8fCmINQ1EZ9cNrMG0.uNl7d63gmb/zTwj6yCdgsPXn5WD4tcW");
        admin.setFirstName("Super");
        admin.setLastName("Manager");
        admin.setRoles(Set.of(managerRole));

        Map<String, User> users = Map.of(
                oleh.getEmail(), oleh,
                admin.getEmail(), admin
        );
        return users::get;
    }
}

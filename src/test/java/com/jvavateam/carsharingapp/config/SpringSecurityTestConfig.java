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

        User harry = new User();
        harry.setId(1L);
        harry.setEmail("harry@example.com");
        harry.setPassword("$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK");
        harry.setFirstName("Harry");
        harry.setLastName("Potter");
        harry.setRoles(Set.of(customerRole));

        User oleh = new User();
        oleh.setId(100L);
        oleh.setEmail("wylo@ua.com");
        oleh.setPassword("123456789");
        oleh.setFirstName("Oleh");
        oleh.setLastName("Lyashko");
        oleh.setRoles(Set.of(customerRole));


        Role managerRole = new Role();
        managerRole.setName(Role.RoleName.MANAGER);

        User admin = new User();
        admin.setId(3L);
        admin.setEmail("manager@gmail.com");
        admin.setPassword("$2a$12$2gWx8fCmINQ1EZ9cNrMG0.uNl7d63gmb/zTwj6yCdgsPXn5WD4tcW");
        admin.setFirstName("Super");
        admin.setLastName("Manager");
        admin.setRoles(Set.of(managerRole));

        Map<String, User> users = Map.of(
                harry.getEmail(), harry,
                oleh.getEmail(), oleh,
                admin.getEmail(), admin
        );
        return users::get;
    }
}

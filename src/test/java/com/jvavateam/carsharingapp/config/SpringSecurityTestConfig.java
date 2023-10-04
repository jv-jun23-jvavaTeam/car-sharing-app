package com.jvavateam.carsharingapp.config;

import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
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
        Role customer = new Role().setName(Role.RoleName.CUSTOMER);
        Role manager = new Role().setName(Role.RoleName.MANAGER);

        User john = new User()
                .setId(1L)
                .setEmail("john@mail.com")
                .setPassword("John1234")
                .setFirstName("John")
                .setLastName("Smith")
                .setDeleted(false)
                .setRoles(Set.of(customer));

        User alice = new User()
                .setId(1L)
                .setEmail("alice@mail.com")
                .setPassword("Alice1234")
                .setFirstName("Alice")
                .setLastName("Johnson")
                .setDeleted(false)
                .setRoles(Set.of(customer));

        User bob = new User()
                .setId(3L)
                .setEmail("bob@mail.com")
                .setPassword("Bob12345")
                .setFirstName("Bob")
                .setLastName("Dickson")
                .setDeleted(false)
                .setRoles(Set.of(manager));

        Map<String, User> users = Map.of(
                john.getEmail(), john,
                bob.getEmail(), bob,
                alice.getEmail(), alice
        );
        return users::get;
    }
}

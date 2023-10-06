package com.jvavateam.carsharingapp.repository;

import com.jvavateam.carsharingapp.config.SpringSecurityTestConfig;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(SpringSecurityTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    private static final User DENZEL = new User()
            .setEmail("Denzel@mail.com")
            .setPassword("Denzel123456")
            .setFirstName("Denzel")
            .setLastName("Denzel");
    private static final User OLEH = new User()
            .setId(100L)
            .setEmail("wylo@ua.com")
            .setPassword("$2a$12$2gWx8fCmINQ1EZ9cNrMG0.uNl7d63gmb/zTwj6yCdgsPXn5WD4tcW")
            .setFirstName("Oleh")
            .setLastName("Lyashko");
    private static final String INVALID_EMAIL = "invalid@mail.com";
    private static final String OLEH_EMAIL = "wylo@ua.com";
    private static final String CLEAR_USERS_TABLE =
            "classpath:database/user/delete-all-users.sql";
    private static final String ADD_USER =
            "classpath:database/user/add-sample-user-to-users-table.sql";
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Verify findByEmail() method works")
    @Sql(scripts = {
            CLEAR_USERS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findByEmail_ValidEmail_ReturnsOptionalOfUser() {
        userRepository.save(DENZEL);
        Optional<User> userFromDB = userRepository.findByEmail(DENZEL.getEmail());
        Assertions.assertTrue(userFromDB.isPresent());
        Assertions.assertEquals(userFromDB.get(), DENZEL);
    }

    @Test
    @DisplayName("Verify findByEmail() method doesn't work with invalid email")
    @Sql(scripts = {
            CLEAR_USERS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findByEmail_InValidEmail_ReturnsValidUser() {
        userRepository.save(DENZEL);
        Optional<User> userFromDB = userRepository.findByEmail(INVALID_EMAIL);
        Assertions.assertTrue(userFromDB.isEmpty());
    }

    @Test
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Verify getCurrentUser() method works")
    @Sql(scripts = {
            ADD_USER
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USERS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCurrentUser_ValidUser_ReturnsTrue() {
        User currentUser = userRepository.getCurrentUser();
        Assertions.assertEquals(currentUser, OLEH);
    }

    @Test
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Verify getCurrentUser() method doesn't work")
    @Sql(scripts = {
            ADD_USER
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USERS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCurrentUser_InValidUser_ReturnsFalse() {
        User currentUser = userRepository.getCurrentUser();
        Assertions.assertNotEquals(currentUser, DENZEL);
    }
}

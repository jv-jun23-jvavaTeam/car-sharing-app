package com.jvavateam.carsharingapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jvavateam.carsharingapp.config.SpringSecurityTestConfig;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(SpringSecurityTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class RentalRepositoryTest {
    private static final String OLEH_EMAIL = "wylo@ua.com";
    private static final LocalDate RENTAL_DATE = LocalDate.of(2023, 11, 5);
    private static final LocalDate RETURN_DATE = LocalDate.of(2023, 11, 20);
    private static final LocalDate ACTUAL_RETURN_DATE = LocalDate.of(2023, 11, 20);
    private static final Long CAR_ID = 100L;
    private static final Long USER_ID = 100L;
    private static final Long RENTAL_ID = 100L;

    private static final Car FOREIGN_KEY_CAR = new Car()
            .setId(CAR_ID)
            .setDeleted(false);
    private static final User FOREIGN_KEY_USER = new User()
            .setId(USER_ID)
            .setDeleted(false);

    private static final Rental TOYOTA_RENTAL = new Rental()
            .setId(RENTAL_ID)
            .setRentalDate(RENTAL_DATE)
            .setReturnDate(RETURN_DATE)
            .setActualReturnDate(ACTUAL_RETURN_DATE)
            .setCar(FOREIGN_KEY_CAR)
            .setUser(FOREIGN_KEY_USER)
            .setActive(true);

    private static final String ADD_TOYOTA_CAR =
            "classpath:database/car/add-toyota-car-to-cars-table.sql";
    private static final String ADD_USER =
            "classpath:database/user/add-sample-user-to-users-table.sql";
    private static final String ADD_FIRST_RENTAL =
            "classpath:database/rental/add-first-toyota-rental-to-rentals-table.sql";
    private static final String ADD_SECOND_RENTAL =
            "classpath:database/rental/add-second-rental-to-rentals-table.sql";
    private static final String CLEAR_RENTALS_TABLE =
            "classpath:database/rental/delete-all-rentals.sql";
    private static final String CLEAR_USERS_TABLE =
            "classpath:database/user/delete-all-users.sql";
    private static final String CLEAR_CARS_TABLE =
            "classpath:database/car/delete-all-cars.sql";
    private static final String CLEAR_ROLES_TABLE =
            "classpath:database/role/clear-roles-table.sql";
    private static final String INSERT_ROLES =
            "classpath:database/role/insert-roles.sql";
    private static final String CONNECT_USER_ROLE_TO_USER =
            "classpath:database/connect_user_role/connect-sample-user-role.sql";
    private static final String CLEAR_USER_ROLES =
            "classpath:database/connect_user_role/clear_user_roles_connection.sql";

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    @Sql(
            scripts = {
                    ADD_TOYOTA_CAR,
                    ADD_USER,
                    ADD_FIRST_RENTAL,
                    ADD_SECOND_RENTAL
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_RENTALS_TABLE,
                    CLEAR_USER_ROLES,
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Verify returned rental by id")
    public void getByIdForCurrentUser_validRentalId_shouldReturnUserRental() {
        Optional<Rental> rentalById = rentalRepository.getByIdForCurrentUser(RENTAL_ID);
        assertTrue(rentalById.isPresent());
        assertEquals(TOYOTA_RENTAL, rentalById.get());
    }

}

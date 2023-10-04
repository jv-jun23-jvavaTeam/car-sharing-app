package com.jvavateam.carsharingapp.repository;

import static com.jvavateam.carsharingapp.model.Role.RoleName.CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class RentalRepositoryTest {
    private static final LocalDate RENTAL_DATE = LocalDate.of(2023, 10, 3);
    private static final LocalDate RETURN_DATE = LocalDate.of(2023, 10, 18);
    private static final Long CAR_ID = 100L;
    private static final Long USER_ID = 100L;
    private static final Long RENTAL_ID = 100L;
    private static final String OLEH_EMAIL = "wylo@ua.com";

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
            .setCar(FOREIGN_KEY_CAR)
            .setUser(FOREIGN_KEY_USER)
            .setActive(true);

    private static final String ADD_TOYOTA_CAR =
            "classpath:database/car/add-100th-car-to-cars-table.sql";
    private static final String ADD_USER =
            "classpath:database/user/add-100th-user-to-users-table.sql";
    private static final String ADD_RENTAL =
            "classpath:database/rental/add-100th-rental-to-rentals-table.sql";
    private static final String CLEAR_RENTAL_TABLE =
            "classpath:database/rental/remove-100th-rental-from-rentals-table.sql.sql";
    private static final String CLEAR_USER_TABLE =
            "classpath:database/user/remove-100th-user-from-users-table.sql";
    private static final String CLEAR_CAR_TABLE =
            "classpath:database/car/remove-100th-car-from-cars-table.sql";

    @Autowired
    private RentalRepository rentalRepository;


    @Test
    @DisplayName("Verify returned rental by id")
    @Sql(
            scripts = {
                    ADD_TOYOTA_CAR,
                    ADD_USER,
                    ADD_RENTAL
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_RENTAL_TABLE,
                    CLEAR_USER_TABLE,
                    CLEAR_CAR_TABLE
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails("wylo@ua.com")
    public void getByIdForCurrentUser_validRentalId_shouldReturnUserRental() {
        Optional<Rental> rentalById = rentalRepository.getByIdForCurrentUser(RENTAL_ID);
        assertTrue(rentalById.isPresent());
        assertEquals(TOYOTA_RENTAL, rentalById.get());
    }

}

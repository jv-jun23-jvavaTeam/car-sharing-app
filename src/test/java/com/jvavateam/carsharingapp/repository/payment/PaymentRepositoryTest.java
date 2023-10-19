package com.jvavateam.carsharingapp.repository.payment;

import com.jvavateam.carsharingapp.config.SpringSecurityTestConfig;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Payment;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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
class PaymentRepositoryTest {
    private static final String CUSTOMER = "wylo@ua.com";
    private static final String MANAGER = "manager@gmail.com";
    private static final String INSERT_CUSTOMER_DATA =
            "classpath:database/user/add-sample-user-to-users-table.sql";
    private static final String INSERT_USER_ROLES_DATA =
            "classpath:database/connect_user_role/connect-sample-user-role.sql";
    private static final String INSERT_MANAGER_ROLES_DATA =
            "classpath:database/connect_user_role/connect-sample-manager-role.sql";

    private static final String INSERT_CAR_DATA =
            "classpath:database/car/add-toyota-car-to-cars-table.sql";
    private static final String INSERT_RENTAL_ONE_DATA =
            "classpath:database/rental/add-first-toyota-rental-to-rentals-table.sql";
    private static final String INSERT_RENTAL_TWO_DATA =
            "classpath:database/rental/add-second-rental-to-rentals-table.sql";

    private static final String INSERT_PAYMENT_UNPAID_DATA =
            "classpath:database/payment/add-first-payment.sql";
    private static final String INSERT_PAYMENT_PAID_DATA =
            "classpath:database/payment/add-second-payment.sql";

    private static final String DELETE_CUSTOMER_DATA =
            "classpath:database/user/remove-sample-user-from-users-table.sql";
    private static final String DELETE_USER_ROLES_DATA =
            "classpath:database/connect_user_role/clear_user_roles_connection.sql";

    private static final String DELETE_CAR_DATA =
            "classpath:database/car/remove-toyota-car-from-cars-table.sql";

    private static final String DELETE_RENTALS_DATA =
            "classpath:database/rental/delete-all-rentals.sql";

    private static final String DELETE_PAYMENT_DATA =
            "classpath:database/payment/delete-all-from-payments.sql";

    private static final String VALID_SESSION_ID =
            "VALID_SESSION_ID";
    private static final String VALID_SESSION_URL =
            "VALID_SESSION_URL";
    private static final User VALID_USER_CUSTOMER = new User()
            .setId(100L)
            .setEmail("wylo@ua.com")
            .setPassword("Oleh")
            .setLastName("Lyashko")
            .setPassword("$2a$12$2gWx8fCmINQ1EZ9cNrMG0.uNl7d63gmb/zTwj6yCdgsPXn5WD4tcW")
            .setRoles(Set.of(new Role().setId(2L).setName(Role.RoleName.CUSTOMER)))
            .setDeleted(false);

    private static final Car VALID_CAR = new Car()
            .setId(100L)
            .setModel("Toyota")
            .setModel("Camry")
            .setType(Car.Type.SEDAN)
            .setInventory(11)
            .setDailyFee(BigDecimal.valueOf(8000,2))
            .setDeleted(false);

    private static final Rental VALID_RENTAL = new Rental()
            .setId(100L)
            .setCar(VALID_CAR)
            .setUser(VALID_USER_CUSTOMER)
            .setRentalDate(LocalDate.of(2023,11,05))
            .setReturnDate(LocalDate.of(2023,11,20))
            .setActive(true)
            .setIsDeleted(false);
    private static final Rental VALID_RENTAL_TWO = new Rental()
            .setId(101L)
            .setCar(VALID_CAR)
            .setUser(VALID_USER_CUSTOMER)
            .setRentalDate(LocalDate.of(2023,11,05))
            .setReturnDate(LocalDate.of(2023,11,20))
            .setActive(true)
            .setIsDeleted(false);

    private static final Payment PAYMENT_UNPAID =
            new Payment()
                    .setId(1L)
                    .setStatus(Payment.Status.PENDING)
                    .setType(Payment.Type.PAYMENT)
                    .setSessionId(VALID_SESSION_ID)
                    .setSessionUrl(VALID_SESSION_URL)
                    .setRental(VALID_RENTAL)
                    .setAmountToPay(BigDecimal.valueOf(40000, 2))
                    .setDeleted(false);

    private static final Payment PAYMENT_PAID =
            new Payment()
                    .setId(2L)
                    .setStatus(Payment.Status.PAID)
                    .setType(Payment.Type.PAYMENT)
                    .setSessionId(VALID_SESSION_ID + "2")
                    .setSessionUrl(VALID_SESSION_URL + "2")
                    .setRental(VALID_RENTAL_TWO)
                    .setAmountToPay(BigDecimal.valueOf(40000,2))
                    .setDeleted(false);

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("Verify retrieving all payments by status for current user")
    @WithUserDetails(CUSTOMER)
    @Sql(scripts = {
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA, INSERT_CAR_DATA,
            INSERT_RENTAL_ONE_DATA, INSERT_RENTAL_TWO_DATA, INSERT_PAYMENT_UNPAID_DATA,
            INSERT_PAYMENT_PAID_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_PAYMENT_DATA, DELETE_RENTALS_DATA, DELETE_CUSTOMER_DATA,
            DELETE_USER_ROLES_DATA, DELETE_CAR_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByStatus_ValidStatus_ReturnListOfPayments() {

        List<Payment> actualSuccessful = paymentRepository.findAllByStatus(Payment.Status.PAID);
        List<Payment> actualPaused = paymentRepository.findAllByStatus(Payment.Status.PENDING);

        Assertions.assertEquals(List.of(PAYMENT_PAID), actualSuccessful);
        Assertions.assertEquals(List.of(PAYMENT_UNPAID), actualPaused);
    }

    @Test
    @DisplayName("Verify retrieving all payments for current user")
    @WithUserDetails(CUSTOMER)
    @Sql(scripts = {
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA, INSERT_CAR_DATA,
            INSERT_RENTAL_ONE_DATA, INSERT_RENTAL_TWO_DATA, INSERT_PAYMENT_UNPAID_DATA,
            INSERT_PAYMENT_PAID_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_PAYMENT_DATA, DELETE_RENTALS_DATA, DELETE_CUSTOMER_DATA,
            DELETE_USER_ROLES_DATA, DELETE_CAR_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllForCurrentUser_PaymentsExist_ReturnListOfPayments() {

        List<Payment> actual = paymentRepository.findAllForCurrentUser();

        Assertions.assertEquals(List.of(PAYMENT_UNPAID, PAYMENT_PAID), actual);
    }

    @Test
    @DisplayName("Verify retrieving all payments for certain user by id")
    @WithUserDetails(MANAGER)
    @Sql(scripts = {
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA, INSERT_CAR_DATA,
            INSERT_RENTAL_ONE_DATA, INSERT_RENTAL_TWO_DATA, INSERT_PAYMENT_UNPAID_DATA,
            INSERT_PAYMENT_PAID_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_PAYMENT_DATA, DELETE_RENTALS_DATA, DELETE_CUSTOMER_DATA,
            DELETE_USER_ROLES_DATA, DELETE_CAR_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllForCertainUser_ExistingId_ReturnListOfPayments() {

        List<Payment> actual = paymentRepository
                .findAllByRentalUserId(VALID_USER_CUSTOMER.getId());

        Assertions.assertEquals(List.of(PAYMENT_UNPAID, PAYMENT_PAID), actual);
    }
}

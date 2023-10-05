package com.jvavateam.carsharingapp.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {
    protected static MockMvc mockMvc;
    private static final String CUSTOMER = "wylo@ua.com";
    private static final String MANAGER = "manager@gmail.com";

    private static final String INSERT_CUSTOMER_DATA =
            "classpath:database/user/add-sample-user-to-users-table.sql";
    private static final String INSERT_MANAGER_DATA =
            "classpath:database/connect_user_role/connect-sample-manager-role.sql";

    private static final String INSERT_MANAGER_ROLES_DATA =
            "classpath:database/user/add-manager-to-users-table.sql";

    private static final String INSERT_USER_ROLES_DATA =
            "classpath:database/connect_user_role/connect-sample-user-role.sql";

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

    private static final String DELETE_MANAGER_DATA =
            "classpath:database/user/remove-manager-from-users-table.sql";


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
    private static final CreatePaymentRequestDto CREATE_PAYMENT_DTO =
            new CreatePaymentRequestDto(
                    100L,
                    "PAYMENT"
            );
    private static final PaymentResponseDto PAYMENT_RESPONSE_DTO_UNPAID_PAYMENT =
            new PaymentResponseDto(
                    1L,
                    "PENDING",
                    "PAYMENT",
                    VALID_SESSION_URL,
                    VALID_SESSION_ID,
                    VALID_RENTAL.getId(),
                    BigDecimal.valueOf(40000,2)
            );
    private static final PaymentResponseDto PAYMENT_RESPONSE_DTO_PAID_PAYMENT =
            new PaymentResponseDto(
                    2L,
                    "PAID",
                    "PAYMENT",
                    VALID_SESSION_URL,
                    VALID_SESSION_ID,
                    VALID_RENTAL_TWO.getId(),
                    BigDecimal.valueOf(40000,2)
            );
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Sql(scripts = {
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_CUSTOMER_DATA, DELETE_USER_ROLES_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(CUSTOMER)
    @DisplayName("Verify creation of a new payment session")
    void createPayment_InvalidRequestDto_ThrowsException() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(CREATE_PAYMENT_DTO);
        mockMvc.perform(post("/payments")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    @Sql(scripts = {
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA, INSERT_CAR_DATA,
            INSERT_RENTAL_ONE_DATA, INSERT_RENTAL_TWO_DATA, INSERT_PAYMENT_UNPAID_DATA,
            INSERT_PAYMENT_PAID_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_PAYMENT_DATA, DELETE_RENTALS_DATA, DELETE_CUSTOMER_DATA,
            DELETE_USER_ROLES_DATA, DELETE_CAR_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(CUSTOMER)
    @DisplayName("Verify getting all of current user's payments")
    void getAllForCurrentUser_ReturnsListOfPayments() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/payments")
                        .content(objectMapper.writeValueAsString(Pageable.ofSize(20)))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<PaymentResponseDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), PaymentResponseDto[].class));
        boolean allExpectedFound = List.of(PAYMENT_RESPONSE_DTO_UNPAID_PAYMENT,
                        PAYMENT_RESPONSE_DTO_PAID_PAYMENT).stream()
                .allMatch(expected -> actual.stream()
                        .anyMatch(actualFound ->
                                expected.id().equals(actualFound.id())));
        assertTrue(allExpectedFound,
                "Not all expected payments were found in actual list.");
    }

    @Test
    @Sql(scripts = {
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA, INSERT_CAR_DATA,
            INSERT_RENTAL_ONE_DATA, INSERT_RENTAL_TWO_DATA, INSERT_PAYMENT_UNPAID_DATA,
            INSERT_PAYMENT_PAID_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_PAYMENT_DATA, DELETE_RENTALS_DATA, DELETE_CUSTOMER_DATA,
            DELETE_USER_ROLES_DATA, DELETE_CAR_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("super_manager@gmail.com")
    @DisplayName("Verify getting all of certain user's payments")
    void getAllForCertainUser_ReturnsListOfPayments() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/payments/" + VALID_USER_CUSTOMER.getId())
                        .content(objectMapper.writeValueAsString(Pageable.ofSize(20)))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<PaymentResponseDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), PaymentResponseDto[].class));
        boolean allExpectedFound = List.of(PAYMENT_RESPONSE_DTO_UNPAID_PAYMENT,
                        PAYMENT_RESPONSE_DTO_PAID_PAYMENT).stream()
                .allMatch(expected -> actual.stream()
                        .anyMatch(actualFound ->
                                expected.id().equals(actualFound.id())));
        assertTrue(allExpectedFound,
                "Not all expected payments were found in actual list.");
    }

    @Test
    @Sql(scripts = {
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA, INSERT_CAR_DATA,
            INSERT_RENTAL_ONE_DATA, INSERT_RENTAL_TWO_DATA, INSERT_PAYMENT_UNPAID_DATA,
            INSERT_PAYMENT_PAID_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_PAYMENT_DATA, DELETE_RENTALS_DATA, DELETE_CUSTOMER_DATA,
            DELETE_USER_ROLES_DATA, DELETE_CAR_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(CUSTOMER)
    @DisplayName("Verify getting all of current user's successful payments")
    void getAllSuccessfulPayments_ReturnsListOfPayments() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/payments/successful")
                        .content(objectMapper.writeValueAsString(Pageable.ofSize(20)))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<PaymentResponseDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), PaymentResponseDto[].class));
        boolean allExpectedFound = List.of(PAYMENT_RESPONSE_DTO_PAID_PAYMENT).stream()
                .allMatch(expected -> actual.stream()
                        .anyMatch(actualFound ->
                                expected.id().equals(actualFound.id())));
        assertTrue(allExpectedFound,
                "Not all successful payments were found in actual list.");
    }

    @Test
    @Sql(scripts = {INSERT_MANAGER_DATA,INSERT_MANAGER_ROLES_DATA,
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA, INSERT_CAR_DATA,
            INSERT_RENTAL_ONE_DATA, INSERT_RENTAL_TWO_DATA, INSERT_PAYMENT_UNPAID_DATA,
            INSERT_PAYMENT_PAID_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_PAYMENT_DATA, DELETE_RENTALS_DATA, DELETE_CUSTOMER_DATA,
            DELETE_MANAGER_DATA, DELETE_USER_ROLES_DATA, DELETE_CAR_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(CUSTOMER)
    @DisplayName("Verify getting all of current user's successful payments")
    void getAllPausedPayments_ReturnsListOfPayments() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/payments/paused")
                        .content(objectMapper.writeValueAsString(Pageable.ofSize(20)))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<PaymentResponseDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), PaymentResponseDto[].class));
        boolean allExpectedFound = List.of(PAYMENT_RESPONSE_DTO_UNPAID_PAYMENT).stream()
                .allMatch(expected -> actual.stream()
                        .anyMatch(actualFound ->
                                expected.id().equals(actualFound.id())));
        assertTrue(allExpectedFound,
                "Not all paused payments were found in actual list.");
    }

    @Test
    @Sql(scripts = {
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA, INSERT_CAR_DATA,
            INSERT_RENTAL_ONE_DATA, INSERT_RENTAL_TWO_DATA, INSERT_PAYMENT_UNPAID_DATA,
            INSERT_PAYMENT_PAID_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_PAYMENT_DATA, DELETE_RENTALS_DATA, DELETE_CUSTOMER_DATA,
            DELETE_USER_ROLES_DATA, DELETE_CAR_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(CUSTOMER)
    @DisplayName("Verify getting successful message on redirection successful page")
    public void successful_ReturnsSuccessfulMessage() throws Exception {
        String sessionId = "VALID_SESSION_ID";

        mockMvc.perform(get("/payments/success/")
                        .param("sessionId", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Payment " + sessionId + " successfully provided!"));
    }

    @Test
    @Sql(scripts = {
            INSERT_CUSTOMER_DATA, INSERT_USER_ROLES_DATA, INSERT_CAR_DATA,
            INSERT_RENTAL_ONE_DATA, INSERT_RENTAL_TWO_DATA, INSERT_PAYMENT_UNPAID_DATA,
            INSERT_PAYMENT_PAID_DATA
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_PAYMENT_DATA, DELETE_RENTALS_DATA, DELETE_CUSTOMER_DATA,
            DELETE_USER_ROLES_DATA, DELETE_CAR_DATA
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails(CUSTOMER)
    @DisplayName("Verify getting cancel message on redirection cancel page")
    public void cancel_ReturnsCancelMessage() throws Exception {
        mockMvc.perform(get("/payments/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Payment cancelled! You can try again later. "
                                + "The session will be open for 24hours."));
    }
}

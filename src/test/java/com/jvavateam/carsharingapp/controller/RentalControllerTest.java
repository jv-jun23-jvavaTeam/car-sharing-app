package com.jvavateam.carsharingapp.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvavateam.carsharingapp.config.SecurityConfig;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalByManagerDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SecurityConfig.class)
public class RentalControllerTest {
    protected static MockMvc mockMvc;
    private static final String OLEH_EMAIL = "wylo@ua.com";
    private static final String MANAGER_EMAIL = "super_manager@gmail.com";
    private static final LocalDate RENTAL_DATE = LocalDate.of(2023, 11, 5);
    private static final LocalDate RETURN_DATE = LocalDate.of(2023, 11, 20);
    private static final LocalDate ACTUAL_RETURN_DATE = LocalDate.now();
    private static final Long CAR_ID = 100L;
    private static final Long USER_ID = 100L;
    private static final Long RENTAL_ID = 100L;
    private static final Long SECOND_RENTAL_ID = 101L;
    private static final Car FOREIGN_KEY_CAR = new Car()
            .setId(CAR_ID)
            .setDeleted(false);
    private static final User FOREIGN_KEY_USER = new User()
            .setId(USER_ID)
            .setDeleted(false);

    static final CreateRentalDto REQUEST_CREATE_RENTAL_DTO = new CreateRentalDto(
            RENTAL_DATE,
            RETURN_DATE,
            CAR_ID
    );

    static final CreateRentalByManagerDto REQUEST_CREATE_RENTAL_BY_MANAGER_DTO =
            new CreateRentalByManagerDto(
                    RENTAL_DATE,
                    RETURN_DATE,
                    CAR_ID,
                    USER_ID
            );

    private static final Rental TOYOTA_RENTAL = new Rental()
            .setId(RENTAL_ID)
            .setRentalDate(RENTAL_DATE)
            .setReturnDate(RETURN_DATE)
            .setCar(FOREIGN_KEY_CAR)
            .setUser(FOREIGN_KEY_USER)
            .setActive(true);
    private static final Rental SECOND_RENTAL = new Rental()
            .setId(SECOND_RENTAL_ID)
            .setRentalDate(RENTAL_DATE)
            .setReturnDate(RETURN_DATE)
            .setCar(FOREIGN_KEY_CAR)
            .setUser(FOREIGN_KEY_USER)
            .setActive(true);

    private static final RentalResponseDto RESPONSE_CREATED_RENTAL_DTO = new RentalResponseDto(
            TOYOTA_RENTAL.getId(),
            RENTAL_DATE,
            RETURN_DATE,
            CAR_ID,
            USER_ID,
            TOYOTA_RENTAL.isActive()
    );

    private static final RentalResponseDto SECOND_RENTAL_DTO = new RentalResponseDto(
            SECOND_RENTAL.getId(),
            RENTAL_DATE,
            RETURN_DATE,
            CAR_ID,
            USER_ID,
            SECOND_RENTAL.isActive()
    );

    private static final List<RentalResponseDto> REPOSITORY_RENTALS_DTO =
            List.of(RESPONSE_CREATED_RENTAL_DTO, SECOND_RENTAL_DTO);
    private static final RentalSearchParameters SEARCH_PARAMS = new RentalSearchParameters(
            USER_ID,
            true
    );

    private static final RentalReturnResponseDto RENTAL_RETURN_RESPONSE_DTO =
            new RentalReturnResponseDto(
                    TOYOTA_RENTAL.getId(),
                    RENTAL_DATE,
                    RETURN_DATE,
                    ACTUAL_RETURN_DATE,
                    CAR_ID,
                    USER_ID,
                    false
            );

    private static final String ADD_TOYOTA_CAR =
            "classpath:database/car/add-toyota-car-to-cars-table.sql";
    private static final String ADD_USER =
            "classpath:database/user/add-sample-user-to-users-table.sql";
    private static final String ADD_MANAGER =
            "classpath:database/user/add-manager-to-users-table.sql";
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
    private static final String CONNECT_MANAGER_ROLE_TO_MANAGER =
            "classpath:database/connect_user_role/connect-sample-manager-role.sql";
    private static final String CLEAR_USER_ROLES =
            "classpath:database/connect_user_role/clear_user_roles_connection.sql";

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
    @Sql(
            scripts = {
                    ADD_USER,
                    INSERT_ROLES,
                    CONNECT_USER_ROLE_TO_USER,
                    ADD_TOYOTA_CAR
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_RENTALS_TABLE,
                    CLEAR_USER_ROLES,
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_ROLES_TABLE,
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Creating rental with valid dto. Ok Status and response DTO expected")
    void create_withValidCreateDto_returnCreatedRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/rentals")
                        .content(objectMapper.writeValueAsString(REQUEST_CREATE_RENTAL_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        RentalResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(), RentalResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(RESPONSE_CREATED_RENTAL_DTO, actual, "id"));
    }

    @Test
    @Sql(
            scripts = {
                    ADD_USER,
                    ADD_MANAGER,
                    INSERT_ROLES,
                    CONNECT_USER_ROLE_TO_USER,
                    CONNECT_MANAGER_ROLE_TO_MANAGER,
                    ADD_TOYOTA_CAR
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_RENTALS_TABLE,
                    CLEAR_USER_ROLES,
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_ROLES_TABLE,
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(MANAGER_EMAIL)
    @DisplayName("Creating rental by manager with valid dto. Ok Status and response DTO expected")
    void createByManager_withValidCreateDto_returnCreatedRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/rentals/manager")
                        .content(objectMapper
                                .writeValueAsString(REQUEST_CREATE_RENTAL_BY_MANAGER_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        RentalResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(), RentalResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(RESPONSE_CREATED_RENTAL_DTO, actual, "id"));
    }

    @Test
    @Sql(
            scripts = {
                    ADD_TOYOTA_CAR,
                    INSERT_ROLES,
                    ADD_MANAGER,
                    CONNECT_MANAGER_ROLE_TO_MANAGER,
                    ADD_USER,
                    CONNECT_USER_ROLE_TO_USER,
                    ADD_FIRST_RENTAL,
                    ADD_SECOND_RENTAL
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_RENTALS_TABLE,
                    CLEAR_USER_ROLES,
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_ROLES_TABLE,
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(MANAGER_EMAIL)
    @DisplayName("Getting rental list with valid search params. "
            + "Ok Status and list response DTO expected")
    void getAllByManager_withValidSearchParams_returnListRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rentals/manager")
                        .content(objectMapper.writeValueAsString(SEARCH_PARAMS))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<RentalResponseDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), RentalResponseDto[].class));
        boolean allExpectedRentalsFounded = REPOSITORY_RENTALS_DTO.stream()
                .allMatch(expectedRentals -> actual.stream()
                        .anyMatch(actualRentals -> expectedRentals.id()
                                .equals(actualRentals.id())));
        assertTrue(allExpectedRentalsFounded, "Not all expected books were found in actual list.");
    }

    @Test
    @Sql(
            scripts = {
                    INSERT_ROLES,
                    ADD_TOYOTA_CAR,
                    ADD_USER,
                    CONNECT_USER_ROLE_TO_USER,
                    ADD_FIRST_RENTAL,
                    ADD_SECOND_RENTAL
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_RENTALS_TABLE,
                    CLEAR_USER_ROLES,
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_ROLES_TABLE,
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Getting rental list with authorized user."
            + " Ok Status and list response DTO expected")
    void getAll_withAuthenticatedUser_returnListRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rentals")
                        .content(objectMapper.writeValueAsString(Pageable.ofSize(20)))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<RentalResponseDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), RentalResponseDto[].class));
        boolean allExpectedRentalsFounded = REPOSITORY_RENTALS_DTO.stream()
                .allMatch(expectedRentals -> actual.stream()
                        .anyMatch(actualRentals ->
                                expectedRentals.id().equals(actualRentals.id())));
        assertTrue(allExpectedRentalsFounded,
                "Not all expected books were found in actual list.");
    }

    @Test
    @Sql(
            scripts = {
                    INSERT_ROLES,
                    ADD_TOYOTA_CAR,
                    ADD_USER,
                    CONNECT_USER_ROLE_TO_USER,
                    ADD_FIRST_RENTAL,
                    ADD_SECOND_RENTAL
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_RENTALS_TABLE,
                    CLEAR_USER_ROLES,
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_ROLES_TABLE,
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Getting rental list with authorized user. "
            + "Ok Status and list response DTO expected")
    void get_withAuthenticatedUser_returnRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/rentals/" + RENTAL_ID)).andReturn();
        RentalResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(), RentalResponseDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(RESPONSE_CREATED_RENTAL_DTO, actual, "id"));
    }

    @Test
    @Sql(
            scripts = {
                    ADD_TOYOTA_CAR,
                    ADD_USER,
                    CONNECT_USER_ROLE_TO_USER,
                    ADD_FIRST_RENTAL,
                    ADD_SECOND_RENTAL
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_RENTALS_TABLE,
                    CLEAR_USER_ROLES,
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_ROLES_TABLE,
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Completing rental with authorized user. "
            + "Ok Status and response return DTO expected")
    void updateReturnDate_withAuthenticatedUser_returnListRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/rentals/" + RENTAL_ID + "/return"))
                .andReturn();
        RentalReturnResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse()
                        .getContentAsString(), RentalReturnResponseDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(RENTAL_RETURN_RESPONSE_DTO, actual, "id"));
        assertFalse(actual.isActive());
    }
}

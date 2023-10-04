package com.jvavateam.carsharingapp.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.DispatcherType;
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
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private static final LocalDate RENTAL_DATE = LocalDate.of(2024, 10, 4);
    private static final LocalDate RETURN_DATE = LocalDate.of(2024, 10, 18);
    private static final LocalDate ACTUAL_RETURN_DATE = LocalDate.of(2024, 10, 18);
    private static final Long CAR_ID = 100L;
    private static final Long USER_ID = 100L;
    private static final Long MANAGER_ID = 101L;
    private static final Long RENTAL_ID = 100L;
    private static final Long SECOND_RENTAL_ID = 101L;

    private static final Car CAR = new Car()
            .setId(1L)
            .setModel("Toyota Camry")
            .setBrand("Toyota")
            .setInventory(11)
            .setDailyFee(new BigDecimal("80.00"))
            .setType(Car.Type.SEDAN)
            .setDeleted(false);
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
                    MANAGER_ID
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
    private static final List<Rental> REPOSITORY_RENTALS =
            List.of(TOYOTA_RENTAL, SECOND_RENTAL);

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

    private static final Pageable DEFAULT_PAGEABLE = Pageable.ofSize(20);
    private static final Page<Rental> REPOSITORY_PAGE =
            new PageImpl<>(REPOSITORY_RENTALS, DEFAULT_PAGEABLE, REPOSITORY_RENTALS.size());
    private final Specification<Rental> SEARCH_SPECIFICATION = (root, query, criteriaBuilder) -> {
        Long userId = SEARCH_PARAMS.userId();
        boolean isActive = SEARCH_PARAMS.isActive();
        List<Predicate> predicates = new ArrayList<>();
        if (userId != null) {
            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
        }
        if (isActive) {
            predicates.add(criteriaBuilder.isTrue(root.get("active")));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    private static final Rental SAVED_RETURN_SECOND_RENTAL = new Rental()
            .setId(SECOND_RENTAL_ID)
            .setRentalDate(RENTAL_DATE)
            .setReturnDate(RETURN_DATE)
            .setActualReturnDate(LocalDate.now())
            .setCar(FOREIGN_KEY_CAR)
            .setUser(FOREIGN_KEY_USER)
            .setActive(false);
    private static final String ADD_TOYOTA_CAR =
            "classpath:database/car/add-100th-car-to-cars-table.sql";
    private static final String ADD_100TH_USER =
            "classpath:database/user/add-100th-user-to-users-table.sql";
    private static final String ADD_101_MANAGER =
            "classpath:database/user/add-101th-manager-to-users-table.sql";

    private static final String ADD_100TH_RENTAL =
            "classpath:database/rental/add-100th-rental-to-rentals-table.sql";
    private static final String ADD_101TH_RENTAL =
            "classpath:database/rental/add-101th-rental-to-rentals-table.sql";
    private static final String REMOVE_100TH_RENTAL =
            "classpath:database/rental/remove-101th-rental-from-rentals-table.sql";
    private static final String REMOVE_101TH_RENTAL =
            "classpath:database/rental/remove-101th-rental-from-rentals-table.sql";
    private static final String REMOVE_100TH_USER_FROM_TABLE =
            "classpath:database/user/remove-100th-user-from-users-table.sql";
    private static final String REMOVE_101TH_MANAGER_FROM_TABLE =
            "classpath:database/user/add-101th-manager-to-users-table.sql";
    private static final String CLEAR_CAR_TABLE =
            "classpath:database/car/remove-100th-car-from-cars-table.sql";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(
            scripts = {
                    ADD_100TH_USER,
                    ADD_TOYOTA_CAR
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    REMOVE_100TH_RENTAL,
                    CLEAR_CAR_TABLE,
                    REMOVE_100TH_USER_FROM_TABLE
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
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RentalResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(RESPONSE_CREATED_RENTAL_DTO, actual, "id"));
    }

    @Test
    @Sql(
            scripts = {
                    ADD_101_MANAGER,
                    ADD_TOYOTA_CAR
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    REMOVE_100TH_RENTAL,
                    CLEAR_CAR_TABLE,
                    REMOVE_101TH_MANAGER_FROM_TABLE
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(MANAGER_EMAIL)
    @DisplayName("Creating rental with valid dto. Ok Status and response DTO expected")
    void createByManager_withValidCreateDto_returnCreatedRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/rentals/manager")
                        .content(objectMapper.writeValueAsString(REQUEST_CREATE_RENTAL_BY_MANAGER_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        RentalResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RentalResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(RESPONSE_CREATED_RENTAL_DTO, actual, "id"));
    }

    @Test
    @Sql(
            scripts = {
                    ADD_TOYOTA_CAR,
                    ADD_100TH_USER,
                    ADD_100TH_RENTAL,
                    ADD_101TH_RENTAL
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    REMOVE_100TH_RENTAL,
                    REMOVE_101TH_RENTAL,
                    CLEAR_CAR_TABLE,
                    REMOVE_101TH_MANAGER_FROM_TABLE
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(MANAGER_EMAIL)
    @DisplayName("Getting rental list with valid search params. Ok Status and list response DTO expected")
    void getAllByManager_withValidSearchParams_returnListRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rentals")
                        .content(objectMapper.writeValueAsString(SEARCH_PARAMS))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        List<RentalResponseDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), RentalResponseDto[].class));
        boolean allExpectedRentalsFounded = REPOSITORY_RENTALS_DTO.stream()
                .allMatch(expectedRentals -> actual.stream()
                        .anyMatch(actualRentals -> expectedRentals.id().equals(actualRentals.id())));
        assertTrue(allExpectedRentalsFounded, "Not all expected books were found in actual list.");
    }

    @Test
    @Sql(
            scripts = {
                    ADD_TOYOTA_CAR,
                    ADD_100TH_USER,
                    ADD_100TH_RENTAL,
                    ADD_101TH_RENTAL
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    REMOVE_100TH_RENTAL,
                    REMOVE_101TH_RENTAL,
                    CLEAR_CAR_TABLE,
                    REMOVE_101TH_MANAGER_FROM_TABLE
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Getting rental list with authorized user. Ok Status and list response DTO expected")
    void getAll_withAuthenticatedUser_returnListRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rentals")
                        .content(objectMapper.writeValueAsString(Pageable.ofSize(20)))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        List<RentalResponseDto> actual = Arrays.asList(objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), RentalResponseDto[].class));
        boolean allExpectedRentalsFounded = REPOSITORY_RENTALS_DTO.stream()
                .allMatch(expectedRentals -> actual.stream()
                        .anyMatch(actualRentals -> expectedRentals.id().equals(actualRentals.id())));
        assertTrue(allExpectedRentalsFounded, "Not all expected books were found in actual list.");
    }

    @Test
    @Sql(
            scripts = {
                    ADD_TOYOTA_CAR,
                    ADD_100TH_USER,
                    ADD_100TH_RENTAL,
                    ADD_101TH_RENTAL
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    REMOVE_100TH_RENTAL,
                    REMOVE_101TH_RENTAL,
                    CLEAR_CAR_TABLE,
                    REMOVE_101TH_MANAGER_FROM_TABLE
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Getting rental list with authorized user. Ok Status and list response DTO expected")
    void get_withAuthenticatedUser_returnRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/rentals/" + RENTAL_ID)).andReturn();
        RentalResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RentalResponseDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(RESPONSE_CREATED_RENTAL_DTO, actual, "id"));
    }
    @Test
    @Sql(
            scripts = {
                    ADD_TOYOTA_CAR,
                    ADD_100TH_USER,
                    ADD_100TH_RENTAL,
                    ADD_101TH_RENTAL
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    REMOVE_100TH_RENTAL,
                    REMOVE_101TH_RENTAL,
                    CLEAR_CAR_TABLE,
                    REMOVE_101TH_MANAGER_FROM_TABLE
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithUserDetails(OLEH_EMAIL)
    @DisplayName("Completing rental with authorized user. Ok Status and response return DTO expected")
    void updateReturnDate_withAuthenticatedUser_returnListRentalDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/" + RENTAL_ID + "/return")).andReturn();
        RentalReturnResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RentalReturnResponseDto.class);
        assertTrue(EqualsBuilder.reflectionEquals(RESPONSE_CREATED_RENTAL_DTO, actual, "id"));
        assertFalse(actual.isActive());
    }
}

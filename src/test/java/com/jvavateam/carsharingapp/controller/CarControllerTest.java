package com.jvavateam.carsharingapp.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvavateam.carsharingapp.config.SecurityConfig;
import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
import com.jvavateam.carsharingapp.model.Car;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SecurityConfig.class)
public class CarControllerTest {
    protected static MockMvc mockMvc;
    private static final String USER_EMAIL = "wylo@ua.com";
    private static final String MANAGER_EMAIL = "super_manager@gmail.com";
    private static final Long TOYOTA_CAR_ID = 100L;
    private static final String TOYOTA_BRAND = "Toyota";
    private static final String TOYOTA_MODEL = "Toyota Camry";
    private static final int TOYOTA_INVENTORY = 11;
    private static final BigDecimal TOYOTA_DAILY_FEE = BigDecimal.valueOf(80.01);
    private static final Car.Type TOYOTA_TYPE = Car.Type.SEDAN;
    private static final Long HONDA_CAR_ID = 101L;
    private static final String HONDA_BRAND = "Honda";
    private static final String HONDA_MODEL = "Honda CR-V";
    private static final int HONDA_INVENTORY = 10;
    private static final BigDecimal HONDA_DAILY_FEE = BigDecimal.valueOf(95.51);
    private static final Car.Type HONDA_TYPE = Car.Type.SUV;
    private static final CarDtoRequest TOYOTA_CAR_DTO_REQUEST = new CarDtoRequest(
            TOYOTA_BRAND, TOYOTA_MODEL, TOYOTA_INVENTORY, TOYOTA_DAILY_FEE, TOYOTA_TYPE);
    private static final CarDtoResponse TOYOTA_CAR_DTO_RESPONSE = new CarDtoResponse(
            TOYOTA_CAR_ID, TOYOTA_BRAND, TOYOTA_MODEL,
            TOYOTA_INVENTORY, TOYOTA_DAILY_FEE, TOYOTA_TYPE);
    private static final CarDtoResponse HONDA_CAR_DTO_RESPONSE = new CarDtoResponse(
            HONDA_CAR_ID, HONDA_BRAND, HONDA_MODEL, HONDA_INVENTORY, HONDA_DAILY_FEE, HONDA_TYPE);
    private static final String ADD_TOYOTA_CAR =
            "classpath:database/car/add-toyota-car-to-cars-table.sql";
    private static final String ADD_HONDA_CAR =
            "classpath:database/car/add-honda-car-to-cars-table.sql";
    private static final String ADD_USER =
            "classpath:database/user/add-sample-user-to-users-table.sql";
    private static final String ADD_MANAGER =
            "classpath:database/user/add-manager-to-users-table.sql";
    private static final String CLEAR_USERS_TABLE =
            "classpath:database/user/delete-all-users.sql";
    private static final String CLEAR_CARS_TABLE =
            "classpath:database/car/delete-all-cars.sql";
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

    @Sql(
            scripts = {
                    ADD_USER,
                    CONNECT_USER_ROLE_TO_USER,
                    CLEAR_CARS_TABLE,
                    ADD_TOYOTA_CAR,
                    ADD_HONDA_CAR
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_USERS_TABLE,
                    CLEAR_CARS_TABLE,
                    CLEAR_USER_ROLES
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Verify retrieving all available cars")
    @WithUserDetails(USER_EMAIL)
    public void getAll_validCars_ReturnResponse() throws Exception {
        List<CarDtoResponse> expected = List.of(TOYOTA_CAR_DTO_RESPONSE, HONDA_CAR_DTO_RESPONSE);
        MvcResult result = mockMvc.perform(
                        get("/cars")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<CarDtoResponse> actual = List.of(objectMapper.readValue(result
                .getResponse().getContentAsString(), CarDtoResponse[].class));
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Sql(
            scripts = {
                    ADD_USER,
                    CONNECT_USER_ROLE_TO_USER,
                    ADD_TOYOTA_CAR
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_USER_ROLES
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Verify retrieving car by its id")
    @WithUserDetails(USER_EMAIL)
    public void getById_ValidId_ReturnResponse() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/cars/" + TOYOTA_CAR_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CarDtoResponse actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), CarDtoResponse.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                TOYOTA_CAR_DTO_RESPONSE, actual, "id"));
    }

    @Sql(
            scripts = {
                    ADD_MANAGER,
                    CONNECT_MANAGER_ROLE_TO_MANAGER,
                    ADD_TOYOTA_CAR
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_USER_ROLES
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @WithUserDetails(MANAGER_EMAIL)
    @DisplayName("Verify creating a new car")
    public void createCar_validRequestDto_returnCarDtoResponse() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(TOYOTA_CAR_DTO_REQUEST);
        MvcResult result = mockMvc.perform(
                        post("/cars")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CarDtoResponse actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), CarDtoResponse.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                TOYOTA_CAR_DTO_RESPONSE, actual, "id"));
    }

    @Sql(
            scripts = {
                    ADD_MANAGER,
                    CONNECT_MANAGER_ROLE_TO_MANAGER,
                    ADD_TOYOTA_CAR
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_USER_ROLES
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Verify deleting car by its id")
    @WithUserDetails(MANAGER_EMAIL)
    public void deleteById_ValidId_successful() throws Exception {
        mockMvc.perform(delete("/cars/" + TOYOTA_CAR_ID))
                .andExpect(status().isNoContent());
    }

    @Sql(
            scripts = {
                    ADD_MANAGER,
                    CONNECT_MANAGER_ROLE_TO_MANAGER,
                    ADD_TOYOTA_CAR
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    CLEAR_CARS_TABLE,
                    CLEAR_USERS_TABLE,
                    CLEAR_USER_ROLES
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    @DisplayName("Verify updating car by its id")
    @WithUserDetails(MANAGER_EMAIL)
    public void updateById_ValidId_successful() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/cars/" + TOYOTA_CAR_ID)
                        .content(objectMapper.writeValueAsString(TOYOTA_CAR_DTO_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CarDtoResponse actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CarDtoResponse.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                TOYOTA_CAR_DTO_RESPONSE, actual, "id"));
    }
}

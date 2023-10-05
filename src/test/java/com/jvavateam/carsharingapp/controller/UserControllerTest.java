package com.jvavateam.carsharingapp.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvavateam.carsharingapp.config.SecurityConfig;
import com.jvavateam.carsharingapp.dto.role.RoleRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserResponseDto;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SecurityConfig.class)
public class UserControllerTest {
    protected static MockMvc mockMvc;
    private static final String ADD_USER
            = "classpath:database/user/add-sample-user-to-users-table.sql";
    private static final String ADD_MANAGER
            = "classpath:database/user/add-manager-to-users-table.sql";
    private static final String ADD_ROLES
            = "classpath:database/role/insert-roles.sql";
    private static final String ADD_USER_ROLE_RELATION
            = "classpath:database/connect_user_role/connect-sample-user-role.sql";
    private static final String ADD_MANAGER_ROLE_RELATION
            = "classpath:database/connect_user_role/connect-sample-manager-role.sql";
    private static final String CLEAR_USERS
            = "classpath:database/user/delete-all-users.sql";
    private static final String CLEAR_USERS_ROLES
            = "classpath:database/connect_user_role/clear-user-roles-connection.sql";
    private static final String ClEAR_ROLES
            = "classpath:database/role/clear-roles-table.sql";

    private static final String USER_EMAIL = "wylo@ua.com";
    private static final String MANAGER_EMAIL = "super_manager@gmail.com";
    private static final RoleRequestDto ROLE_REQUEST_DTO
            = new RoleRequestDto("CUSTOMER");

    private static final UserResponseDto USER_RESPONSE_DTO
            = new UserResponseDto("wylo@ua.com",
            "Oleh",
            "Lyashko");
    private static final UserRequestDto USER_REQUEST_DTO
            = new UserRequestDto("updated@mail.com",
            "Updated123",
            "Updated123",
            "Update",
            "Update");

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
    @DisplayName("Verify getUserInfo() method works")
    @WithUserDetails(USER_EMAIL)
    @Sql(scripts = {
            ADD_USER,
            ADD_ROLES,
            ADD_USER_ROLE_RELATION
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USERS_ROLES,
            ClEAR_ROLES,
            CLEAR_USERS
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getUserInfo_AuthenticatedUser_ReturnsValidResponseDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/me"))
                .andExpect(status().isFound())
                .andReturn();
        UserResponseDto authenticatedUserInfo = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserResponseDto.class);
        Assertions.assertNotNull(authenticatedUserInfo);
        EqualsBuilder.reflectionEquals(USER_RESPONSE_DTO, authenticatedUserInfo, "id");
    }

    @Test
    @DisplayName("Verify updateUserRole() method works")
    @WithUserDetails(MANAGER_EMAIL)
    @Sql(scripts = {
            ADD_USER,
            ADD_MANAGER,
            ADD_ROLES,
            ADD_USER_ROLE_RELATION,
            ADD_MANAGER_ROLE_RELATION
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USERS_ROLES,
            CLEAR_USERS,
            ClEAR_ROLES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateUserRole_AuthenticatedManager_ReturnsValidHttpStatus() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(ROLE_REQUEST_DTO);
        mockMvc.perform(patch("/users/" + 100L + "/role")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Verify updateUserInfo() method works")
    @WithUserDetails(USER_EMAIL)
    @Sql(scripts = {
            ADD_USER,
            ADD_ROLES,
            ADD_USER_ROLE_RELATION
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USERS_ROLES,
            CLEAR_USERS,
            ClEAR_ROLES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateUserInfo_ValidUserInfoRequestDto_ReturnsValidResponseDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(USER_REQUEST_DTO);
        MvcResult result = mockMvc.perform(put("/users/me")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto updatedUserDto = objectMapper.readValue(result
                .getResponse().getContentAsString(), UserResponseDto.class);

        Assertions.assertNotNull(updatedUserDto);
        EqualsBuilder.reflectionEquals(updatedUserDto, updatedUserDto);
    }
}

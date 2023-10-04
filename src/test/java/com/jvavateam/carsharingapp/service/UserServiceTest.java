package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.role.RoleRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserResponseDto;
import com.jvavateam.carsharingapp.exception.EntityNotFoundException;
import com.jvavateam.carsharingapp.exception.RegistrationException;
import com.jvavateam.carsharingapp.mapper.user.UserMapper;
import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.user.RoleRepository;
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import com.jvavateam.carsharingapp.service.impl.UserServiceImpl;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final Role MANAGER = new Role().setId(1L).setName(Role.RoleName.MANAGER);
    private static final Role CUSTOMER = new Role().setId(2L).setName(Role.RoleName.CUSTOMER);
    private static final RoleRequestDto ROLE_REQUEST_DTO = new RoleRequestDto("MANAGER");
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 1000L;
    private static final String ENCODED_PASSWORD = "First1234";
    private static final UserRequestDto USER_REQUEST_DTO =
            new UserRequestDto("fisrtUser@mail.com",
                    "First1234",
                    "First1234",
                    "First",
                    "First");
    private static final User USER_FROM_DB = new User()
            .setId(1L)
            .setEmail("fisrtUser@mail.com")
            .setPassword("First1234")
            .setFirstName("First")
            .setLastName("First")
            .setDeleted(false)
            .setRoles(Set.of(CUSTOMER));
    private static final UserResponseDto USER_RESPONSE_DTO =
            new UserResponseDto("fisrtUser@mail.com",
                    "First",
                    "First" );
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

//    @Test
//    @DisplayName("Verify getAuthentificationMethod() method works")
//    public void getAuthentificationMethod_ReturnsAuthenticatedUser() {
//        Mockito.when(userRepository.getCurrentUser()).thenReturn(USER_FROM_DB);
//
//    }

//    @Test
//    @DisplayName("Verify updateUserInfo() method works")
//    public void updateUserInfo_ValidRequestDto_ReturnsValidResponseDto() {
//        Mockito.when(userMapper.toModel(USER_REQUEST_DTO)).thenReturn(USER_FROM_DB);
//
//        UserResponseDto updateUserDto = userService.updateUserInfo(USER_REQUEST_DTO);
//
//        EqualsBuilder.reflectionEquals(updateUserDto, USER_RESPONSE_DTO);
//    }

    @Test
    @DisplayName("Verify updateUserRole() method works")
    public void updateUserRole_ValidUserIdAndRole_ReturnsResponseStatusOk() {
        Mockito.when(userRepository.findById(VALID_ID)).thenReturn(Optional.of(USER_FROM_DB));
        Mockito.when(roleRepository.getRoleByName(Role.RoleName.valueOf(ROLE_REQUEST_DTO.status())))
                .thenReturn(MANAGER);

        userService.updateUserRole(VALID_ID, ROLE_REQUEST_DTO);
    }

    @Test
    @DisplayName("Verify updateUserRole() doesn't work for invalid id")
    public void updateUserRole_InValidUserIdAndRole_ThrowsException() {
        Mockito.when(userRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userService.updateUserRole(INVALID_ID, ROLE_REQUEST_DTO));

        String expected = "User with such ID doesn't exists";
        String actual = exception.getMessage();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify register() method works")
    public void register_ValidUserRequestDto_ReturnsValidUserResponseDto() throws RegistrationException {
        Mockito.when(userMapper.toModel(USER_REQUEST_DTO)).thenReturn(USER_FROM_DB);
        Mockito.when(passwordEncoder.encode(USER_FROM_DB.getPassword())).thenReturn(ENCODED_PASSWORD);
        Mockito.when(roleRepository.getRoleByName(CUSTOMER.getName())).thenReturn(CUSTOMER);
        Mockito.when(userRepository.save(USER_FROM_DB)).thenReturn(USER_FROM_DB);
        Mockito.when(userMapper.toDto(USER_FROM_DB)).thenReturn(USER_RESPONSE_DTO);

        UserResponseDto registerUser = userService.register(USER_REQUEST_DTO);

        Assertions.assertEquals(registerUser, USER_RESPONSE_DTO);
    }

    @Test
    @DisplayName("Verify register() method doesn't work for existing user")
    public void register_InValidUserRequestDto_ThrowsRegistrationException() {
        Mockito.when(userRepository.findByEmail(USER_REQUEST_DTO.email()))
                .thenReturn(Optional.of(USER_FROM_DB));

        Exception exception = assertThrows(RegistrationException.class,
                () -> userService.register(USER_REQUEST_DTO));

        String expected = "User with such email already exists";
        String actual = exception.getMessage();

        Assertions.assertEquals(expected, actual);
    }
}

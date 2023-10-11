package com.jvavateam.carsharingapp.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final Role MANAGER = new Role().setId(1L).setName(Role.RoleName.MANAGER);
    private static final Role CUSTOMER = new Role().setId(2L).setName(Role.RoleName.CUSTOMER);
    private static final String[] ROLE = {"MANAGER"};
    private static final RoleRequestDto ROLE_REQUEST_DTO = new RoleRequestDto(ROLE);
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 1000L;
    private static final String ENCODED_PASSWORD = "Password1234";
    private static final UserRequestDto USER_REQUEST_DTO =
            new UserRequestDto("userBob@mail.com",
                    "Password1234",
                    "Password1234",
                    "Bob",
                    "Johnson");
    private static final User USER = new User()
            .setId(1L)
            .setEmail("userBob@mail.com")
            .setPassword("Password1234")
            .setFirstName("Bob")
            .setLastName("Johnson")
            .setDeleted(false)
            .setRoles(Set.of(CUSTOMER));
    private static final UserResponseDto USER_RESPONSE_DTO =
            new UserResponseDto("userBob@mail.com",
                    "Bob",
                    "Johnson");
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

    @Test
    @DisplayName("Verify getCurrentUserInfo() method works")
    public void getCurrentUserInfo_ReturnsUserResponseDto() {
        Mockito.when(userRepository.getCurrentUser()).thenReturn(USER);
        Mockito.when(userMapper.toDto(USER)).thenReturn(USER_RESPONSE_DTO);

        UserResponseDto authentificationUserInfo = userService.getCurrentUserInfo();

        Assertions.assertEquals(authentificationUserInfo, USER_RESPONSE_DTO);
        Mockito.verify(userRepository, Mockito.times(1))
                .getCurrentUser();
        Mockito.verify(userMapper, Mockito.times(1))
                .toDto(USER);
    }

    @Test
    @DisplayName("Verify getAuthentificationMethod() method works")
    public void getAuthentificationMethod_ReturnsAuthenticatedUser() {
        Mockito.when(userRepository.getCurrentUser()).thenReturn(USER);

        User authentificatedUser = userService.getAuthentificatedUser();

        Mockito.verify(userRepository, Mockito.times(1))
                        .getCurrentUser();
        Assertions.assertEquals(authentificatedUser, USER);
    }

    @Test
    @DisplayName("Verify updateUserInfo() method works")
    public void updateUserInfo_ValidRequestDto_ReturnsValidResponseDto() {
        Mockito.when(userMapper.toModel(USER_REQUEST_DTO)).thenReturn(USER);
        Mockito.when(userRepository.getCurrentUser()).thenReturn(USER);
        Mockito.when(userRepository.getCurrentUser()).thenReturn(USER);
        Mockito.when(userRepository.save(USER)).thenReturn(USER);
        Mockito.when(userMapper.toDto(USER)).thenReturn(USER_RESPONSE_DTO);

        UserResponseDto updateUserDto = userService.updateUserInfo(USER_REQUEST_DTO);

        Assertions.assertEquals(updateUserDto, USER_RESPONSE_DTO);
        Mockito.verify(userMapper, Mockito.times(1))
                .toModel(USER_REQUEST_DTO);
        Mockito.verify(userRepository, Mockito.times(2))
                .getCurrentUser();
        Mockito.verify(userRepository, Mockito.times(1))
                .save(USER);
        Mockito.verify(userMapper, Mockito.times(1))
                .toDto(USER);
    }

    @Test
    @DisplayName("Verify updateUserRole() method works")
    public void updateUserRole_ValidUserIdAndRole_ReturnsResponseStatusOk() {
        Mockito.when(userRepository.findById(VALID_ID)).thenReturn(Optional.of(USER));
        Mockito.when(roleRepository
                        .getRoleByName(Role.RoleName.valueOf(ROLE_REQUEST_DTO.status()[0])))
                .thenReturn(MANAGER);

        userService.updateUserRole(VALID_ID, ROLE_REQUEST_DTO);

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(VALID_ID);
        Mockito.verify(roleRepository, Mockito.times(1))
                .getRoleByName(Role.RoleName.valueOf(ROLE_REQUEST_DTO.status()[0]));
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
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(INVALID_ID);
    }

    @Test
    @DisplayName("Verify register() method works")
    public void register_ValidUserRequestDto_ReturnsValidUserResponseDto()
            throws RegistrationException {
        Mockito.when(userMapper.toModel(USER_REQUEST_DTO)).thenReturn(USER);
        Mockito.when(passwordEncoder.encode(USER.getPassword())).thenReturn(ENCODED_PASSWORD);
        Mockito.when(roleRepository.getRoleByName(CUSTOMER.getName())).thenReturn(CUSTOMER);
        Mockito.when(userRepository.save(USER)).thenReturn(USER);
        Mockito.when(userMapper.toDto(USER)).thenReturn(USER_RESPONSE_DTO);

        UserResponseDto registeredUser = userService.register(USER_REQUEST_DTO);

        Assertions.assertEquals(registeredUser, USER_RESPONSE_DTO);
        Mockito.verify(userMapper, Mockito.times(1))
                .toModel(USER_REQUEST_DTO);
        Mockito.verify(roleRepository, Mockito.times(1))
                .getRoleByName(CUSTOMER.getName());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(USER);
        Mockito.verify(userMapper, Mockito.times(1))
                .toDto(USER);
    }

    @Test
    @DisplayName("Verify register() method doesn't work for existing user")
    public void register_InValidUserRequestDto_ThrowsRegistrationException() {
        Mockito.when(userRepository.findByEmail(USER_REQUEST_DTO.email()))
                .thenReturn(Optional.of(USER));

        Exception exception = assertThrows(RegistrationException.class,
                () -> userService.register(USER_REQUEST_DTO));

        String expected = "User with such email already exists";
        String actual = exception.getMessage();

        Assertions.assertEquals(expected, actual);
        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(USER_REQUEST_DTO.email());
    }
}

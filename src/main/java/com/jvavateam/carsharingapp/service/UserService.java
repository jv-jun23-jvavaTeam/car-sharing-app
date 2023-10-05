package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.role.RoleRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserResponseDto;
import com.jvavateam.carsharingapp.exception.RegistrationException;
import com.jvavateam.carsharingapp.model.User;
import java.util.List;

public interface UserService {
    UserResponseDto getCurrentUserInfo();

    void updateUserRole(Long id, RoleRequestDto role);

    UserResponseDto updateUserInfo(UserRequestDto userRequestDto);

    UserResponseDto register(UserRequestDto request) throws RegistrationException;

    User getAuthentificatedUser();

    List<User> findAllManagers();
}

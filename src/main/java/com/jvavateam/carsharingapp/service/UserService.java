package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.role.RoleRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserResponseDto;
import com.jvavateam.carsharingapp.exception.RegistrationException;
import com.jvavateam.carsharingapp.model.Role;

public interface UserService {
    UserResponseDto getCurrentUserInfo();

    void updateUserRole(Long id, RoleRequestDto role);

    void updateUserInfo(UserRequestDto userRequestDto);

    UserResponseDto register(UserRequestDto request) throws RegistrationException;
}

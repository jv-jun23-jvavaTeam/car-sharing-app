package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.user.UserRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserResponseDto;
import com.jvavateam.carsharingapp.model.Role;

public interface UserService {
    UserResponseDto getCurrentUserInfo();

    void updateUserRole(Long id, Role role);

    void updateUserInfo(UserRequestDto userRequestDto);
}

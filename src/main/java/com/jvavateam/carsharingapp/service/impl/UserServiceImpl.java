package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.user.UserRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserResponseDto;
import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.UserRepository;
import com.jvavateam.carsharingapp.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDto getCurrentUserInfo() {
        return null;
    }

    @Override
    public void updateUserRole(Long id, Role role) {
        User userForRoleUpdate = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User by provided id "
                + id + " was not found"));
        userForRoleUpdate.setRoles(Set.of(role));
        userRepository.save(userForRoleUpdate);
    }

    @Override
    public void updateUserInfo(UserRequestDto userRequestDto) {
    }
}

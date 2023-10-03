package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.user.UserRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserResponseDto;
import com.jvavateam.carsharingapp.mapper.user.UserMapper;
import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import com.jvavateam.carsharingapp.repository.user.RoleRepository;
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import com.jvavateam.carsharingapp.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public UserResponseDto register(UserRequestDto request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("User with such email is already exists");
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role customer = roleRepository.getRoleByName(Role.RoleName.CUSTOMER);
        user.setRoles(Set.of(customer));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}

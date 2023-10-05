package com.jvavateam.carsharingapp.service.impl;

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
import com.jvavateam.carsharingapp.service.UserService;
import java.util.List;
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
        return userMapper.toDto(getAuthentificatedUser());
    }

    @Override
    public void updateUserRole(Long id, RoleRequestDto role) {
        User userForRoleUpdating = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with such ID doesn't exists"));
        Role roleToUpdate = roleRepository.getRoleByName(Role.RoleName.valueOf(role.status()));
        userForRoleUpdating.setRoles(Set.of(roleToUpdate));
        userRepository.save(userForRoleUpdating);
    }

    @Override
    public UserResponseDto updateUserInfo(UserRequestDto userRequestDto) {
        User userForInfoUpdating = userMapper.toModel(userRequestDto);
        userForInfoUpdating.setId(userRepository.getCurrentUser().getId());
        return userMapper.toDto(userRepository.save(userForInfoUpdating));
    }

    @Override
    public UserResponseDto register(UserRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RegistrationException("User with such email already exists");
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role customer = roleRepository.getRoleByName(Role.RoleName.CUSTOMER);
        user.setRoles(Set.of(customer));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public User getAuthentificatedUser() {
        return userRepository.getCurrentUser();
    }

    @Override
    public List<User> findAllManagers() {
        Role role = roleRepository.getRoleByName(Role.RoleName.MANAGER);
        return userRepository.findAllByRolesContains(role);
    }
}

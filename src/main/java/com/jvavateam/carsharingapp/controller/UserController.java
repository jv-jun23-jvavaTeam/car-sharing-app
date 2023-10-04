package com.jvavateam.carsharingapp.controller;

import com.jvavateam.carsharingapp.dto.role.RoleRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserResponseDto;
import com.jvavateam.carsharingapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
@Tag(name = "Users management",
        description = "Endpoints for user's operations")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Retrieve user's info",
            description = "Retrieve current user full information")
    @ResponseStatus(HttpStatus.FOUND)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public UserResponseDto getUserInfo() {
        return userService.getCurrentUserInfo();
    }

    @PatchMapping ("/{id}/role")
    @Operation(summary = "Update user role",
            description = "Update user role by provided user ID and new role")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('MANAGER')")
    public void updateUserRole(@PathVariable Long id, @RequestBody RoleRequestDto role) {
        userService.updateUserRole(id, role);
    }

    @PutMapping("/me")
    @Operation(summary = "Update user's info",
            description = "Update current user's full information")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public UserResponseDto updateUserInfo(@RequestBody UserRequestDto request) {
        return userService.updateUserInfo(request);
    }
}

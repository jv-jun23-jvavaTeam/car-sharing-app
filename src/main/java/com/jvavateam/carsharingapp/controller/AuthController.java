package com.jvavateam.carsharingapp.controller;

import com.jvavateam.carsharingapp.dto.user.UserLoginRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserLoginResponseDto;
import com.jvavateam.carsharingapp.dto.user.UserRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserResponseDto;
import com.jvavateam.carsharingapp.security.AuthenticationService;
import com.jvavateam.carsharingapp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification management",
        description = "Endpoints for registration and authentification")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody UserRequestDto request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}

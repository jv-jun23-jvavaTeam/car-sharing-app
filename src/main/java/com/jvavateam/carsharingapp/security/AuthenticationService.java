package com.jvavateam.carsharingapp.security;

import com.jvavateam.carsharingapp.dto.user.UserLoginRequestDto;
import com.jvavateam.carsharingapp.dto.user.UserLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager manager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {
        final Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(),
                        request.password())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}

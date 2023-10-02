package com.jvavateam.carsharingapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDto(@Email @NotBlank String email,
                             @NotBlank @Size(min = 8) String password,
                             @NotNull @Size(min = 8) String repeatPassword,
                             @NotBlank String firstName,
                             @NotBlank String lastName) {
}

package com.jvavateam.carsharingapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
public record UserRequestDto(@Email @NotBlank String email,
                             @NotBlank @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
                                     message = "Minimum eight characters, at least one uppercase"
                                             + " letter, one lowercase letter and one number")
                             String password,
                             @NotNull @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$",
                                     message = "Minimum eight characters, at least one uppercase"
                                             + " letter, one lowercase letter and one number") String repeatPassword,
                             @NotBlank String firstName,
                             @NotBlank String lastName) {
}

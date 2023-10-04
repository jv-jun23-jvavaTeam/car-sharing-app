package com.jvavateam.carsharingapp.dto.user;

import com.jvavateam.carsharingapp.validation.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@FieldMatch(first = "password",
            second = "repeatPassword",
            message = "Passwords don't match")
public record UserRequestDto(@Email
                             @NotBlank(message = "User's email cannot be blank")
                             @Schema(description = "User's email", example = "user@mail.com")
                             String email,
                             @NotBlank(message = "User's password cannot be blank")
                             @Pattern(regexp = "^(?=.*[a-z])"
                                     + "(?=.*[A-Z])(?=.*\\d)"
                                     + "[a-zA-Z\\d]{6,}$",
                                     message = "Minimum eight characters, "
                                             + "at least one uppercase"
                                             + " letter, one lowercase "
                                             + "letter and one number")
                             @Schema(description = "User's password", example = "Aa12345678")
                             String password,
                             @NotBlank(message = "User's repeat password cannot be blank")
                             @Pattern(regexp = "^(?=.*[a-z])"
                                     + "(?=.*[A-Z])(?=.*\\d)"
                                     + "[a-zA-Z\\d]{6,}$",
                                     message = "Minimum eight characters, "
                                             + "at least one uppercase"
                                             + " letter, one lowercase "
                                             + "letter and one number")
                             @Schema(description = "User's repeat password", example = "Aa12345678")
                             String repeatPassword,
                             @NotBlank(message = "User's first name cannot be blank")
                             @Schema(description = "User's first name", example = "John")
                             String firstName,
                             @NotBlank(message = "User's last name cannot be blank")
                             @Schema(description = "User's last name", example = "Doe")
                             String lastName) {
}

package com.jvavateam.carsharingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Accessors(chain = true)
public record UserLoginRequestDto(
        @NotBlank(message = "User's email cannot be blank")
        @Email
        @Schema(description = "User's email", example = "user@mail.com")
        String email,
        @NotBlank(message = "User's password cannot be blank")
        @Length(min = 8, message = "Minimum password length is 8")
        @Schema(description = "User's password", example = "Aa12345678")
        String password) {
}

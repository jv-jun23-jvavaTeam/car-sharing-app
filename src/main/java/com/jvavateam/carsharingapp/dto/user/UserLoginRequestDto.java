package com.jvavateam.carsharingapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Accessors(chain = true)
public record UserLoginRequestDto(
        @Email @NotBlank String email,
        @NotBlank @Length(min = 8)
        String password) {
}

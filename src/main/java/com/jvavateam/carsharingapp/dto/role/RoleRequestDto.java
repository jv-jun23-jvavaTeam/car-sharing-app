package com.jvavateam.carsharingapp.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RoleRequestDto(
        @NotNull(message = "Status cannot be null")
        @Schema(description = "Role status", example = "CUSTOMER")
        String[] status) {
}


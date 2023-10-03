package com.jvavateam.carsharingapp.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequestDto {
    @NotBlank
    private String status;
}

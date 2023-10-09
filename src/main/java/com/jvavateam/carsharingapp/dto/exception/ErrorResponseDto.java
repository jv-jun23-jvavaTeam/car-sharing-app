package com.jvavateam.carsharingapp.dto.exception;

import java.time.LocalDateTime;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Accessors(chain = true)
public record ErrorResponseDto(LocalDateTime timestamp, HttpStatus status, String error) {
}

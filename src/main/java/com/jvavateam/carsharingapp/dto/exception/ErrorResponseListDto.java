package com.jvavateam.carsharingapp.dto.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Accessors(chain = true)
public record ErrorResponseListDto(LocalDateTime timestamp,
                                   HttpStatus status,
                                   List<String> validationErrors) {
}

package com.jvavateam.carsharingapp.exception;

import com.jvavateam.carsharingapp.dto.exception.ErrorResponseDto;
import com.jvavateam.carsharingapp.dto.exception.ErrorResponseListDto;
import io.jsonwebtoken.JwtException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String UNKNOWN_ERROR_MESSAGE =
            "Server throws unknown exception ";

    private static final String PAYMENT_ERROR_MESSAGE =
            "An error occured while proceeding payment: ";

    private static final String ENTITY_NOT_FOUND_MESSAGE =
            "An error occurred while proceeding data from database. "
                    + "Nothing found by provided parameters: ";
    private static final String INVALID_REQUEST_DATA_MESSAGE =
            "An error occurred while proceeding request data: ";
    private static final String BAD_CREDENTIALS_MESSAGE =
            "User not authenticated: Wrong data entered: ";
    private static final String WRONG_JWT_MESSAGE =
            "User not authenticated: Error during proceeding JWT: ";
    private static final String ACCESS_DENIED_MESSAGE =
            "User does not have access for this action: ";
    private static final String WRONG_REGISTRATION_DATA_MESSAGE =
            "Entered wrong data for registration: ";
    private static final String DATA_INTEGRITY_VIOLATION_MESSAGE =
            "An error occurred while processing the request. "
            + "You are attempting to add data "
            + "that violates a constraint in the database: ";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        ErrorResponseListDto errorResponse = new ErrorResponseListDto();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        errorResponse.setValidationErrors(errors);
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleAllErrors(Exception exception) {
        logger.error("Internal server error: ", exception);
        ErrorResponseDto response =
                getErrorMessageBody(UNKNOWN_ERROR_MESSAGE,
                        HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleUniqueDataDuplicate(
            DataIntegrityViolationException ex) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody(DATA_INTEGRITY_VIOLATION_MESSAGE + ex.getMessage(),
                        HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({RegistrationException.class})
    protected ResponseEntity<ErrorResponseDto> handleRegistrationException(
            RegistrationException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody(WRONG_REGISTRATION_DATA_MESSAGE
                                + ex.getMessage(),
                        HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponseDto> handleAccessDeniedException(
            AccessDeniedException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody(ACCESS_DENIED_MESSAGE
                                + ex.getMessage(),
                        HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponseDto> handleJwtException(JwtException ex) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody(WRONG_JWT_MESSAGE
                        + ex.getMessage(),
                        HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ErrorResponseDto> handleBadCredentialsException(
            BadCredentialsException ex) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody(BAD_CREDENTIALS_MESSAGE
                        + ex.getMessage(),
                        HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidRequestParametersException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRequestParametersException(
            InvalidRequestParametersException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody(INVALID_REQUEST_DATA_MESSAGE
                        + ex.getMessage(),
                        HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(
            EntityNotFoundException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody(ENTITY_NOT_FOUND_MESSAGE
                        + ex.getMessage(),
                        HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({PaymentException.class})
    protected ResponseEntity<ErrorResponseDto> handlePaymentException(
            PaymentException ex
    ) {
        ErrorResponseDto errorResponse =
                getErrorMessageBody(PAYMENT_ERROR_MESSAGE
                                + ex.getMessage(),
                        HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
  
    @ExceptionHandler(TelegramBotException.class)
    public void handleTelegramBotException(TelegramBotException e) {
        logger.error("Telegram bot exception occurred", e);
    }

    private ErrorResponseDto getErrorMessageBody(String errorMessage, HttpStatus httpStatus) {
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(httpStatus);
        errorResponse.setError(errorMessage);
        return errorResponse;
    }

    private String getErrorMessage(ObjectError ex) {
        if (ex instanceof FieldError) {
            String field = ((FieldError) ex).getField();
            String defaultMessage = ex.getDefaultMessage();
            return defaultMessage + " for " + field;
        }
        return ex.getDefaultMessage();
    }
}

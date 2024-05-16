package com.example.userapi.exception;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST);
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                                      .map(e -> getErrorMessage(e))
                                      .toList();
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            return field + ": " + message;
        }
        return e.getDefaultMessage();
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(RuntimeException exception) {
        Map<String, Object> errorResponseBody = new LinkedHashMap<>();
        errorResponseBody.put(TIMESTAMP, LocalDateTime.now());
        errorResponseBody.put(STATUS, HttpStatus.NOT_FOUND);
        errorResponseBody.put(ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidUserBirthDateException.class})
    protected ResponseEntity<Object> handleInvalidBirthDateException(RuntimeException exception) {
        Map<String, Object> errorResponseBody = new LinkedHashMap<>();
        errorResponseBody.put(TIMESTAMP, LocalDateTime.now());
        errorResponseBody.put(STATUS, HttpStatus.BAD_REQUEST);
        errorResponseBody.put(ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidDateRangeException.class})
    protected ResponseEntity<Object> handleInvalidBirthDateRangeException(RuntimeException exception) {
        Map<String, Object> errorResponseBody = new LinkedHashMap<>();
        errorResponseBody.put(TIMESTAMP, LocalDateTime.now());
        errorResponseBody.put(STATUS, HttpStatus.BAD_REQUEST);
        errorResponseBody.put(ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);
    }
}

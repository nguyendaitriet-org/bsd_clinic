package com.bsdclinic.exception_handler;

import com.bsdclinic.exception_handler.exception.ForbiddenException;
import com.bsdclinic.exception_handler.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetails> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        ErrorDetails response = new ErrorDetails(
                HttpStatus.UNAUTHORIZED.value(),
                "error.401",
                null
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDetails> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        ErrorDetails response = new ErrorDetails(
                HttpStatus.FORBIDDEN.value(),
                "error.403",
                null
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler ({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorDetails> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errorDetails = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            // Handle to get main property
            String[] parts = propertyPath.split("\\.");
            String mainProperty = parts[parts.length - 1];
            String errorMessage = violation.getMessage();
            errorDetails.put(mainProperty, errorMessage);
        }
        ErrorDetails response = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), errorDetails);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorDetails {
        private int errorCode;
        private String message;
        private Map<String, String> errors;
    }
}
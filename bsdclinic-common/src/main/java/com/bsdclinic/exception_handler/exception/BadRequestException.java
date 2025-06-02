package com.bsdclinic.exception_handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private final Map<String, String> errors;

    public BadRequestException(Map<String, String> errorDetails) {
        super();
        this.errors = errorDetails;
    }

    public Map<String, String> getErrors() {return errors;}
}
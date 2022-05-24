package com.biit.usermanager.core.exceptions;


import com.biit.logger.ExceptionType;
import com.biit.usermanager.logger.LoggedException;
import org.springframework.http.HttpStatus;

public class ValidateBadRequestException extends LoggedException {

    public ValidateBadRequestException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type, HttpStatus.NOT_FOUND);
    }

    public ValidateBadRequestException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.WARNING, HttpStatus.NOT_FOUND);
    }

    public ValidateBadRequestException(Class<?> clazz) {
        this(clazz, "Invalid!");
    }

    public ValidateBadRequestException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

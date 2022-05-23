package com.biit.usermanager.core.exceptions;


import com.biit.logger.ExceptionType;
import com.biit.usermanager.logger.LoggedException;
import org.springframework.http.HttpStatus;

public class UnexpectedValueException extends LoggedException {

    public UnexpectedValueException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type, HttpStatus.NOT_FOUND);
    }

    public UnexpectedValueException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.WARNING, HttpStatus.NOT_FOUND);
    }

    public UnexpectedValueException(Class<?> clazz) {
        this(clazz, "Unexpected!");
    }

    public UnexpectedValueException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

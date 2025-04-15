package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.usermanager.logger.LoggedException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidParameterException extends LoggedException {
    @Serial
    private static final long serialVersionUID = -2510787123562846634L;

    public InvalidParameterException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type, HttpStatus.NOT_FOUND);
    }

    public InvalidParameterException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.WARNING, HttpStatus.NOT_FOUND);
    }

    public InvalidParameterException(Class<?> clazz) {
        this(clazz, "Comment not found");
    }

    public InvalidParameterException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

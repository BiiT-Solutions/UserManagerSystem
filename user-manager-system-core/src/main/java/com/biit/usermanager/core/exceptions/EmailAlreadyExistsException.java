package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyExistsException extends LoggedException {

    @Serial
    private static final long serialVersionUID = -3507272771195819297L;

    public EmailAlreadyExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public EmailAlreadyExistsException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.INFO, HttpStatus.BAD_REQUEST);
    }

    public EmailAlreadyExistsException(Class<?> clazz) {
        this(clazz, "User already exists!");
    }

    public EmailAlreadyExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

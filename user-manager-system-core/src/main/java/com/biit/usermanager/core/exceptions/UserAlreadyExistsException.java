package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends LoggedException {
    private static final long serialVersionUID = 7032994111678894370L;

    public UserAlreadyExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public UserAlreadyExistsException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.INFO, HttpStatus.CONFLICT);
    }

    public UserAlreadyExistsException(Class<?> clazz) {
        this(clazz, "User already exists!");
    }

    public UserAlreadyExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

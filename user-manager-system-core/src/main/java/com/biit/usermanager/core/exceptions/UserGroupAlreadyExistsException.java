package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserGroupAlreadyExistsException extends LoggedException {

    @Serial
    private static final long serialVersionUID = -963427805471263596L;

    public UserGroupAlreadyExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public UserGroupAlreadyExistsException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.INFO, HttpStatus.BAD_REQUEST);
    }

    public UserGroupAlreadyExistsException(Class<?> clazz) {
        this(clazz, "Group already exists!");
    }

    public UserGroupAlreadyExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

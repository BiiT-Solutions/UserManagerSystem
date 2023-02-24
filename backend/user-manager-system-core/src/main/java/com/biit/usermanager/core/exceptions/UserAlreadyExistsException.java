package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends NotFoundException {
    private static final long serialVersionUID = 7032994111678894370L;

    public UserAlreadyExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public UserAlreadyExistsException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public UserAlreadyExistsException(Class<?> clazz) {
        this(clazz, "User not found");
    }

    public UserAlreadyExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

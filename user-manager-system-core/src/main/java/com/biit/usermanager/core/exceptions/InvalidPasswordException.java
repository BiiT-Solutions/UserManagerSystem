package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidPasswordException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = 7032994111678894370L;

    public InvalidPasswordException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public InvalidPasswordException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public InvalidPasswordException(Class<?> clazz) {
        this(clazz, "User not found");
    }

    public InvalidPasswordException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

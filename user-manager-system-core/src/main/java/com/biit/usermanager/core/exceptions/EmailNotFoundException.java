package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailNotFoundException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = -716059469847920927L;

    public EmailNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public EmailNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public EmailNotFoundException(Class<?> clazz) {
        this(clazz, "User not found");
    }

    public EmailNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BackendServiceNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = -2342025998729753831L;

    public BackendServiceNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public BackendServiceNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public BackendServiceNotFoundException(Class<?> clazz) {
        this(clazz, "Role not found");
    }

    public BackendServiceNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApplicationBackendServiceNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = -2342025228729753831L;

    public ApplicationBackendServiceNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ApplicationBackendServiceNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public ApplicationBackendServiceNotFoundException(Class<?> clazz) {
        this(clazz, "Role not found");
    }

    public ApplicationBackendServiceNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

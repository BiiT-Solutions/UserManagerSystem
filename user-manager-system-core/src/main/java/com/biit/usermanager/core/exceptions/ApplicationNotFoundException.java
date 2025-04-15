package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApplicationNotFoundException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = 7132994133678894370L;

    public ApplicationNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ApplicationNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public ApplicationNotFoundException(Class<?> clazz) {
        this(clazz, "Role not found");
    }

    public ApplicationNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

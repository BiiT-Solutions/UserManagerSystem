package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BackendServiceRoleNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = -2342025111729753831L;

    public BackendServiceRoleNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public BackendServiceRoleNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public BackendServiceRoleNotFoundException(Class<?> clazz) {
        this(clazz, "Role not found");
    }

    public BackendServiceRoleNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

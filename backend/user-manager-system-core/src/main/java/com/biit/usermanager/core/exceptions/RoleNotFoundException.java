package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 7132994111678894370L;

    public RoleNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public RoleNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public RoleNotFoundException(Class<?> clazz) {
        this(clazz, "Role not found");
    }

    public RoleNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

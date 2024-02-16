package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoleWithoutBackendServiceRoleException extends LoggedException {
    @Serial
    private static final long serialVersionUID = -2342125111729635831L;

    public RoleWithoutBackendServiceRoleException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public RoleWithoutBackendServiceRoleException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.SEVERE);
    }

    public RoleWithoutBackendServiceRoleException(Class<?> clazz) {
        this(clazz, "Role not found");
    }

    public RoleWithoutBackendServiceRoleException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

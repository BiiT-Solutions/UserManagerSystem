package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoleAlreadyExistsException extends LoggedException {


    @Serial
    private static final long serialVersionUID = -3097428695193709389L;

    public RoleAlreadyExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public RoleAlreadyExistsException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.INFO, HttpStatus.BAD_REQUEST);
    }

    public RoleAlreadyExistsException(Class<?> clazz) {
        this(clazz, "Role already exists!");
    }

    public RoleAlreadyExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

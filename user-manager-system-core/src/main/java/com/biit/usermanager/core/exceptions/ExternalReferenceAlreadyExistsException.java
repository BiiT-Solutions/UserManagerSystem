package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExternalReferenceAlreadyExistsException extends LoggedException {

    @Serial
    private static final long serialVersionUID = -4999613495139154923L;

    public ExternalReferenceAlreadyExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ExternalReferenceAlreadyExistsException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.INFO, HttpStatus.BAD_REQUEST);
    }

    public ExternalReferenceAlreadyExistsException(Class<?> clazz) {
        this(clazz, "User already exists!");
    }

    public ExternalReferenceAlreadyExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

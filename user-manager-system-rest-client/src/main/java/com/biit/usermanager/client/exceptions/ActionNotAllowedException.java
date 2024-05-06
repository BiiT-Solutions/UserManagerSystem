package com.biit.usermanager.client.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;

import java.io.Serial;


public class ActionNotAllowedException extends LoggedException {

    @Serial
    private static final long serialVersionUID = 8171412898207123564L;

    public ActionNotAllowedException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.WARNING, HttpStatus.METHOD_NOT_ALLOWED);
    }

    public ActionNotAllowedException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type, HttpStatus.METHOD_NOT_ALLOWED);
    }
}

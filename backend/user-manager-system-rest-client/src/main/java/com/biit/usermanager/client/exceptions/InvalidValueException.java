package com.biit.usermanager.client.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;

import java.io.Serial;


public class InvalidValueException extends LoggedException {

    @Serial
    private static final long serialVersionUID = 8171412838307235644L;

    public InvalidValueException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.WARNING, null);
    }

    public InvalidValueException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type, null);
    }
}

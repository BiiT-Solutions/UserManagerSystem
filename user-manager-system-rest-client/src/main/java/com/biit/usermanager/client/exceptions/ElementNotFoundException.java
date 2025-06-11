package com.biit.usermanager.client.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;

import java.io.Serial;

public class ElementNotFoundException extends LoggedException {

    @Serial
    private static final long serialVersionUID = 1664081286620176382L;

    public ElementNotFoundException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.WARNING, null);
    }

    public ElementNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type, null);
    }

    public ElementNotFoundException(Class<?> clazz, String message, Throwable e) {
        super(clazz, message, e, ExceptionType.SEVERE, null);
    }
}

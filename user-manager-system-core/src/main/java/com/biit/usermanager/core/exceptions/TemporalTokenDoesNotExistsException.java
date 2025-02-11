package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TemporalTokenDoesNotExistsException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = 5114334832884622859L;

    public TemporalTokenDoesNotExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public TemporalTokenDoesNotExistsException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public TemporalTokenDoesNotExistsException(Class<?> clazz) {
        this(clazz, "Token not found");
    }

    public TemporalTokenDoesNotExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

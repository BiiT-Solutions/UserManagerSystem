package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TeamAlreadyExistsException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = -1616121732096251421L;

    public TeamAlreadyExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public TeamAlreadyExistsException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public TeamAlreadyExistsException(Class<?> clazz) {
        this(clazz, "Group not found");
    }

    public TeamAlreadyExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

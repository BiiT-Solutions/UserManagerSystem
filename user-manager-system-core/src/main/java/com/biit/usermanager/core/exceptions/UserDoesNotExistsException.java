package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserDoesNotExistsException extends UserNotFoundException {

    @Serial
    private static final long serialVersionUID = -3114061489180352282L;

    public UserDoesNotExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public UserDoesNotExistsException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public UserDoesNotExistsException(Class<?> clazz) {
        this(clazz, "User not found");
    }

    public UserDoesNotExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

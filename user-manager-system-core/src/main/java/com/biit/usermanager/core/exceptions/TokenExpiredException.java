package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenExpiredException extends InvalidRequestException {

    @Serial
    private static final long serialVersionUID = 1940336335628874562L;

    public TokenExpiredException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public TokenExpiredException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public TokenExpiredException(Class<?> clazz) {
        this(clazz, "Token has expired!");
    }

    public TokenExpiredException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

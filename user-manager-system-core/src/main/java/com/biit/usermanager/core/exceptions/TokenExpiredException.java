package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class TokenExpiredException extends InvalidRequestException {

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

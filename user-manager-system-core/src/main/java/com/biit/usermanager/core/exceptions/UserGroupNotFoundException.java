package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserGroupNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 7032994112178894311L;

    public UserGroupNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public UserGroupNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public UserGroupNotFoundException(Class<?> clazz) {
        this(clazz, "UserGroup not found");
    }

    public UserGroupNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

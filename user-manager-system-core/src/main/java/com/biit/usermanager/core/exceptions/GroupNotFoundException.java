package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GroupNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 7132994121678894370L;

    public GroupNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public GroupNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public GroupNotFoundException(Class<?> clazz) {
        this(clazz, "Group not found");
    }

    public GroupNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

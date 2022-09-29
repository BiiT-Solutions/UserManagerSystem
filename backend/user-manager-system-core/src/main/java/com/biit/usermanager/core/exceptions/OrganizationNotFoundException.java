package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrganizationNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 7132994121678894370L;

    public OrganizationNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public OrganizationNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public OrganizationNotFoundException(Class<?> clazz) {
        this(clazz, "Organization not found");
    }

    public OrganizationNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrganizationNotFoundException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = -1616121732096251271L;

    public OrganizationNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public OrganizationNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public OrganizationNotFoundException(Class<?> clazz) {
        this(clazz, "Group not found");
    }

    public OrganizationNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

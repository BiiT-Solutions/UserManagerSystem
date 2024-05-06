package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrganizationAlreadyExistsException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = -161123452096251271L;

    public OrganizationAlreadyExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public OrganizationAlreadyExistsException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public OrganizationAlreadyExistsException(Class<?> clazz) {
        this(clazz, "Group not found");
    }

    public OrganizationAlreadyExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

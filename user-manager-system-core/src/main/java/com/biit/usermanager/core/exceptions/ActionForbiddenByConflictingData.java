package com.biit.usermanager.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class ActionForbiddenByConflictingData extends NotFoundException {


    @Serial
    private static final long serialVersionUID = -3721266685002576741L;

    public ActionForbiddenByConflictingData(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ActionForbiddenByConflictingData(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public ActionForbiddenByConflictingData(Class<?> clazz) {
        this(clazz, "Role not found");
    }

    public ActionForbiddenByConflictingData(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}

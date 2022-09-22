package com.biit.usermanager.core.converters.models;

import com.biit.logger.ExceptionType;
import com.biit.usermanager.core.exceptions.UnexpectedValueException;

public class ConverterRequest<T> {
    public ConverterRequest(T entity) {
        this.entity = entity;
    }

    private T entity;

    public boolean hasEntity() {
        return entity != null;
    }

    public T getEntity() {
        if (entity == null) {
            throw new UnexpectedValueException(this.getClass(), "Entity could not be converted into a proper object.\n ", ExceptionType.WARNING);
        }
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}

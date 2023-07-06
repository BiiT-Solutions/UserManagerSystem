package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.User;

import java.util.Optional;

public class BasicUserConverterRequest extends ConverterRequest<User> {
    public BasicUserConverterRequest(User entity) {
        super(entity);
    }

    public BasicUserConverterRequest(Optional<User> entity) {
        super(entity);
    }
}

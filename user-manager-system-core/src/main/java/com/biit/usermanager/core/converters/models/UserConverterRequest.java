package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.User;

public class UserConverterRequest extends ConverterRequest<User> {
    public UserConverterRequest(User entity) {
        super(entity);
    }
}

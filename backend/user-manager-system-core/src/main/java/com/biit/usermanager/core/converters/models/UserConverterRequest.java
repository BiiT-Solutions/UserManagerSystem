package com.biit.usermanager.core.converters.models;

import com.biit.usermanager.persistence.User;

public class UserConverterRequest extends ConverterRequest<User> {
    public UserConverterRequest(User entity) {
        super(entity);
    }
}

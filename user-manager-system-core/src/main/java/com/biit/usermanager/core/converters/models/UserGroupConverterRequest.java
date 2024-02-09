package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.UserGroup;

public class UserGroupConverterRequest extends ConverterRequest<UserGroup> {
    public UserGroupConverterRequest(UserGroup entity) {
        super(entity);
    }
}

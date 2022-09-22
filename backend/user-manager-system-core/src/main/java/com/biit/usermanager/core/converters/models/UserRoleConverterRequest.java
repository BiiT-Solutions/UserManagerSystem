package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.UserRole;

public class UserRoleConverterRequest extends ConverterRequest<UserRole> {
    public UserRoleConverterRequest(UserRole entity) {
        super(entity);
    }
}

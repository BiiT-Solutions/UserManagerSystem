package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Role;

public class RoleConverterRequest extends ConverterRequest<Role> {
    public RoleConverterRequest(Role entity) {
        super(entity);
    }
}

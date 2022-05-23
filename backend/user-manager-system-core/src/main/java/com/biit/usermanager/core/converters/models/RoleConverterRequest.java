package com.biit.usermanager.core.converters.models;

import com.biit.usermanager.persistence.Role;

public class RoleConverterRequest extends ConverterRequest<Role> {
    public RoleConverterRequest(Role entity) {
        super(entity);
    }
}

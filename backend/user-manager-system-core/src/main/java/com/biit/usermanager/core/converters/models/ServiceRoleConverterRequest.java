package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.BackendServiceRole;

public class ServiceRoleConverterRequest extends ConverterRequest<BackendServiceRole> {
    public ServiceRoleConverterRequest(BackendServiceRole entity) {
        super(entity);
    }
}

package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.ServiceRole;

public class ServiceRoleConverterRequest extends ConverterRequest<ServiceRole> {
    public ServiceRoleConverterRequest(ServiceRole entity) {
        super(entity);
    }
}

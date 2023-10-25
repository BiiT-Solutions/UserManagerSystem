package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;

public class ApplicationServiceRoleConverterRequest extends ConverterRequest<ApplicationBackendServiceRole> {
    public ApplicationServiceRoleConverterRequest(ApplicationBackendServiceRole entity) {
        super(entity);
    }
}

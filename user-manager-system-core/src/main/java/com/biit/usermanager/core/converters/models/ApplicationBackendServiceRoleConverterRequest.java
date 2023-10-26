package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;

public class ApplicationBackendServiceRoleConverterRequest extends ConverterRequest<ApplicationBackendServiceRole> {
    public ApplicationBackendServiceRoleConverterRequest(ApplicationBackendServiceRole entity) {
        super(entity);
    }
}

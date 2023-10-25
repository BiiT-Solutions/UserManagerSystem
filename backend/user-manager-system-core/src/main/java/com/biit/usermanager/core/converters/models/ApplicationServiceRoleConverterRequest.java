package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationServiceRole;

public class ApplicationServiceRoleConverterRequest extends ConverterRequest<ApplicationServiceRole> {
    public ApplicationServiceRoleConverterRequest(ApplicationServiceRole entity) {
        super(entity);
    }
}

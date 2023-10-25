package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.ApplicationRole;

public class ApplicationRoleConverterRequest extends ConverterRequest<ApplicationRole> {
    public ApplicationRoleConverterRequest(ApplicationRole entity) {
        super(entity);
    }
}

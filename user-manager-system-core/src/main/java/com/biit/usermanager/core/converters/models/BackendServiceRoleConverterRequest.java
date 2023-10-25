package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.BackendServiceRole;

public class BackendServiceRoleConverterRequest extends ConverterRequest<BackendServiceRole> {
    public BackendServiceRoleConverterRequest(BackendServiceRole entity) {
        super(entity);
    }
}

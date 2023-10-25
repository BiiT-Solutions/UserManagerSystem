package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.BackendService;

public class ServiceConverterRequest extends ConverterRequest<BackendService> {
    public ServiceConverterRequest(BackendService entity) {
        super(entity);
    }
}

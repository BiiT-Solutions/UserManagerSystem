package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.BackendService;

public class BackendServiceConverterRequest extends ConverterRequest<BackendService> {
    public BackendServiceConverterRequest(BackendService entity) {
        super(entity);
    }
}

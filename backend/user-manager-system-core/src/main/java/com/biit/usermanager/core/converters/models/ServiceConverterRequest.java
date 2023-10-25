package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Service;

public class ServiceConverterRequest extends ConverterRequest<Service> {
    public ServiceConverterRequest(Service entity) {
        super(entity);
    }
}

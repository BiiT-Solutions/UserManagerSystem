package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Application;

public class ApplicationConverterRequest extends ConverterRequest<Application> {
    public ApplicationConverterRequest(Application entity) {
        super(entity);
    }
}

package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Application;

import java.util.Optional;

public class ApplicationConverterRequest extends ConverterRequest<Application> {
    public ApplicationConverterRequest(Application entity) {
        super(entity);
    }

    public ApplicationConverterRequest(Optional<Application> entity) {
        super(entity);
    }
}

package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Group;

import java.util.Optional;

public class GroupConverterRequest extends ConverterRequest<Group> {
    private final Application application;

    public GroupConverterRequest(Group entity) {
        super(entity);
        this.application = null;
    }

    public GroupConverterRequest(Group entity, Application application) {
        super(entity);
        this.application = application;
    }

    public GroupConverterRequest(Optional<Group> entity) {
        super(entity);
        this.application = null;
    }

    public Application getApplication() {
        return application;
    }
}

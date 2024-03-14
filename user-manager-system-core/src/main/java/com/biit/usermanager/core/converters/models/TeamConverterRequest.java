package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Team;

import java.util.Optional;

public class TeamConverterRequest extends ConverterRequest<Team> {
    private final Application application;

    public TeamConverterRequest(Team entity) {
        super(entity);
        this.application = null;
    }

    public TeamConverterRequest(Team entity, Application application) {
        super(entity);
        this.application = application;
    }

    public TeamConverterRequest(Optional<Team> entity) {
        super(entity);
        this.application = null;
    }

    public Application getApplication() {
        return application;
    }
}

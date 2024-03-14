package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.Team;

import java.util.Optional;

public class TeamConverterRequest extends ConverterRequest<Team> {
    private final Organization organization;

    public TeamConverterRequest(Team entity) {
        super(entity);
        this.organization = null;
    }

    public TeamConverterRequest(Team entity, Organization organization) {
        super(entity);
        this.organization = organization;
    }

    public TeamConverterRequest(Optional<Team> entity) {
        super(entity);
        this.organization = null;
    }

    public Organization getOrganization() {
        return organization;
    }
}

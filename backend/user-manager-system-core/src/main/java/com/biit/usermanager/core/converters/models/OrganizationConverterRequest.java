package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Organization;

import java.util.Optional;

public class OrganizationConverterRequest extends ConverterRequest<Organization> {
    public OrganizationConverterRequest(Organization entity) {
        super(entity);
    }

    public OrganizationConverterRequest(Optional<Organization> entity) {
        super(entity);
    }
}

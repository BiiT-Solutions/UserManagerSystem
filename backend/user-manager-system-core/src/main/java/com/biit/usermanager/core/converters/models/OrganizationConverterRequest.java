package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.Organization;

public class OrganizationConverterRequest extends ConverterRequest<Organization> {
    public OrganizationConverterRequest(Organization entity) {
        super(entity);
    }
}

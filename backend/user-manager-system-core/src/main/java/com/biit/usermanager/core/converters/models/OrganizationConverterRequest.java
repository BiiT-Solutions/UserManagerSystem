package com.biit.usermanager.core.converters.models;

import com.biit.usermanager.persistence.entities.Organization;

public class OrganizationConverterRequest extends ConverterRequest<Organization> {
    public OrganizationConverterRequest(Organization entity) {
        super(entity);
    }
}

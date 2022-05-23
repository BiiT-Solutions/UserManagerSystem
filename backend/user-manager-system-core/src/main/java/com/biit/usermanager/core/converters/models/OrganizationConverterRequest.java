package com.biit.usermanager.core.converters.models;

import com.biit.usermanager.persistence.Organization;
import com.biit.usermanager.persistence.User;

public class OrganizationConverterRequest extends ConverterRequest<Organization> {
    public OrganizationConverterRequest(Organization entity) {
        super(entity);
    }
}

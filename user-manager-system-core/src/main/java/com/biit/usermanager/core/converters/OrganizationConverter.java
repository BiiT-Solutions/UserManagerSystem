package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.persistence.entities.Organization;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrganizationConverter extends ElementConverter<Organization, OrganizationDTO, OrganizationConverterRequest> {


    @Override
    protected OrganizationDTO convertElement(OrganizationConverterRequest from) {
        final OrganizationDTO organizationDTO = new OrganizationDTO();
        BeanUtils.copyProperties(from.getEntity(), organizationDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        return organizationDTO;
    }

    @Override
    public Organization reverse(OrganizationDTO to) {
        if (to == null) {
            return null;
        }
        final Organization organization = new Organization();
        BeanUtils.copyProperties(to, organization, ConverterUtils.getNullPropertyNames(to));
        return organization;
    }
}

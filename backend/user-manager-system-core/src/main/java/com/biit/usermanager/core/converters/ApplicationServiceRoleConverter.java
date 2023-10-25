package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.ApplicationRoleConverterRequest;
import com.biit.usermanager.core.converters.models.ApplicationServiceRoleConverterRequest;
import com.biit.usermanager.core.converters.models.ServiceRoleConverterRequest;
import com.biit.usermanager.dto.ApplicationServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationServiceRoleIdDTO;
import com.biit.usermanager.persistence.entities.ApplicationServiceRole;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ApplicationServiceRoleConverter extends ElementConverter<ApplicationServiceRole, ApplicationServiceRoleDTO, ApplicationServiceRoleConverterRequest> {

    private final ApplicationRoleConverter applicationRoleConverter;
    private final ServiceRoleConverter serviceRoleConverter;

    public ApplicationServiceRoleConverter(ApplicationRoleConverter applicationRoleConverter, ServiceRoleConverter serviceRoleConverter) {
        this.applicationRoleConverter = applicationRoleConverter;
        this.serviceRoleConverter = serviceRoleConverter;
    }

    @Override
    protected ApplicationServiceRoleDTO convertElement(ApplicationServiceRoleConverterRequest from) {
        final ApplicationServiceRoleDTO applicationServiceRoleDTO = new ApplicationServiceRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), applicationServiceRoleDTO);
        applicationServiceRoleDTO.setId(new ApplicationServiceRoleIdDTO(
                applicationRoleConverter.convertElement(new ApplicationRoleConverterRequest(from.getEntity().getId().getApplicationRole())),
                serviceRoleConverter.convertElement(new ServiceRoleConverterRequest(from.getEntity().getId().getServiceRole())))
        );
        return applicationServiceRoleDTO;
    }

    @Override
    public ApplicationServiceRole reverse(ApplicationServiceRoleDTO to) {
        if (to == null) {
            return null;
        }
        final ApplicationServiceRole applicationServiceRole = new ApplicationServiceRole();
        BeanUtils.copyProperties(to, applicationServiceRole);
        return applicationServiceRole;
    }

}

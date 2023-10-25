package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.ApplicationRoleConverterRequest;
import com.biit.usermanager.core.converters.models.ApplicationServiceRoleConverterRequest;
import com.biit.usermanager.core.converters.models.ServiceRoleConverterRequest;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleIdDTO;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBackendServiceRoleConverter extends ElementConverter<ApplicationBackendServiceRole, ApplicationBackendServiceRoleDTO, ApplicationServiceRoleConverterRequest> {

    private final ApplicationRoleConverter applicationRoleConverter;
    private final BackendServiceRoleConverter backendServiceRoleConverter;

    public ApplicationBackendServiceRoleConverter(ApplicationRoleConverter applicationRoleConverter, BackendServiceRoleConverter backendServiceRoleConverter) {
        this.applicationRoleConverter = applicationRoleConverter;
        this.backendServiceRoleConverter = backendServiceRoleConverter;
    }

    @Override
    protected ApplicationBackendServiceRoleDTO convertElement(ApplicationServiceRoleConverterRequest from) {
        final ApplicationBackendServiceRoleDTO applicationBackendServiceRoleDTO = new ApplicationBackendServiceRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), applicationBackendServiceRoleDTO);
        applicationBackendServiceRoleDTO.setId(new ApplicationBackendServiceRoleIdDTO(
                applicationRoleConverter.convertElement(new ApplicationRoleConverterRequest(from.getEntity().getId().getApplicationRole())),
                backendServiceRoleConverter.convertElement(new ServiceRoleConverterRequest(from.getEntity().getId().getServiceRole())))
        );
        return applicationBackendServiceRoleDTO;
    }

    @Override
    public ApplicationBackendServiceRole reverse(ApplicationBackendServiceRoleDTO to) {
        if (to == null) {
            return null;
        }
        final ApplicationBackendServiceRole applicationBackendServiceRole = new ApplicationBackendServiceRole();
        BeanUtils.copyProperties(to, applicationBackendServiceRole);
        return applicationBackendServiceRole;
    }

}

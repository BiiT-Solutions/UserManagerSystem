package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.ServiceConverterRequest;
import com.biit.usermanager.core.converters.models.ServiceRoleConverterRequest;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.BackendServiceRoleIdDTO;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BackendServiceRoleConverter extends ElementConverter<BackendServiceRole, BackendServiceRoleDTO, ServiceRoleConverterRequest> {

    private final BackendServiceConverter backendServiceConverter;

    public BackendServiceRoleConverter(BackendServiceConverter backendServiceConverter) {
        this.backendServiceConverter = backendServiceConverter;
    }

    @Override
    protected BackendServiceRoleDTO convertElement(ServiceRoleConverterRequest from) {
        final BackendServiceRoleDTO backendServiceRoleDTO = new BackendServiceRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), backendServiceRoleDTO);

        backendServiceRoleDTO.setId(new BackendServiceRoleIdDTO(backendServiceConverter.convertElement(
                new ServiceConverterRequest(from.getEntity().getId().getService())), from.getEntity().getId().getName()));

        return backendServiceRoleDTO;
    }

    @Override
    public BackendServiceRole reverse(BackendServiceRoleDTO to) {
        if (to == null) {
            return null;
        }
        final BackendServiceRole backendServiceRole = new BackendServiceRole();
        BeanUtils.copyProperties(to, backendServiceRole);

        backendServiceRole.setId(new BackendServiceRoleId(backendServiceConverter.reverse(to.getId().getService()), to.getId().getName()));

        return backendServiceRole;
    }
}

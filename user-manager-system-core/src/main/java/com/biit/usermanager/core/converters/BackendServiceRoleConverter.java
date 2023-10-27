package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.BackendServiceConverterRequest;
import com.biit.usermanager.core.converters.models.BackendServiceRoleConverterRequest;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.BackendServiceRoleIdDTO;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BackendServiceRoleConverter extends ElementConverter<BackendServiceRole, BackendServiceRoleDTO, BackendServiceRoleConverterRequest> {

    private final BackendServiceConverter backendServiceConverter;

    public BackendServiceRoleConverter(BackendServiceConverter backendServiceConverter) {
        this.backendServiceConverter = backendServiceConverter;
    }

    @Override
    protected BackendServiceRoleDTO convertElement(BackendServiceRoleConverterRequest from) {
        final BackendServiceRoleDTO backendServiceRoleDTO = new BackendServiceRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), backendServiceRoleDTO);

        backendServiceRoleDTO.setId(new BackendServiceRoleIdDTO(backendServiceConverter.convertElement(
                new BackendServiceConverterRequest(from.getEntity().getId().getBackendService())), from.getEntity().getId().getName()));

        return backendServiceRoleDTO;
    }

    @Override
    public BackendServiceRole reverse(BackendServiceRoleDTO to) {
        if (to == null) {
            return null;
        }
        final BackendServiceRole backendServiceRole = new BackendServiceRole();
        BeanUtils.copyProperties(to, backendServiceRole);

        backendServiceRole.setId(new BackendServiceRoleId(backendServiceConverter.reverse(to.getId().getBackendService()), to.getId().getName()));

        return backendServiceRole;
    }
}

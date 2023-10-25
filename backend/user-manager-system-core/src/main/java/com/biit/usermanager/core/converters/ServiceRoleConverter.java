package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.ServiceConverterRequest;
import com.biit.usermanager.core.converters.models.ServiceRoleConverterRequest;
import com.biit.usermanager.dto.ServiceRoleDTO;
import com.biit.usermanager.dto.ServiceRoleIdDTO;
import com.biit.usermanager.persistence.entities.ServiceRole;
import com.biit.usermanager.persistence.entities.ServiceRoleId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ServiceRoleConverter extends ElementConverter<ServiceRole, ServiceRoleDTO, ServiceRoleConverterRequest> {

    private final ServiceConverter serviceConverter;

    public ServiceRoleConverter(ServiceConverter serviceConverter) {
        this.serviceConverter = serviceConverter;
    }

    @Override
    protected ServiceRoleDTO convertElement(ServiceRoleConverterRequest from) {
        final ServiceRoleDTO serviceRoleDTO = new ServiceRoleDTO();
        BeanUtils.copyProperties(from.getEntity(), serviceRoleDTO);

        serviceRoleDTO.setId(new ServiceRoleIdDTO(serviceConverter.convertElement(
                new ServiceConverterRequest(from.getEntity().getId().getService())), from.getEntity().getId().getName()));

        return serviceRoleDTO;
    }

    @Override
    public ServiceRole reverse(ServiceRoleDTO to) {
        if (to == null) {
            return null;
        }
        final ServiceRole serviceRole = new ServiceRole();
        BeanUtils.copyProperties(to, serviceRole);

        serviceRole.setId(new ServiceRoleId(serviceConverter.reverse(to.getId().getService()), to.getId().getName()));

        return serviceRole;
    }
}

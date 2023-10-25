package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.ServiceConverterRequest;
import com.biit.usermanager.dto.ServiceDTO;
import com.biit.usermanager.persistence.entities.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ServiceConverter extends ElementConverter<Service, ServiceDTO, ServiceConverterRequest> {


    @Override
    protected ServiceDTO convertElement(ServiceConverterRequest from) {
        final ServiceDTO serviceDTO = new ServiceDTO();
        BeanUtils.copyProperties(from.getEntity(), serviceDTO);
        return serviceDTO;
    }

    @Override
    public Service reverse(ServiceDTO to) {
        if (to == null) {
            return null;
        }
        final Service service = new Service();
        BeanUtils.copyProperties(to, service);
        return service;
    }
}

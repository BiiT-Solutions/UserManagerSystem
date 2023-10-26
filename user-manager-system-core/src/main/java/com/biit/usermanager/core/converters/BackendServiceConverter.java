package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.BackendServiceConverterRequest;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.persistence.entities.BackendService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BackendServiceConverter extends ElementConverter<BackendService, BackendServiceDTO, BackendServiceConverterRequest> {


    @Override
    protected BackendServiceDTO convertElement(BackendServiceConverterRequest from) {
        final BackendServiceDTO backendServiceDTO = new BackendServiceDTO();
        BeanUtils.copyProperties(from.getEntity(), backendServiceDTO);
        return backendServiceDTO;
    }

    @Override
    public BackendService reverse(BackendServiceDTO to) {
        if (to == null) {
            return null;
        }
        final BackendService backendService = new BackendService();
        BeanUtils.copyProperties(to, backendService);
        return backendService;
    }
}

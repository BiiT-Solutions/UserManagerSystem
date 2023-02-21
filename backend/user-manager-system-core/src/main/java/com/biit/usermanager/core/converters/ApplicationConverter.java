package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.persistence.entities.Application;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConverter extends ElementConverter<Application, ApplicationDTO, ApplicationConverterRequest> {


    @Override
    protected ApplicationDTO convertElement(ApplicationConverterRequest from) {
        final ApplicationDTO applicationDTO = new ApplicationDTO();
        BeanUtils.copyProperties(from.getEntity(), applicationDTO);
        return applicationDTO;
    }

    @Override
    public Application reverse(ApplicationDTO to) {
        if (to == null) {
            return null;
        }
        final Application application = new Application();
        BeanUtils.copyProperties(to, application);
        return application;
    }
}

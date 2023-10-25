package com.biit.usermanager.core.controller;


import com.biit.server.controller.BasicElementController;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ApplicationController extends BasicElementController<Application, Long, ApplicationDTO, ApplicationRepository,
        ApplicationProvider, ApplicationConverterRequest, ApplicationConverter> {

    @Autowired
    protected ApplicationController(ApplicationProvider provider, ApplicationConverter converter) {
        super(provider, converter);
    }

    @Override
    protected ApplicationConverterRequest createConverterRequest(Application entity) {
        return new ApplicationConverterRequest(entity);
    }

    public ApplicationDTO getByName(String applicationName) {
        return getConverter().convert(new ApplicationConverterRequest(getProvider().findByName(applicationName).orElse(null)));
    }
}

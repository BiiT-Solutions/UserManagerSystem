package com.biit.usermanager.core.controller;


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.kafka.ApplicationEventSender;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ApplicationController extends KafkaElementController<Application, String, ApplicationDTO, ApplicationRepository,
        ApplicationProvider, ApplicationConverterRequest, ApplicationConverter> {

    @Autowired
    protected ApplicationController(ApplicationProvider provider, ApplicationConverter converter, ApplicationEventSender eventSender) {
        super(provider, converter, eventSender);
    }

    @Override
    protected ApplicationConverterRequest createConverterRequest(Application entity) {
        return new ApplicationConverterRequest(entity);
    }

    public ApplicationDTO getByName(String applicationName) {
        return getConverter().convert(new ApplicationConverterRequest(getProvider().findByName(applicationName).orElse(null)));
    }
}

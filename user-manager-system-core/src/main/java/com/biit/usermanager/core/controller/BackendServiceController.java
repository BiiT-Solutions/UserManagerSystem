package com.biit.usermanager.core.controller;

import com.biit.kafka.controller.KafkaElementController;
import com.biit.usermanager.core.converters.BackendServiceConverter;
import com.biit.usermanager.core.converters.models.BackendServiceConverterRequest;
import com.biit.usermanager.core.exceptions.BackendServiceNotFoundException;
import com.biit.usermanager.core.kafka.BackendServiceEventSender;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.repositories.BackendServiceRepository;
import org.springframework.stereotype.Controller;

@Controller
public class BackendServiceController extends KafkaElementController<BackendService, String, BackendServiceDTO, BackendServiceRepository,
        BackendServiceProvider, BackendServiceConverterRequest, BackendServiceConverter> {

    protected BackendServiceController(BackendServiceProvider provider, BackendServiceConverter converter, BackendServiceEventSender eventSender) {
        super(provider, converter, eventSender);
    }

    @Override
    protected BackendServiceConverterRequest createConverterRequest(BackendService backendService) {
        return new BackendServiceConverterRequest(backendService);
    }

    public BackendServiceDTO findByName(String backendServiceName) {
        return convert(getProvider().findByName(backendServiceName).orElseThrow(() ->
                new BackendServiceNotFoundException(this.getClass(), "No backend service with name '" + backendServiceName + "' exists on the system")));
    }
}

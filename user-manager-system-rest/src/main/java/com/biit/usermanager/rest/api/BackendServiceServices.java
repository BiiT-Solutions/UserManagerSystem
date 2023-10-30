package com.biit.usermanager.rest.api;

import com.biit.server.rest.CreatedElementServices;
import com.biit.usermanager.core.controller.BackendServiceController;
import com.biit.usermanager.core.converters.BackendServiceConverter;
import com.biit.usermanager.core.converters.models.BackendServiceConverterRequest;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.repositories.BackendServiceRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend-services")
public class BackendServiceServices extends CreatedElementServices<
        BackendService,
        String,
        BackendServiceDTO,
        BackendServiceRepository,
        BackendServiceProvider,
        BackendServiceConverterRequest,
        BackendServiceConverter,
        BackendServiceController
        > {

    public BackendServiceServices(BackendServiceController backendServiceController) {
        super(backendServiceController);
    }

}

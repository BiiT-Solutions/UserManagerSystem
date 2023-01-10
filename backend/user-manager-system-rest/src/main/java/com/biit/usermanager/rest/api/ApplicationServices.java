package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.models.ApplicationDTO;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class ApplicationServices extends BasicServices<Application, ApplicationDTO, ApplicationRepository,
        ApplicationProvider, ApplicationConverterRequest, ApplicationConverter, ApplicationController> {

    public ApplicationServices(ApplicationController controller) {
        super(controller);
    }
}

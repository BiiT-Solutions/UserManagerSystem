package com.biit.usermanager.rest.api;

import com.biit.server.rest.CreatedElementServices;
import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationBackendServiceRoleConverterRequest;
import com.biit.usermanager.core.providers.ApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application-backend-service-roles")
public class ApplicationBackendServiceRoleServices extends CreatedElementServices<ApplicationBackendServiceRole, ApplicationBackendServiceRoleDTO,
        ApplicationBackendServiceRoleProvider, ApplicationBackendServiceRoleConverterRequest,
        ApplicationBackendServiceRoleConverter, ApplicationBackendServiceRoleController> {

    public ApplicationBackendServiceRoleServices(ApplicationBackendServiceRoleController applicationBackendServiceRoleController) {
        super(applicationBackendServiceRoleController);
    }


}

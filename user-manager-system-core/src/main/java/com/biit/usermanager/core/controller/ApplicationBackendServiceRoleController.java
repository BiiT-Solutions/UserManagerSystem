package com.biit.usermanager.core.controller;


import com.biit.kafka.controllers.KafkaCreatedElementController;
import com.biit.server.logger.DtoControllerLogger;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationBackendServiceRoleConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationBackendServiceNotFoundException;
import com.biit.usermanager.core.kafka.ApplicationBackendServiceRoleEventSender;
import com.biit.usermanager.core.providers.ApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ApplicationBackendServiceRoleController extends KafkaCreatedElementController<ApplicationBackendServiceRole,
        ApplicationBackendServiceRoleId, ApplicationBackendServiceRoleDTO, ApplicationBackendServiceRoleRepository,
        ApplicationBackendServiceRoleProvider, ApplicationBackendServiceRoleConverterRequest, ApplicationBackendServiceRoleConverter> {


    @Autowired
    protected ApplicationBackendServiceRoleController(ApplicationBackendServiceRoleProvider provider,
                                                      ApplicationBackendServiceRoleConverter converter,
                                                      ApplicationBackendServiceRoleEventSender eventSender) {
        super(provider, converter, eventSender);
    }

    @Override
    protected ApplicationBackendServiceRoleConverterRequest createConverterRequest(ApplicationBackendServiceRole entity) {
        return new ApplicationBackendServiceRoleConverterRequest(entity);
    }

    public ApplicationBackendServiceRoleDTO findByApplicationRoleAndServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        return convert(getProvider().findByApplicationRoleAndServiceRole(applicationName, applicationRoleName, backendServiceName, backendServiceRoleName)
                .orElseThrow(() ->
                        new ApplicationBackendServiceNotFoundException(this.getClass(), "No role found for application '" + applicationName + "' with role '"
                                + applicationRoleName + "' and backend service '" + backendServiceName + "' role '" + backendServiceRoleName + "'")));
    }

    public ApplicationBackendServiceRoleDTO findByApplicationRoleAndServiceRole(ApplicationRole applicationRole, BackendServiceRole backendServiceRole) {
        return convert(getProvider().findByApplicationRoleAndServiceRole(applicationRole, backendServiceRole).orElseThrow(() ->
                new ApplicationBackendServiceNotFoundException(this.getClass(), "No role found for application role '"
                        + applicationRole + "' and backend service role '" + backendServiceRole + "'")));
    }

    public List<ApplicationBackendServiceRoleDTO> findByApplicationRole(ApplicationRole applicationRole) {
        return convertAll(getProvider().findByApplicationRole(applicationRole));
    }

    public List<ApplicationBackendServiceRoleDTO> findByServiceRole(BackendServiceRole backendServiceRole) {
        return convertAll(getProvider().findByServiceRole(backendServiceRole));
    }

    public void deleteByApplicationRoleAndServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName, String deletedBy) {
        final ApplicationBackendServiceRole applicationBackendServiceRole = getProvider().findByApplicationRoleAndServiceRole(
                applicationName, applicationRoleName, backendServiceName, backendServiceRoleName).orElseThrow(() ->
                new ApplicationBackendServiceNotFoundException(this.getClass(), "No role found for application '" + applicationName + "' with role '"
                        + applicationRoleName + "' and backend service '" + backendServiceName + "' role '" + backendServiceRoleName + "'"));
        DtoControllerLogger.info(this.getClass(), "Entity '{}' deleted by '{}'.", applicationBackendServiceRole, deletedBy);
        getProvider().delete(applicationBackendServiceRole);
    }
}

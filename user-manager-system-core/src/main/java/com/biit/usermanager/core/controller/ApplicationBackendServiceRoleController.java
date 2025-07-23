package com.biit.usermanager.core.controller;


import com.biit.kafka.controllers.KafkaCreatedElementController;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.logger.DtoControllerLogger;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationBackendServiceRoleConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationBackendServiceNotFoundException;
import com.biit.usermanager.core.kafka.ApplicationBackendServiceRoleEventSender;
import com.biit.usermanager.core.kafka.UserEventSender;
import com.biit.usermanager.core.providers.ApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserGroupApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class ApplicationBackendServiceRoleController extends KafkaCreatedElementController<ApplicationBackendServiceRole,
        ApplicationBackendServiceRoleId, ApplicationBackendServiceRoleDTO, ApplicationBackendServiceRoleRepository,
        ApplicationBackendServiceRoleProvider, ApplicationBackendServiceRoleConverterRequest, ApplicationBackendServiceRoleConverter> {

    private final UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider;
    private final UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider;

    private final UserEventSender userEventSender;


    @Autowired
    protected ApplicationBackendServiceRoleController(ApplicationBackendServiceRoleProvider provider,
                                                      ApplicationBackendServiceRoleConverter converter,
                                                      ApplicationBackendServiceRoleEventSender eventSender,
                                                      UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider,
                                                      UserEventSender userEventSender,
                                                      List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider,
                                                      UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider) {
        super(provider, converter, eventSender, userOrganizationProvider);
        this.userApplicationBackendServiceRoleProvider = userApplicationBackendServiceRoleProvider;
        this.userEventSender = userEventSender;
        this.userGroupApplicationBackendServiceRoleProvider = userGroupApplicationBackendServiceRoleProvider;
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


    @Transactional
    public void deleteByApplicationRoleAndServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName, String deletedBy) {
        final ApplicationBackendServiceRole applicationBackendServiceRole = getProvider().findByApplicationRoleAndServiceRole(
                applicationName, applicationRoleName, backendServiceName, backendServiceRoleName).orElseThrow(() ->
                new ApplicationBackendServiceNotFoundException(this.getClass(), "No role found for application '" + applicationName + "' with role '"
                        + applicationRoleName + "' and backend service '" + backendServiceName + "' role '" + backendServiceRoleName + "'"));
        DtoControllerLogger.info(this.getClass(), "Entity '{}' deleted by '{}'.", applicationBackendServiceRole, deletedBy);
        delete(convert(applicationBackendServiceRole), deletedBy);
    }


    @Override
    @Transactional
    public void delete(ApplicationBackendServiceRoleDTO entity, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider
                .findBy(entity.getId().getApplicationRole().getId().getApplication().getName(),
                        entity.getId().getApplicationRole().getId().getRole().getName(),
                        entity.getId().getBackendServiceRole().getId().getBackendService().getName(),
                        entity.getId().getBackendServiceRole().getId().getName());

        userApplicationBackendServiceRoleProvider.deleteAll(usersToInform);

        userGroupApplicationBackendServiceRoleProvider.deleteBy(
                entity.getId().getApplicationRole().getId().getApplication().getName(),
                entity.getId().getApplicationRole().getId().getRole().getName(),
                entity.getId().getBackendServiceRole().getId().getBackendService().getName(),
                entity.getId().getBackendServiceRole().getId().getName());

        super.delete(entity, deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }

    @Override
    @Transactional
    public void deleteById(ApplicationBackendServiceRoleId id, String deletedBy) {

        final ApplicationBackendServiceRole entity = getProvider().findById(id).orElseThrow(() ->
                new NotFoundException(this.getClass(), "No application backend service role exists with id '" + id + "'."));

        delete(convert(entity), deletedBy);
    }

    @Override
    public void deleteAll(String deletedBy) {
        //Send events.
        final List<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider.getAll();

        userGroupApplicationBackendServiceRoleProvider.deleteAll();

        userApplicationBackendServiceRoleProvider.deleteAll(usersToInform);

        super.deleteAll(deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }
}

package com.biit.usermanager.core.controller;

import com.biit.kafka.controllers.KafkaCreatedElementController;
import com.biit.server.logger.DtoControllerLogger;
import com.biit.usermanager.core.converters.BackendServiceRoleConverter;
import com.biit.usermanager.core.converters.models.BackendServiceRoleConverterRequest;
import com.biit.usermanager.core.exceptions.BackendServiceNotFoundException;
import com.biit.usermanager.core.exceptions.BackendServiceRoleNotFoundException;
import com.biit.usermanager.core.exceptions.RoleNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.kafka.BackendServiceRoleEventSender;
import com.biit.usermanager.core.kafka.UserEventSender;
import com.biit.usermanager.core.providers.ApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.core.providers.BackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.repositories.BackendServiceRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class BackendServiceRoleController extends KafkaCreatedElementController<BackendServiceRole, BackendServiceRoleId,
        BackendServiceRoleDTO, BackendServiceRoleRepository,
        BackendServiceRoleProvider, BackendServiceRoleConverterRequest, BackendServiceRoleConverter> {

    private final BackendServiceProvider backendServiceProvider;

    private final UserProvider userProvider;

    private final UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider;
    private final ApplicationBackendServiceRoleProvider applicationBackendServiceRoleProvider;

    private final UserEventSender userEventSender;

    protected BackendServiceRoleController(BackendServiceRoleProvider provider, BackendServiceRoleConverter converter,
                                           BackendServiceProvider backendServiceProvider,
                                           UserProvider userProvider,
                                           BackendServiceRoleEventSender eventSender,
                                           UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider,
                                           ApplicationBackendServiceRoleProvider applicationBackendServiceRoleProvider, UserEventSender userEventSender) {
        super(provider, converter, eventSender);
        this.backendServiceProvider = backendServiceProvider;
        this.userProvider = userProvider;
        this.userApplicationBackendServiceRoleProvider = userApplicationBackendServiceRoleProvider;
        this.applicationBackendServiceRoleProvider = applicationBackendServiceRoleProvider;
        this.userEventSender = userEventSender;
    }

    @Override
    protected BackendServiceRoleConverterRequest createConverterRequest(BackendServiceRole backendServiceRole) {
        return new BackendServiceRoleConverterRequest(backendServiceRole);
    }

    public List<BackendServiceRoleDTO> findByService(String backendServiceName) {
        final BackendService backendService = backendServiceProvider.findByName(backendServiceName).orElseThrow(() ->
                new BackendServiceNotFoundException(this.getClass(), "No backend service with name '" + backendServiceName + "' exists on the system"));
        return convertAll(getProvider().findByBackendService(backendService));
    }

    public List<BackendServiceRoleDTO> findByName(String name) {
        return convertAll(getProvider().findByName(name));
    }

    public BackendServiceRoleDTO findByServiceAndRole(String backendServiceName, String roleName) {
        final BackendService backendService = backendServiceProvider.findByName(backendServiceName).orElseThrow(() ->
                new BackendServiceNotFoundException(this.getClass(), "No backend service with name '" + backendServiceName + "' exists on the system"));
        return convert(getProvider().findByBackendServiceAndName(backendService, roleName).orElseThrow(() ->
                new RoleNotFoundException(this.getClass(), "")));
    }

    public List<BackendServiceRoleDTO> findBy(String username) {
        final User user = userProvider.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No users found with username '" + username + "'."));
        final List<BackendServiceRoleId> backendServiceRoleId = user.getApplicationBackendServiceRoles().stream()
                .map(applicationBackendServiceRole -> applicationBackendServiceRole.getId().getBackendServiceRole().getId()).toList();
        return convertAll(getProvider().findByIdIn(backendServiceRoleId));
    }

    public List<BackendServiceRoleDTO> findBy(String username, String applicationName) {
        if (applicationName == null) {
            return findBy(username);
        }
        final User user = userProvider.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No users found with username '" + username + "'."));
        final Set<UserApplicationBackendServiceRole> userApplicationBackendServiceRoles = userApplicationBackendServiceRoleProvider.findBy(user.getId(),
                applicationName);

        final List<BackendServiceRoleId> backendServiceRoleId = userApplicationBackendServiceRoles.stream().map(userApplicationBackendServiceRole ->
                new BackendServiceRole(
                        new BackendService(userApplicationBackendServiceRole.getId().getBackendServiceName()),
                        userApplicationBackendServiceRole.getId().getBackendServiceRole()).getId()).toList();

        return convertAll(getProvider().findByIdIn(backendServiceRoleId));
    }


    public BackendServiceRoleDTO get(String backendServiceName, String roleName) {
        return convert(getProvider().findByBackendServiceAndName(backendServiceName, roleName).orElseThrow(() ->
                new BackendServiceRoleNotFoundException(this.getClass(),
                        "No backend service role defined for service '" + backendServiceName + "' and role '" + roleName + "'.")));
    }


    public void delete(String backendServiceName, String roleName, String deletedBy) {
        final BackendServiceRole backendServiceRole = getProvider().findByBackendServiceAndName(backendServiceName, roleName).orElseThrow(() ->
                new BackendServiceRoleNotFoundException(this.getClass(),
                        "No backend service role defined for service '" + backendServiceName + "' and role '" + roleName + "'."));
        getProvider().delete(backendServiceRole);
        DtoControllerLogger.info(this.getClass(), "Entity '{}' deleted by '{}'.", backendServiceRole, deletedBy);
    }

    public List<BackendServiceRoleDTO> findByApplicationAndRole(String applicationName, String applicationRoleName) {
        return convertAll(getProvider().findByApplicationAndRole(applicationName, applicationRoleName));
    }

    @Override
    @Transactional
    public void delete(BackendServiceRoleDTO entity, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider
                .findByBackendServiceNameAndBackendServiceRole(entity.getId().getBackendService().getName(), entity.getId().getName());

        super.delete(entity, deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }

    @Override
    public void deleteById(BackendServiceRoleId id, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider
                .findByBackendServiceNameAndBackendServiceRole(id.getBackendService().getName(), id.getName());

        super.deleteById(id, deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }

    @Override
    public void deleteAll(String deletedBy) {
        //Send events.
        final List<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider.getAll();

        super.deleteAll(deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }
}

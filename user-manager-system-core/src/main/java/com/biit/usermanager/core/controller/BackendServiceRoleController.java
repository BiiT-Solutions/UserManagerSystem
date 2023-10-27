package com.biit.usermanager.core.controller;

import com.biit.server.controller.CreatedElementController;
import com.biit.usermanager.core.converters.BackendServiceRoleConverter;
import com.biit.usermanager.core.converters.models.BackendServiceRoleConverterRequest;
import com.biit.usermanager.core.exceptions.BackendServiceNotFoundException;
import com.biit.usermanager.core.exceptions.RoleNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.core.providers.BackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.BackendServiceRoleRepository;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class BackendServiceRoleController extends CreatedElementController<BackendServiceRole, BackendServiceRoleId,
        BackendServiceRoleDTO, BackendServiceRoleRepository,
        BackendServiceRoleProvider, BackendServiceRoleConverterRequest, BackendServiceRoleConverter> {

    private final BackendServiceProvider backendServiceProvider;

    private final UserProvider userProvider;

    protected BackendServiceRoleController(BackendServiceRoleProvider provider, BackendServiceRoleConverter converter,
                                           BackendServiceProvider backendServiceProvider, UserProvider userProvider) {
        super(provider, converter);
        this.backendServiceProvider = backendServiceProvider;
        this.userProvider = userProvider;
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
        final List<BackendServiceRoleId> backendServiceRoleId = user.getApplicationBackendServiceRole().stream()
                .map(applicationBackendServiceRole -> applicationBackendServiceRole.getId().getBackendServiceRole().getId()).toList();
        return convertAll(getProvider().findByIdIn(backendServiceRoleId));
    }

    public List<BackendServiceRoleDTO> findBy(String username, String groupName, String applicationName) {
        if (applicationName == null) {
            return findBy(username);
        }
        final User user = userProvider.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No users found with username '" + username + "'."));
        final List<BackendServiceRoleId> backendServiceRoleId = user.getApplicationBackendServiceRole().stream()
                .filter(applicationBackendServiceRole ->
                        Objects.equals(applicationBackendServiceRole.getId().getApplicationRole().getId().getApplication().getName(), applicationName))
                .map(applicationBackendServiceRole -> applicationBackendServiceRole.getId().getBackendServiceRole().getId()).toList();
        return convertAll(getProvider().findByIdIn(backendServiceRoleId));
    }
}

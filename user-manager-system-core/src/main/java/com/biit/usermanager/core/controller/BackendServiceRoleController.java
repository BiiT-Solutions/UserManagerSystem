package com.biit.usermanager.core.controller;

import com.biit.server.controller.CreatedElementController;
import com.biit.usermanager.core.converters.BackendServiceRoleConverter;
import com.biit.usermanager.core.converters.models.BackendServiceRoleConverterRequest;
import com.biit.usermanager.core.exceptions.BackendServiceNotFoundException;
import com.biit.usermanager.core.exceptions.RoleNotFoundException;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.core.providers.BackendServiceRoleProvider;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.BackendServiceRoleRepository;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BackendServiceRoleController extends CreatedElementController<BackendServiceRole, BackendServiceRoleId,
        BackendServiceRoleDTO, BackendServiceRoleRepository,
        BackendServiceRoleProvider, BackendServiceRoleConverterRequest, BackendServiceRoleConverter> {

    private final BackendServiceProvider backendServiceProvider;

    protected BackendServiceRoleController(BackendServiceRoleProvider provider, BackendServiceRoleConverter converter,
                                           BackendServiceProvider backendServiceProvider) {
        super(provider, converter);
        this.backendServiceProvider = backendServiceProvider;
    }

    @Override
    protected BackendServiceRoleConverterRequest createConverterRequest(BackendServiceRole backendServiceRole) {
        return new BackendServiceRoleConverterRequest(backendServiceRole);
    }

    public List<BackendServiceRoleDTO> findByService(String backendServiceName) {
        final BackendService backendService = backendServiceProvider.findByName(backendServiceName).orElseThrow(() ->
                new BackendServiceNotFoundException(this.getClass(), "No backend service with name '" + backendServiceName + "' exists on the system"));
        return convertAll(getProvider().findByService(backendService));
    }

    public List<BackendServiceRoleDTO> findByName(String name) {
        return convertAll(getProvider().findByName(name));
    }

    public BackendServiceRoleDTO findByServiceAndName(String backendServiceName, String name) {
        final BackendService backendService = backendServiceProvider.findByName(backendServiceName).orElseThrow(() ->
                new BackendServiceNotFoundException(this.getClass(), "No backend service with name '" + backendServiceName + "' exists on the system"));
        return convert(getProvider().findByServiceAndName(backendService, name).orElseThrow(() ->
                new RoleNotFoundException(this.getClass(), "")));
    }
}

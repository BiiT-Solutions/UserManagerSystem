package com.biit.usermanager.core.controller;


import com.biit.server.controller.CreatedElementController;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.RoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationBackendServiceRoleConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationBackendServiceNotFoundException;
import com.biit.usermanager.core.providers.ApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.RoleProvider;
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
public class ApplicationBackendServiceRoleController extends CreatedElementController<ApplicationBackendServiceRole,
        ApplicationBackendServiceRoleId, ApplicationBackendServiceRoleDTO, ApplicationBackendServiceRoleRepository,
        ApplicationBackendServiceRoleProvider, ApplicationBackendServiceRoleConverterRequest, ApplicationBackendServiceRoleConverter> {

    private final ApplicationProvider applicationProvider;

    private final ApplicationConverter applicationConverter;

    private final RoleProvider roleProvider;

    private final RoleConverter roleConverter;

    @Autowired
    protected ApplicationBackendServiceRoleController(ApplicationBackendServiceRoleProvider provider,
                                                      ApplicationBackendServiceRoleConverter converter, RoleProvider roleProvider,
                                                      ApplicationProvider applicationProvider, ApplicationConverter applicationConverter,
                                                      RoleConverter roleConverter) {
        super(provider, converter);
        this.roleConverter = roleConverter;
        this.roleProvider = roleProvider;
        this.applicationProvider = applicationProvider;
        this.applicationConverter = applicationConverter;
    }

    @Override
    protected ApplicationBackendServiceRoleConverterRequest createConverterRequest(ApplicationBackendServiceRole entity) {
        return new ApplicationBackendServiceRoleConverterRequest(entity);
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
}

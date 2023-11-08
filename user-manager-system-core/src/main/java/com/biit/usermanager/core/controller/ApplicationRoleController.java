package com.biit.usermanager.core.controller;


import com.biit.server.controller.CreatedElementController;
import com.biit.server.logger.DtoControllerLogger;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.ApplicationRoleConverter;
import com.biit.usermanager.core.converters.RoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationRoleConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationNotFoundException;
import com.biit.usermanager.core.exceptions.ApplicationRoleNotFoundException;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.ApplicationRoleProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ApplicationRoleController extends CreatedElementController<ApplicationRole, ApplicationRoleId, ApplicationRoleDTO, ApplicationRoleRepository,
        ApplicationRoleProvider, ApplicationRoleConverterRequest, ApplicationRoleConverter> {

    private final ApplicationProvider applicationProvider;

    private final ApplicationConverter applicationConverter;

    private final RoleConverter roleConverter;

    @Autowired
    protected ApplicationRoleController(ApplicationRoleProvider provider, ApplicationRoleConverter converter,
                                        ApplicationProvider applicationProvider, ApplicationConverter applicationConverter,
                                        RoleConverter roleConverter) {
        super(provider, converter);
        this.roleConverter = roleConverter;
        this.applicationProvider = applicationProvider;
        this.applicationConverter = applicationConverter;
    }

    @Override
    protected ApplicationRoleConverterRequest createConverterRequest(ApplicationRole entity) {
        return new ApplicationRoleConverterRequest(entity);
    }

    public List<ApplicationRoleDTO> getByApplication(String applicationName) {
        return convertAll(getProvider().findByApplication(applicationProvider.findByName(applicationName).orElseThrow(() ->
                new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found."))));
    }

    public List<ApplicationRoleDTO> getByApplication(ApplicationDTO application) {
        return convertAll(getProvider().findByApplication(applicationConverter.reverse(application)));
    }

    public List<ApplicationRoleDTO> getByRole(String roleName) {
        return convertAll(getProvider().findByRoleId(roleName));
    }

    public List<ApplicationRoleDTO> getByRole(RoleDTO role) {
        return convertAll(getProvider().findByRole(roleConverter.reverse(role)));
    }

    public ApplicationRoleDTO getByApplicationAndRole(String applicationName, String roleName) {
        return convert(getProvider().findByApplicationIdAndRoleId(applicationName, roleName).orElseThrow(
                () -> new ApplicationRoleNotFoundException(this.getClass(),
                        "No role exists for application '" + applicationName + "' with name '" + roleName + "'.")));
    }

    public void deleteByApplicationAndRole(String applicationName, String roleName, String deletedBy) {
        final ApplicationRole applicationRole = getProvider().findByApplicationIdAndRoleId(applicationName, roleName).orElseThrow(
                () -> new ApplicationRoleNotFoundException(this.getClass(),
                        "No role exists for application '" + applicationName + "' with name '" + roleName + "'."));
        DtoControllerLogger.info(this.getClass(), "Entity '{}' deleted by '{}'.", applicationRole, deletedBy);
        getProvider().delete(applicationRole);
    }
}

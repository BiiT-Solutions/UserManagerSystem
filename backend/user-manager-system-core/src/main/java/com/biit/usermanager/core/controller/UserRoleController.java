package com.biit.usermanager.core.controller;


import com.biit.logger.ExceptionType;
import com.biit.server.controller.BasicInsertableController;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.OrganizationConverter;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.UserRoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.converters.models.UserRoleConverterRequest;
import com.biit.usermanager.core.exceptions.OrganizationNotFoundException;
import com.biit.usermanager.core.exceptions.RoleNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.*;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.persistence.entities.*;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserRoleController extends BasicInsertableController<UserRole, UserRoleDTO, UserRoleRepository,
        UserRoleProvider, UserRoleConverterRequest, UserRoleConverter> {
    private final UserConverter userConverter;
    private final OrganizationConverter organizationConverter;

    private final UserProvider userProvider;

    private final RoleProvider roleProvider;

    private final OrganizationProvider organizationProvider;

    private final ApplicationProvider applicationProvider;

    private final ApplicationConverter applicationConverter;

    @Autowired
    protected UserRoleController(UserRoleProvider provider, UserRoleConverter converter, UserConverter userConverter,
                                 OrganizationConverter organizationConverter, UserProvider userProvider,
                                 RoleProvider roleProvider, OrganizationProvider organizationProvider,
                                 ApplicationProvider applicationProvider, ApplicationConverter applicationConverter) {
        super(provider, converter);
        this.userConverter = userConverter;
        this.organizationConverter = organizationConverter;
        this.userProvider = userProvider;
        this.roleProvider = roleProvider;
        this.organizationProvider = organizationProvider;
        this.applicationProvider = applicationProvider;
        this.applicationConverter = applicationConverter;
    }

    @Override
    protected UserRoleConverterRequest createConverterRequest(UserRole entity) {
        return new UserRoleConverterRequest(entity);
    }

    public List<UserRoleDTO> getByUser(String username) {
        final User user = userProvider.findByUsername(username).orElseThrow(() -> new UserNotFoundException(getClass(),
                "User with username '" + username + "' not found.", ExceptionType.WARNING));
        return converter.convertAll(provider.findByUser(user).stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<UserRoleDTO> getByOrganization(String organizationName) {
        final Organization organization = organizationProvider.findByName(organizationName).orElseThrow(() -> new OrganizationNotFoundException(getClass(),
                "Organization with name '" + organizationName + "' not found.", ExceptionType.WARNING));
        return converter.convertAll(provider.findByOrganization(organization).stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<UserRoleDTO> getByUserAndOrganizationAndApplication(String username, String organizationName, String applicationName) {
        final User user = userProvider.findByUsername(username).orElseThrow(() -> new UserNotFoundException(getClass(),
                "User with username '" + username + "' not found.",
                ExceptionType.INFO));
        final Organization organization = organizationProvider.findByName(organizationName).orElse(null);
        final Application application = applicationProvider.findByName(applicationName).orElse(null);
        return getByUserAndOrganizationAndApplication(userConverter.convert(new UserConverterRequest(user)),
                organizationConverter.convert(new OrganizationConverterRequest(organization)),
                applicationConverter.convert(new ApplicationConverterRequest(application)));
    }

    public List<UserRoleDTO> getByUserAndOrganizationAndApplication(UserDTO userDTO, OrganizationDTO organizationDTO, ApplicationDTO applicationDTO) {
        return converter.convertAll(provider.findByUserAndOrganizationAndApplication(userConverter.reverse(userDTO),
                        organizationConverter.reverse(organizationDTO), applicationConverter.reverse(applicationDTO)).stream()
                .map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<UserRoleDTO> getByUserAndRole(String organizationName, String roleName) {
        final Organization organization = organizationProvider.findByName(organizationName).orElseThrow(() -> new OrganizationNotFoundException(getClass(),
                "Organization with name '" + roleName + "' not found.", ExceptionType.WARNING));
        final Role role = roleProvider.findByName(roleName).orElseThrow(() -> new RoleNotFoundException(getClass(),
                "Role with name '" + roleName + "' not found.", ExceptionType.WARNING));
        return converter.convertAll(provider.findByOrganizationAndRole(organization, role)
                .stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }
}

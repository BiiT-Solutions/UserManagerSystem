package com.biit.usermanager.core.controller;


import com.biit.logger.ExceptionType;
import com.biit.server.controller.BasicElementController;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.GroupConverter;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.UserRoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.converters.models.GroupConverterRequest;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.converters.models.UserRoleConverterRequest;
import com.biit.usermanager.core.exceptions.GroupNotFoundException;
import com.biit.usermanager.core.exceptions.RoleNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.GroupProvider;
import com.biit.usermanager.core.providers.RoleProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.core.providers.UserRoleProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Group;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserRoleController extends BasicElementController<UserRole, UserRoleDTO, UserRoleRepository,
        UserRoleProvider, UserRoleConverterRequest, UserRoleConverter> {
    private final UserConverter userConverter;
    private final GroupConverter groupConverter;

    private final UserProvider userProvider;

    private final RoleProvider roleProvider;

    private final GroupProvider groupProvider;

    private final ApplicationProvider applicationProvider;

    private final ApplicationConverter applicationConverter;

    @Autowired
    protected UserRoleController(UserRoleProvider provider, UserRoleConverter converter, UserConverter userConverter,
                                 GroupConverter groupConverter, UserProvider userProvider,
                                 RoleProvider roleProvider, GroupProvider groupProvider,
                                 ApplicationProvider applicationProvider, ApplicationConverter applicationConverter) {
        super(provider, converter);
        this.userConverter = userConverter;
        this.groupConverter = groupConverter;
        this.userProvider = userProvider;
        this.roleProvider = roleProvider;
        this.groupProvider = groupProvider;
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
        return getConverter().convertAll(getProvider().findByUser(user).stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<UserRoleDTO> getByGroup(String groupName) {
        System.out.println("#######################3 " + groupName);
        System.out.println(groupProvider.getAll());
        System.out.println(groupProvider.findByName(groupName));
        final Group group = groupProvider.findByName(groupName).orElseThrow(() -> new GroupNotFoundException(getClass(),
                "Group with name '" + groupName + "' not found.", ExceptionType.WARNING));
        return getConverter().convertAll(getProvider().findByGroup(group).stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<UserRoleDTO> getByUserAndGroupAndApplication(String username, String groupName, String applicationName) {
        final User user = userProvider.findByUsername(username).orElseThrow(() -> new UserNotFoundException(getClass(),
                "User with username '" + username + "' not found.",
                ExceptionType.INFO));
        final Group group = groupProvider.findByName(groupName).orElse(null);
        final Application application = applicationProvider.findByName(applicationName).orElse(null);
        return getByUserAndGroupAndApplication(userConverter.convert(new UserConverterRequest(user)),
                groupConverter.convert(new GroupConverterRequest(group)),
                applicationConverter.convert(new ApplicationConverterRequest(application)));
    }

    public List<UserRoleDTO> getByUserAndGroupAndApplication(UserDTO userDTO, GroupDTO groupDTO, ApplicationDTO applicationDTO) {
        return getConverter().convertAll(getProvider().findByUserAndGroupAndApplication(userConverter.reverse(userDTO),
                        groupConverter.reverse(groupDTO), applicationConverter.reverse(applicationDTO)).stream()
                .map(this::createConverterRequest).collect(Collectors.toList()));
    }

    public List<UserRoleDTO> getByUserAndRole(String groupName, String roleName) {
        final Group group = groupProvider.findByName(groupName).orElseThrow(() -> new GroupNotFoundException(getClass(),
                "Group with name '" + roleName + "' not found.", ExceptionType.WARNING));
        final Role role = roleProvider.findByName(roleName).orElseThrow(() -> new RoleNotFoundException(getClass(),
                "Role with name '" + roleName + "' not found.", ExceptionType.WARNING));
        return getConverter().convertAll(getProvider().findByGroupAndRole(group, role)
                .stream().map(this::createConverterRequest).collect(Collectors.toList()));
    }
}

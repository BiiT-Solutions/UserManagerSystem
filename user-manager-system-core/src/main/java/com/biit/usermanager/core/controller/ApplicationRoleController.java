package com.biit.usermanager.core.controller;

/*-
 * #%L
 * User Manager System (core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.biit.kafka.controllers.KafkaCreatedElementController;
import com.biit.server.logger.DtoControllerLogger;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.ApplicationRoleConverter;
import com.biit.usermanager.core.converters.RoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationRoleConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationNotFoundException;
import com.biit.usermanager.core.exceptions.ApplicationRoleNotFoundException;
import com.biit.usermanager.core.exceptions.UserGroupNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.kafka.ApplicationRolesEventSender;
import com.biit.usermanager.core.kafka.UserEventSender;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.ApplicationRoleProvider;
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserGroupApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserGroupProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ApplicationRoleController extends KafkaCreatedElementController<ApplicationRole, ApplicationRoleId, ApplicationRoleDTO, ApplicationRoleRepository,
        ApplicationRoleProvider, ApplicationRoleConverterRequest, ApplicationRoleConverter> {

    private final ApplicationProvider applicationProvider;

    private final ApplicationConverter applicationConverter;

    private final RoleConverter roleConverter;

    private final UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider;
    private final UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider;
    private final ApplicationRoleProvider applicationRoleProvider;

    private final UserProvider userProvider;

    private final UserGroupProvider userGroupProvider;


    private final UserEventSender userEventSender;


    @Autowired
    protected ApplicationRoleController(ApplicationRoleProvider provider, ApplicationRoleConverter converter,
                                        ApplicationProvider applicationProvider, ApplicationConverter applicationConverter,
                                        RoleConverter roleConverter,
                                        UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider,
                                        ApplicationRoleProvider applicationRoleProvider, UserProvider userProvider,
                                        ApplicationRolesEventSender eventSender,
                                        UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider,
                                        UserGroupProvider userGroupProvider,
                                        UserEventSender userEventSender,
                                        List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(provider, converter, eventSender, userOrganizationProvider);
        this.roleConverter = roleConverter;
        this.applicationProvider = applicationProvider;
        this.applicationConverter = applicationConverter;
        this.userApplicationBackendServiceRoleProvider = userApplicationBackendServiceRoleProvider;
        this.applicationRoleProvider = applicationRoleProvider;
        this.userProvider = userProvider;
        this.userGroupApplicationBackendServiceRoleProvider = userGroupApplicationBackendServiceRoleProvider;
        this.userGroupProvider = userGroupProvider;
        this.userEventSender = userEventSender;
    }

    @Override
    protected ApplicationRoleConverterRequest createConverterRequest(ApplicationRole entity) {
        return new ApplicationRoleConverterRequest(entity);
    }

    public Set<ApplicationRoleDTO> getByApplication(String applicationName) {
        return new HashSet<>(convertAll(getProvider().findByApplication(applicationProvider.findByName(applicationName).orElseThrow(() ->
                new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found.")))));
    }

    public List<ApplicationRoleDTO> getByApplication(ApplicationDTO application) {
        return convertAll(getProvider().findByApplication(applicationConverter.reverse(application)));
    }

    public Set<ApplicationRoleDTO> getByRole(String roleName) {
        return new HashSet<>(convertAll(getProvider().findByRoleId(roleName)));
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
        getProvider().delete(applicationRole);
        DtoControllerLogger.info(this.getClass(), "Entity '{}' deleted by '{}'.", applicationRole, deletedBy);
    }

    public Set<ApplicationRoleDTO> getByUser(String username) {
        final User user = userProvider.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));

        final Set<UserApplicationBackendServiceRole> userApplicationBackendServiceRoles = userApplicationBackendServiceRoleProvider.findByUserId(user.getId());

        final Set<ApplicationRole> applicationRoles = new HashSet<>();
        userApplicationBackendServiceRoles.forEach(userApplicationBackendServiceRole ->
                applicationRoles.add(applicationRoleProvider.findByApplicationIdAndRoleId(
                        userApplicationBackendServiceRole.getId().getApplicationName(),
                        userApplicationBackendServiceRole.getId().getRoleName()
                ).orElseThrow(() ->
                        new ApplicationRoleNotFoundException(this.getClass(), "No application role defined for application '"
                                + userApplicationBackendServiceRole.getId().getApplicationName()
                                + "' and role '" + userApplicationBackendServiceRole.getId().getRoleName() + "'")))
        );


        return new HashSet<>(convertAll(applicationRoles));
    }

    public Set<ApplicationRoleDTO> getByUserGroup(Long id) {
        if (userGroupProvider.findById(id).isEmpty()) {
            throw new UserGroupNotFoundException(this.getClass(), "No User Group with id '" + id + "' found on the system.");
        }

        final Set<UserGroupApplicationBackendServiceRole> userGroupApplicationBackendServiceRoles = userGroupApplicationBackendServiceRoleProvider
                .findByUserGroupId(id);

        final Set<ApplicationRole> applicationRoles = new HashSet<>();
        userGroupApplicationBackendServiceRoles.forEach(userApplicationBackendServiceRole ->
                applicationRoles.add(applicationRoleProvider.findByApplicationIdAndRoleId(
                        userApplicationBackendServiceRole.getId().getApplicationName(),
                        userApplicationBackendServiceRole.getId().getRoleName()
                ).orElseThrow(() ->
                        new ApplicationRoleNotFoundException(this.getClass(), "No application role defined for application '"
                                + userApplicationBackendServiceRole.getId().getApplicationName()
                                + "' and role '" + userApplicationBackendServiceRole.getId().getRoleName() + "'")))
        );


        return new HashSet<>(convertAll(applicationRoles));
    }

    @Override
    @Transactional
    public void delete(ApplicationRoleDTO entity, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider
                .findBy(entity.getId().getApplication().getName(), entity.getId().getRole().getName());

        super.delete(entity, deletedBy);

        userEventSender.sendEvents(usersToInform, UserEventSender.REVOCATION_EVENT_TAG, deletedBy);
    }

    @Override
    public void deleteById(ApplicationRoleId id, String deletedBy) {
        //Send events.
        final Set<UserApplicationBackendServiceRole> usersToInform = userApplicationBackendServiceRoleProvider
                .findBy(id.getApplication().getName(), id.getRole().getName());

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

package com.biit.usermanager.core.controller;


import com.biit.kafka.controllers.KafkaElementController;
import com.biit.kafka.events.EventSubject;
import com.biit.kafka.events.IEventSender;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.UserGroupConverter;
import com.biit.usermanager.core.converters.models.UserGroupConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationRoleNotFoundException;
import com.biit.usermanager.core.exceptions.BackendServiceRoleNotFoundException;
import com.biit.usermanager.core.exceptions.InvalidParameterException;
import com.biit.usermanager.core.exceptions.UserGroupNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.kafka.UserEventSender;
import com.biit.usermanager.core.providers.ApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.ApplicationRoleProvider;
import com.biit.usermanager.core.providers.BackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserGroupApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserGroupProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.core.utils.RoleNameGenerator;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserGroupDTO;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserGroup;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserGroupUsers;
import com.biit.usermanager.persistence.repositories.UserGroupRepository;
import com.biit.usermanager.persistence.repositories.UserGroupUsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserGroupController extends KafkaElementController<UserGroup, Long, UserGroupDTO, UserGroupRepository,
        UserGroupProvider, UserGroupConverterRequest, UserGroupConverter> {

    private final ApplicationBackendServiceRoleProvider applicationBackendServiceRoleProvider;
    private final UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider;

    private final BackendServiceRoleProvider backendServiceRoleProvider;

    private final ApplicationRoleProvider applicationRoleProvider;

    private final UserConverter userConverter;

    private final UserProvider userProvider;

    private final UserGroupUsersRepository userGroupUsersRepository;

    protected UserGroupController(UserGroupProvider provider, UserGroupConverter converter,
                                  IEventSender<UserGroupDTO> eventSender,
                                  ApplicationBackendServiceRoleProvider applicationBackendServiceRoleProvider,
                                  UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider,
                                  BackendServiceRoleProvider backendServiceRoleProvider,
                                  ApplicationRoleProvider applicationRoleProvider, UserConverter userConverter, UserProvider userProvider,
                                  UserGroupUsersRepository userGroupUsersRepository) {
        super(provider, converter, eventSender);
        this.applicationBackendServiceRoleProvider = applicationBackendServiceRoleProvider;
        this.userGroupApplicationBackendServiceRoleProvider = userGroupApplicationBackendServiceRoleProvider;
        this.backendServiceRoleProvider = backendServiceRoleProvider;
        this.applicationRoleProvider = applicationRoleProvider;
        this.userConverter = userConverter;
        this.userProvider = userProvider;
        this.userGroupUsersRepository = userGroupUsersRepository;
    }

    @Override
    protected UserGroupConverterRequest createConverterRequest(UserGroup userGroup) {
        return new UserGroupConverterRequest(userGroup);
    }

    public UserGroupDTO getByName(String name) {
        return convert(getProvider().findByName(name).orElseThrow(
                () -> new UserGroupNotFoundException(this.getClass(), "No user group exists with the username '" + name + "'.")));
    }

    public void checkNameExists(String name) {
        getProvider().findByName(name).orElseThrow(()
                -> new UserGroupNotFoundException(this.getClass(), "No user group exists with the username '" + name + "'."));
    }

    @Transactional
    public void delete(String name, String deletedBy) {
        delete(getByName(name), deletedBy);
    }

    @Transactional
    public void delete(Long id, String deletedBy) {
        delete(get(id), deletedBy);
    }

    /**
     * Populate the authorities for a user. If a group is selected, only the one of the group. If not the roles that are not at group level.
     *
     * @param userGroupDTO The user to populate.
     * @return the populated user.
     */
    private UserGroupDTO setGrantedAuthorities(UserGroupDTO userGroupDTO, Application application, BackendService backendService) {
        if (userGroupDTO != null) {
            final Set<UserGroupApplicationBackendServiceRole> userGroupApplicationBackendServiceRoleProviderByUserId =
                    userGroupApplicationBackendServiceRoleProvider.findByUserGroupId(userGroupDTO.getId());

            userGroupApplicationBackendServiceRoleProviderByUserId.forEach(userGroupApplicationBackendServiceRole -> {
                if ((application == null
                        || application.getName().equalsIgnoreCase(userGroupApplicationBackendServiceRole.getId().getApplicationName()))
                        && (backendService == null
                        || backendService.getName().equalsIgnoreCase(userGroupApplicationBackendServiceRole.getId().getBackendServiceName()))) {
                    userGroupDTO.addApplicationServiceRoles(RoleNameGenerator.createApplicationRoleName(userGroupApplicationBackendServiceRole));
                    userGroupDTO.addGrantedAuthorities(RoleNameGenerator.createBackendRoleName(userGroupApplicationBackendServiceRole));
                }
            });

            UserManagerLogger.debug(this.getClass(), "Assigning application roles '" + userGroupDTO.getApplicationRoles()
                    + "' to '" + userGroupDTO.getName() + "'.");
            UserManagerLogger.debug(this.getClass(), "Assigning backend roles '" + userGroupDTO.getGrantedAuthorities()
                    + "' to '" + userGroupDTO.getName() + "'.");
        }
        return userGroupDTO;
    }

    public void assign(UserGroupDTO userGroupDTO, List<ApplicationBackendServiceRole> applicationBackendServiceRoles) {
        applicationBackendServiceRoles.forEach(applicationBackendServiceRole -> {
            try {
                assign(userGroupDTO.getName(),
                        applicationBackendServiceRole.getId().getApplicationRole().getId().getApplication().getName(),
                        applicationBackendServiceRole.getId().getApplicationRole().getId().getRole().getName(),
                        applicationBackendServiceRole.getId().getBackendServiceRole().getId().getBackendService().getName(),
                        applicationBackendServiceRole.getId().getBackendServiceRole().getId().getName());
            } catch (InvalidParameterException e) {
                UserManagerLogger.warning(this.getClass(), "Trying to assign an existing role '"
                        + applicationBackendServiceRole + "' to user '" + userGroupDTO + "'.");
            }
        });
    }

    public UserGroupDTO assign(
            Long id, String applicationName, String applicationRoleName) {

        final UserGroup userGroup = getProvider().findById(id).orElseThrow(()
                -> new UserGroupNotFoundException(this.getClass(), "No userGroup exists with id '" + id + "'."));

        return assign(userGroup, applicationName, applicationRoleName);
    }

    public UserGroupDTO assign(
            String name, String applicationName, String applicationRoleName) {

        final UserGroup userGroup = getProvider().findByName(name).orElseThrow(()
                -> new UserGroupNotFoundException(this.getClass(), "No userGroup exists with name '" + name + "'."));

        return assign(userGroup, applicationName, applicationRoleName);
    }

    public UserGroupDTO assign(
            UserGroup userGroup, String applicationName, String applicationRoleName) {

        final List<ApplicationBackendServiceRole> availableRoles = applicationBackendServiceRoleProvider
                .findByApplicationNameAndApplicationRole(applicationName, applicationRoleName);

        //Add any missing permission.
        final List<UserGroupApplicationBackendServiceRole> alreadyAssignedPermissions = userGroupApplicationBackendServiceRoleProvider
                .findBy(userGroup.getId(), applicationName, applicationRoleName);

        final Set<UserGroupApplicationBackendServiceRole> rolesToAdd = new HashSet<>();
        availableRoles.forEach(applicationBackendServiceRole -> {

            //Collection of availableRoles launch a lazy, as these ids are not retrieved in a collection.
            final ApplicationBackendServiceRole applicationBackendServiceRole1 =
                    applicationBackendServiceRoleProvider.findById(applicationBackendServiceRole.getId())
                            .orElseThrow(() -> new ApplicationRoleNotFoundException(this.getClass(),
                                    "No application role found with id '" + applicationBackendServiceRole.getId().getApplicationRole().getId() + "'."));
            final BackendServiceRole backendServiceRole =
                    backendServiceRoleProvider.findById(applicationBackendServiceRole1.getId().getBackendServiceRole().getId())
                            .orElseThrow(() -> new BackendServiceRoleNotFoundException(this.getClass(),
                                    "No backend service role found with id '" + applicationBackendServiceRole.getId().getBackendServiceRole().getId() + "'."));

            final UserGroupApplicationBackendServiceRole userGroupApplicationBackendServiceRole = new UserGroupApplicationBackendServiceRole(
                    userGroup.getId(),
                    applicationName,
                    applicationRoleName,
                    backendServiceRole.getId().getBackendService().getId(),
                    backendServiceRole.getId().getName()
            );
            if (!alreadyAssignedPermissions.contains(userGroupApplicationBackendServiceRole)) {
                rolesToAdd.add(userGroupApplicationBackendServiceRole);
            }
        });
        userGroupApplicationBackendServiceRoleProvider.saveAll(rolesToAdd);
        return setGrantedAuthorities(convert(userGroup), null, null);
    }

    public UserGroupDTO unAssign(
            Long id, String applicationName, String applicationRoleName, String unassignedBy) {

        final UserGroup userGroup = getProvider().findById(id).orElseThrow(()
                -> new UserGroupNotFoundException(this.getClass(), "No UserGroup exists with id '" + id + "'."));

        return unAssign(userGroup, applicationName, applicationRoleName, unassignedBy);
    }

    public UserGroupDTO unAssign(
            String name, String applicationName, String applicationRoleName, String unassignedBy) {

        final UserGroup userGroup = getProvider().findByName(name).orElseThrow(()
                -> new UserGroupNotFoundException(this.getClass(), "No UserGroup exists with name '" + name + "'."));

        return unAssign(userGroup, applicationName, applicationRoleName, unassignedBy);
    }

    public UserGroupDTO unAssign(
            UserGroup userGroup, String applicationName, String applicationRoleName, String unassignedBy) {

        final List<ApplicationBackendServiceRole> availableRoles = applicationBackendServiceRoleProvider
                .findByApplicationNameAndApplicationRole(applicationName, applicationRoleName);

        //Add any missing permission.
        final List<UserGroupApplicationBackendServiceRole> assignedPermissions = userGroupApplicationBackendServiceRoleProvider
                .findBy(userGroup.getId(), applicationName, applicationRoleName);

        //Send events.
        getEventSender().sendEvents(convert(userGroup), EventSubject.UPDATED, UserEventSender.REVOCATION_EVENT_TAG, unassignedBy);


        userGroupApplicationBackendServiceRoleProvider.deleteAll(assignedPermissions);
        return setGrantedAuthorities(convert(userGroup), null, null);
    }

    public UserGroupDTO assign(
            Long id, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        final UserGroup userGroup = getProvider().findById(id).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No UserGroup exists with id '" + id + "'."));

        return assign(userGroup, applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public UserGroupDTO assign(
            String name, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        final UserGroup userGroup = getProvider().findByName(name).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No UserGroup exists with name '" + name + "'."));

        return assign(userGroup, applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public UserGroupDTO assign(
            UserGroup userGroup, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {

        //Ensure it does not exist yet.
        if (userGroupApplicationBackendServiceRoleProvider
                .findBy(userGroup.getId(), applicationName, applicationRoleName, backendServiceName, backendServiceRoleName).isPresent()) {
            throw new InvalidParameterException(this.getClass(), "User Group '" + userGroup + "' already has role '"
                    + applicationRoleName + "' for application '" + applicationName + "' and service '" + backendServiceName
                    + "' with role '" + backendServiceRoleName + "'.");
        }

        final Optional<ApplicationBackendServiceRole> optionalApplicationBackendServiceRole = applicationBackendServiceRoleProvider
                .findByApplicationRoleAndServiceRole(applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);

        //Create it if it does not exist.
        if (optionalApplicationBackendServiceRole.isEmpty()) {
            final ApplicationRole applicationRole = applicationRoleProvider
                    .findByApplicationIdAndRoleId(applicationName, applicationRoleName).orElseThrow(() ->
                            new ApplicationRoleNotFoundException(this.getClass(), "No application role defined for application '"
                                    + applicationName + "' and role '" + applicationRoleName + "'"));

            final BackendServiceRole backendServiceRole = backendServiceRoleProvider
                    .findByBackendServiceAndName(backendServiceName, backendServiceRoleName).orElseThrow(() ->
                            new BackendServiceRoleNotFoundException(this.getClass(), "No backend service role defined for backend '"
                                    + backendServiceName + "' and role '" + backendServiceRoleName + "'."));

            applicationBackendServiceRoleProvider.save(new ApplicationBackendServiceRole(applicationRole, backendServiceRole));
        }

        //Add directly to the join column.
        userGroupApplicationBackendServiceRoleProvider.save(
                new UserGroupApplicationBackendServiceRole(userGroup.getId(), applicationName,
                        applicationRoleName, backendServiceName, backendServiceRoleName));

        //Load again all roles.
        return setGrantedAuthorities(convert(userGroup), null, null);
    }

    public UserGroupDTO assign(Long userGroupId, Collection<UserDTO> users, String assignedBy) {
        final UserGroup userGroup = getProvider().findById(userGroupId).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No UserGroup exists with id '" + userGroupId + "'."));


        final List<Long> usersInGroup = userProvider.getByUserGroup(userGroupId).stream().map(User::getId).toList();
        //final List<Long> usersInGroup = new ArrayList<>();


        users = users.stream().filter(userDTO -> !usersInGroup.contains(userDTO.getId())).toList();

        //Store into the group
        final List<UserGroupUsers> userGroupUsers = new ArrayList<>();
        users.forEach(userDTO -> {
            userGroupUsers.add(new UserGroupUsers(userGroupId, userDTO.getId()));
        });
        userGroupUsersRepository.saveAll(userGroupUsers);

        userGroup.setUpdatedBy(assignedBy);

        return convert(getProvider().save(userGroup));
    }

    public UserGroupDTO unAssign(Long userGroupId, Collection<UserDTO> users, String assignedBy) {
        final UserGroup userGroup = getProvider().findById(userGroupId).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No UserGroup exists with id '" + userGroupId + "'."));


        final List<UserGroupUsers> userGroupUsersToDelete = new ArrayList<>();
        users.forEach(userDTO -> {
            userGroupUsersToDelete.add(new UserGroupUsers(userGroupId, userDTO.getId()));
        });
        userGroupUsersRepository.deleteAll(userGroupUsersToDelete);

        userGroup.setUpdatedBy(assignedBy);

        return convert(getProvider().save(userGroup));
    }


}

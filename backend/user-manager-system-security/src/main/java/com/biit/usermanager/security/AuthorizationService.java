package com.biit.usermanager.security;

import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.NotAuthorizedException;
import com.biit.rest.exceptions.UnprocessableEntityException;
import com.biit.server.client.SecurityClient;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.logger.AuthenticationServiceLogger;
import com.biit.usermanager.security.exceptions.*;
import com.biit.usermanager.security.providers.AuthorizationUrlConstructor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorizationService implements IAuthorizationService<Long, Long, Long> {

    private final AuthorizationUrlConstructor authorizationUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    public AuthorizationService(AuthorizationUrlConstructor authorizationUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.authorizationUrlConstructor = authorizationUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
    }

    @Override
    public Set<IUser<Long>> getAllUsers() throws UserManagementException, InvalidCredentialsException {
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getAllUsers())) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor.getAllUsers(),
                        response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return new HashSet<>(mapper.readValue(response.readEntity(String.class), new TypeReference<Set<UserDTO>>() {
                }));
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public Set<IUser<Long>> getAllUsers(IGroup<Long> organization) throws UserManagementException, OrganizationDoesNotExistException,
            InvalidCredentialsException {
        if (organization == null) {
            throw new OrganizationDoesNotExistException("No organization selected.");
        }
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getUserRolesByOrganization(organization.getUniqueName()))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getUserRolesByOrganization(organization.getUniqueName()), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), new TypeReference<Set<UserRoleDTO>>() {
                }).stream().map(UserRoleDTO::getUser).filter(Objects::nonNull).collect(Collectors.toSet());
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IGroup<Long> getOrganization(Long organizationId) throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException {
        if (organizationId == null) {
            throw new OrganizationDoesNotExistException("No Id provided.");
        }
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getOrganization(organizationId))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getOrganization(organizationId), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), OrganizationDTO.class);
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IGroup<Long> getOrganization(String organizationName) throws UserManagementException, InvalidCredentialsException,
            OrganizationDoesNotExistException {
        if (organizationName == null) {
            throw new OrganizationDoesNotExistException("No name provided.");
        }
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getOrganizationByName(organizationName))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getOrganizationByName(organizationName), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), OrganizationDTO.class);
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public Set<IGroup<Long>> getAllAvailableOrganizations() throws UserManagementException, InvalidCredentialsException {
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getAllOrganizations())) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getAllOrganizations(), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return new HashSet<>(mapper.readValue(response.readEntity(String.class), new TypeReference<Set<OrganizationDTO>>() {
                }));
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IRole<Long> getRole(Long roleId) throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException {
        if (roleId == null) {
            throw new RoleDoesNotExistsException("No Id provided.");
        }
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getRoleById(roleId))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getRoleById(roleId), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), RoleDTO.class);
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IRole<Long> getRole(String roleName) throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException {
        if (roleName == null) {
            throw new RoleDoesNotExistsException("No name provided.");
        }
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getRoleByName(roleName))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getRoleByName(roleName), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), RoleDTO.class);
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public Set<IActivity> getRoleActivities(IRole<Long> role) {
        return null;
    }

    @Override
    public Set<IRole<Long>> getUserGroupRoles(IGroup<Long> organization) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserGroups(IUser<Long> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
        if (user == null) {
            throw new UserDoesNotExistException("No user selected.");
        }
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getUserRolesByUser(user.getUniqueName()))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getUserRolesByUser(user.getUniqueName()), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), new TypeReference<Set<UserRoleDTO>>() {
                }).stream().map(UserRoleDTO::getOrganization).filter(Objects::nonNull).collect(Collectors.toSet());
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user) throws UserManagementException, InvalidCredentialsException,
            UserDoesNotExistException {
        return getUserGroups(user);
    }

    @Override
    public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user, IGroup<Long> site) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IRole<Long>> getUserRoles(IUser<Long> user) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IRole<Long>> getUserRoles(IUser<Long> user, IGroup<Long> organization) throws UserManagementException, UserDoesNotExistException,
            InvalidCredentialsException, OrganizationDoesNotExistException {
        if (user == null) {
            throw new UserDoesNotExistException("No user selected.");
        }
        if (organization == null) {
            throw new OrganizationDoesNotExistException("No organization selected.");
        }
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getUserRolesFromUserOrganizationAndApplication(user.getUniqueName(), organization.getUniqueName(), null))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getUserRolesFromUserOrganizationAndApplication(user.getUniqueName(), organization.getUniqueName(), null), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), new TypeReference<Set<UserRoleDTO>>() {
                }).stream().map(UserRoleDTO::getRole).filter(Objects::nonNull).collect(Collectors.toSet());
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public Set<IRole<Long>> getAllRoles(IGroup<Long> organization) throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException {
        if (organization == null) {
            throw new OrganizationDoesNotExistException("No organization selected.");
        }
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getUserRolesByOrganization(organization.getUniqueName()))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getUserRolesByOrganization(organization.getUniqueName()), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), new TypeReference<Set<UserRoleDTO>>() {
                }).stream().map(UserRoleDTO::getRole).filter(Objects::nonNull).collect(Collectors.toSet());
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) throws UserManagementException {
        return false;
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity) throws UserManagementException {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public Set<IUser<Long>> getUsers(IRole<Long> role, IGroup<Long> organization) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserParentOrganizations(IUser<Long> user) throws UserManagementException {
        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserChildrenOrganizations(IUser<Long> user, IGroup<Long> parentOrganization) throws UserManagementException {
        return null;
    }

    @Override
    public void addUserRole(IUser<Long> user, IRole<Long> role) throws UserManagementException {

    }

    @Override
    public void addUserOrganizationRole(IUser<Long> user, IGroup<Long> organization, IRole<Long> role) throws UserManagementException {

    }

    @Override
    public IRoleActivities getRoleActivities() {
        return null;
    }

    @Override
    public void setRoleActivities(IRoleActivities roleActivities) {

    }

    @Override
    public void createBeans() {

    }

    @Override
    public void cleanUserChildrenOrganizations(IUser<Long> user, IGroup<Long> parentOrganization) {

    }
}

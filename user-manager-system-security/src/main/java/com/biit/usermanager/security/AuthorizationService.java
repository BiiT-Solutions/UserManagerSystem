package com.biit.usermanager.security;

import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.NotAuthorizedException;
import com.biit.rest.exceptions.UnprocessableEntityException;
import com.biit.server.client.SecurityClient;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.logger.AuthenticationServiceLogger;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.OrganizationDoesNotExistException;
import com.biit.usermanager.security.exceptions.RoleDoesNotExistsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.usermanager.security.providers.AuthorizationUrlConstructor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorizationService implements IAuthorizationService<Long, Long, String> {
    private static final int CACHE_EXPIRATION_TIME = 10 * 60 * 1000;

    private final AuthorizationUrlConstructor authorizationUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    @Value("${spring.application.name}")
    private String serviceName;

    public AuthorizationService(AuthorizationUrlConstructor authorizationUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.authorizationUrlConstructor = authorizationUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
    }

    @Override
    public Set<IUser<Long>> getAllUsers() throws UserManagementException, InvalidCredentialsException {
        try {
            try (Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getAllUsers())) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor.getAllUsers(),
                        response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return new HashSet<>(Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class)));
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public Set<IUser<Long>> getAllUsers(IGroup<Long> group) throws UserManagementException, OrganizationDoesNotExistException,
            InvalidCredentialsException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Cacheable(value = "groups", key = "#groupId")
    @Override
    public IGroup<Long> getOrganization(Long groupId) throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException {
        if (groupId == null) {
            throw new OrganizationDoesNotExistException("No Id provided.");
        }
        try {
            try (Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getGroups(groupId))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getGroups(groupId), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), GroupDTO.class);
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IGroup<Long> getOrganization(String groupName) throws UserManagementException, InvalidCredentialsException,
            OrganizationDoesNotExistException {
        if (groupName == null) {
            throw new OrganizationDoesNotExistException("No name provided.");
        }
        try {
            try (Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getGroupByName(groupName, serviceName))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getGroupByName(groupName, serviceName), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), GroupDTO.class);
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
            try (Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getAllGroupsWithoutParent())) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getAllGroups(), response.getStatus());
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return new HashSet<>(Arrays.asList(mapper.readValue(response.readEntity(String.class), GroupDTO[].class)));
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IRole<String> getRole(String roleName) throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException {
        if (roleName == null) {
            throw new RoleDoesNotExistsException("No name provided.");
        }
        try {
            try (Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
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
    public Set<IRole<String>> getUserGroupRoles(IGroup<Long> organization) throws OrganizationDoesNotExistException,
            UserManagementException, InvalidCredentialsException {
        return getAllRoles(organization);
    }

    @Override
    public Set<IGroup<Long>> getUserGroups(IUser<Long> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user) throws UserManagementException, InvalidCredentialsException,
            UserDoesNotExistException {
        return getUserGroups(user);
    }

    @Override
    public Set<IRole<String>> getUserRoles(IUser<Long> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Set<IRole<String>> getUserRoles(IUser<Long> user, IGroup<Long> organization) throws UserManagementException, UserDoesNotExistException,
            InvalidCredentialsException, OrganizationDoesNotExistException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Set<IRole<String>> getAllRoles(IGroup<Long> group) throws UserManagementException, OrganizationDoesNotExistException,
            InvalidCredentialsException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Set<IUser<Long>> getUsers(IRole<String> role, IGroup<Long> group) throws UserManagementException, RoleDoesNotExistsException,
            OrganizationDoesNotExistException, InvalidCredentialsException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @CacheEvict(allEntries = true, value = {"roles"})
    @Override
    public void addUserRole(IUser<Long> user, IRole<String> role) throws UserManagementException, UserDoesNotExistException,
            RoleDoesNotExistsException, InvalidCredentialsException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @CacheEvict(allEntries = true, value = {"roles"})
    @Override
    public void addUserOrganizationRole(IUser<Long> user, IGroup<Long> organization, IRole<String> role) throws UserManagementException,
            UserDoesNotExistException, RoleDoesNotExistsException, InvalidCredentialsException, OrganizationDoesNotExistException {
        throw new UnsupportedOperationException("Not implemented");
    }


    @CacheEvict(allEntries = true, value = {"users", "roles", "groups"})
    @Scheduled(fixedDelay = CACHE_EXPIRATION_TIME)
    @Override
    public void reset() {
        //Only for handling Spring cache.
    }

    @Override
    public void createBeans() {

    }

    @CacheEvict(allEntries = true, value = {"groups"})
    @Override
    public void cleanUserChildrenOrganizations(IUser<Long> user, IGroup<Long> parentOrganization) {
        //Only for handling Spring cache.
    }

    @Override
    public Set<IGroup<Long>> getUserParentOrganizations(IUser<Long> user) throws UserManagementException,
            InvalidCredentialsException, UserDoesNotExistException {
        return getUserGroups(user).stream().filter(group -> ((GroupDTO) group).getParent() == null).collect(Collectors.toSet());
    }

    @Override
    public Set<IGroup<Long>> getUserChildrenOrganizations(IUser<Long> user, IGroup<Long> parentOrganization) throws UserManagementException,
            InvalidCredentialsException, UserDoesNotExistException {
        return getUserGroups(user).stream().filter(group -> ((GroupDTO) group).getParent() != null).collect(Collectors.toSet());
    }

    @Override
    public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user, IGroup<Long> site) throws UserManagementException {
        return null;
    }
}

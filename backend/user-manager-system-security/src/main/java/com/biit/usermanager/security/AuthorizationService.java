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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Cacheable("users")
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

    @Cacheable("users")
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

    @Cacheable("organizations")
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

    @Cacheable("organizations")
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

    @Cacheable("organizations")
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

    @Cacheable("roles")
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

    @Cacheable("roles")
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

    @Cacheable("roles")
    @Override
    public Set<IRole<Long>> getUserGroupRoles(IGroup<Long> organization) throws OrganizationDoesNotExistException,
            UserManagementException, InvalidCredentialsException {
        return getAllRoles(organization);
    }

    @Cacheable("organizations")
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

    @Cacheable("roles")
    @Override
    public Set<IRole<Long>> getUserRoles(IUser<Long> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
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
                }).stream().map(UserRoleDTO::getRole).filter(Objects::nonNull).collect(Collectors.toSet());
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Cacheable("roles")
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
                                .getUserRolesFromUserOrganizationAndApplication(user.getUniqueName(), organization.getUniqueName(), null),
                        response.getStatus());
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

    @Cacheable("roles")
    @Override
    public Set<IRole<Long>> getAllRoles(IGroup<Long> organization) throws UserManagementException, OrganizationDoesNotExistException,
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
                }).stream().map(UserRoleDTO::getRole).filter(Objects::nonNull).collect(Collectors.toSet());
            }
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Cacheable("users")
    @Override
    public Set<IUser<Long>> getUsers(IRole<Long> role, IGroup<Long> organization) throws UserManagementException, RoleDoesNotExistsException,
            OrganizationDoesNotExistException, InvalidCredentialsException {
        if (role == null) {
            throw new RoleDoesNotExistsException("No role selected.");
        }
        if (organization == null) {
            throw new OrganizationDoesNotExistException("No organization selected.");
        }
        try {
            try (final Response response = securityClient.get(authorizationUrlConstructor.getUserManagerServerUrl(),
                    authorizationUrlConstructor.getUserByOrganizationAndRole(organization.getUniqueName(), role.getUniqueName()))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor
                                .getUserByOrganizationAndRole(organization.getUniqueName(), role.getUniqueName()), response.getStatus());
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

    @CacheEvict(allEntries = true, value = {"roles"})
    @Override
    public void addUserRole(IUser<Long> user, IRole<Long> role) throws UserManagementException, UserDoesNotExistException,
            RoleDoesNotExistsException, InvalidCredentialsException {
        if (user == null) {
            throw new UserDoesNotExistException("No user selected.");
        }
        if (role == null) {
            throw new RoleDoesNotExistsException("No role selected.");
        }
        final UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUser((UserDTO) user);
        userRoleDTO.setRole((RoleDTO) role);
        try (final Response response = securityClient.post(authorizationUrlConstructor.getUserManagerServerUrl(),
                authorizationUrlConstructor.addUserRoles(), mapper.writeValueAsString(userRoleDTO))) {
            AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                    authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor.addUserRoles(),
                    response.getStatus());
            if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                throw new InvalidCredentialsException("Invalid JWT credentials!");
            }
        } catch (JsonProcessingException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @CacheEvict(allEntries = true, value = {"roles"})
    @Override
    public void addUserOrganizationRole(IUser<Long> user, IGroup<Long> organization, IRole<Long> role) throws UserManagementException,
            UserDoesNotExistException, RoleDoesNotExistsException, InvalidCredentialsException, OrganizationDoesNotExistException {
        if (user == null) {
            throw new UserDoesNotExistException("No user selected.");
        }
        if (role == null) {
            throw new RoleDoesNotExistsException("No role selected.");
        }
        if (organization == null) {
            throw new OrganizationDoesNotExistException("No role selected.");
        }
        final UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUser((UserDTO) user);
        userRoleDTO.setRole((RoleDTO) role);
        userRoleDTO.setOrganization((OrganizationDTO) organization);
        try (final Response response = securityClient.post(authorizationUrlConstructor.getUserManagerServerUrl(),
                authorizationUrlConstructor.addUserRoles(), mapper.writeValueAsString(userRoleDTO))) {
            AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                    authorizationUrlConstructor.getUserManagerServerUrl() + authorizationUrlConstructor.addUserRoles(),
                    response.getStatus());
            if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                throw new InvalidCredentialsException("Invalid JWT credentials!");
            }
        } catch (JsonProcessingException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }


    @CacheEvict(allEntries = true, value = {"users", "roles", "organizations"})
    @Scheduled(fixedDelay = 60 * 10 * 1000)
    @Override
    public void reset() {
        //Only for handling Spring cache.
    }

    @Override
    public void createBeans() {

    }

    @CacheEvict(allEntries = true, value = {"organizations"})
    @Override
    public void cleanUserChildrenOrganizations(IUser<Long> user, IGroup<Long> parentOrganization) {
        //Only for handling Spring cache.
    }

    @Cacheable("organizations")
    @Override
    public Set<IGroup<Long>> getUserParentOrganizations(IUser<Long> user) throws UserManagementException,
            InvalidCredentialsException, UserDoesNotExistException {
        return getUserOrganizations(user).stream().filter(organization -> ((OrganizationDTO) organization).getParent() == null).collect(Collectors.toSet());
    }

    @Cacheable("organizations")
    @Override
    public Set<IGroup<Long>> getUserChildrenOrganizations(IUser<Long> user, IGroup<Long> parentOrganization) throws UserManagementException,
            InvalidCredentialsException, UserDoesNotExistException {
        return getUserOrganizations(user).stream().filter(organization -> ((OrganizationDTO) organization).getParent() != null).collect(Collectors.toSet());
    }

    @Cacheable("organizations")
    @Override
    public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user, IGroup<Long> site) throws UserManagementException {
        return null;
    }
}

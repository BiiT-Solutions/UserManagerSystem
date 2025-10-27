package com.biit.usermanager.client.providers;

/*-
 * #%L
 * User Manager System (Rest Client)
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

import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.InvalidResponseException;
import com.biit.server.client.SecurityClient;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.server.security.model.IAuthenticatedUser;
import com.biit.server.security.model.UpdatePasswordRequest;
import com.biit.usermanager.client.exceptions.ElementNotFoundException;
import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import com.biit.usermanager.client.providers.converters.UserDTOConverter;
import com.biit.usermanager.client.providers.models.Email;
import com.biit.usermanager.client.validators.EmailValidator;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.utils.RoleNameGenerator;
import com.biit.usermanager.logger.UserManagerClientLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Service
@Order(2)
@Qualifier("userManagerClient")
public class UserManagerClient implements IAuthenticatedUserProvider<UserDTO> {

    private static final String EXTERNAL_REFERENCE_PARAMETER = "references";
    private static final String PAGE_PARAMETER = "page";
    private static final String PAGE_SIZE_PARAMETER = "size";
    private static final int CONFLICT_CODE = 409;

    private final UserUrlConstructor userUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    public UserManagerClient(UserUrlConstructor userUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.userUrlConstructor = userUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
        UserManagerClientLogger.info(this.getClass(), "User Manager Client loaded correctly.");
    }


    @Override
    public Optional<UserDTO> findByUsername(String username) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByName(username))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByName(username), response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    UserManagerClientLogger.warning(this.getClass(), "User '{}' not found.", username);
                    throw new ElementNotFoundException(this.getClass(), "No user with username '" + username + "' found.");
                }
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            UserManagerClientLogger.warning(this.getClass(), "User '{}' not found.", username);
            throw new ElementNotFoundException(this.getClass(), "No user with username '" + username + "' found.", e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDTO> findByUsername(String username, String backendService) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByNameAndBackendService(username, backendService))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByNameAndBackendService(username, backendService),
                        response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    UserManagerClientLogger.warning(this.getClass(), "User '{}' not found.", username);
                    throw new ElementNotFoundException(this.getClass(), "No user with username '" + username + "' found.");
                }
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            UserManagerClientLogger.warning(this.getClass(), "User '{}' not found.", username);
            UserManagerClientLogger.warning(this.getClass(), "User '" + username + "' not found!");
            return Optional.empty();
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDTO> findByEmailAddress(String email) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByEmail(email))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByEmail(email), response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    UserManagerClientLogger.warning(this.getClass(), "User with email '{}' not found.", email);
                    throw new ElementNotFoundException(this.getClass(), "No user with email '" + email + "' found.");
                }
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            UserManagerClientLogger.warning(this.getClass(), "User with email '{}' not found.", email);
            throw e;
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<UserDTO> findByEmailAddress(Email email) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByEmail(email.getEmail()))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByEmail(email.getEmail()), response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    UserManagerClientLogger.warning(this.getClass(), "User with email '{}' not found.", email);
                    throw new ElementNotFoundException(this.getClass(), "No user with username '" + email + "' found.");
                }
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            UserManagerClientLogger.warning(this.getClass(), "User with email '{}' not found.", email);
            throw e;
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDTO> findByEmailAddress(String email, String applicationName) throws EmptyResultException {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByEmailAndApplication(EmailValidator.validate(email), applicationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByEmailAndApplication(email, applicationName),
                        response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    UserManagerClientLogger.warning(this.getClass(), "User with email '{}' not found.", email);
                    throw new ElementNotFoundException(this.getClass(), "No user with email '" + email + "' found.");
                }
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<UserDTO> findByEmailAddress(Email email, String applicationName) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByEmailAndApplication(email.getEmail(), applicationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByEmailAndApplication(email.getEmail(), applicationName),
                        response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    UserManagerClientLogger.warning(this.getClass(), "User with email '{}' not found.", email);
                    throw new ElementNotFoundException(this.getClass(), "No user with email '" + email + "' found.");
                }
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            UserManagerClientLogger.warning(this.getClass(), "User with email '{}' not found.", email);
            throw e;
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public Optional<UserDTO> findByUID(String id) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByUid(id))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByUid(id), response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    UserManagerClientLogger.warning(this.getClass(), "User with id '{}' not found.", id);
                    throw new ElementNotFoundException(this.getClass(), "No user with id '" + id + "' found.");
                }
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            UserManagerClientLogger.warning(this.getClass(), "User with id '{}' not found.", id);
            throw e;
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public UserDTO create(CreateUserRequest createUserRequest, String createdBy) {
        try {
            try (Response result = securityClient.post(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.getUsers(),
                    mapper.writeValueAsString(UserDTOConverter.convert(createUserRequest)))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsers(), result.getStatus());
                return mapper.readValue(result.readEntity(String.class), UserDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }


    @Override
    public UserDTO updatePassword(String username, String oldPassword, String newPassword, String updatedBy) {
        try {
            try (Response result = securityClient.post(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.updateUserPassword(username),
                    mapper.writeValueAsString(new UpdatePasswordRequest(oldPassword, newPassword)))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.updateUserPassword(username), result.getStatus());
                if (Objects.equals(result.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No user with username '" + username + "' found.");
                }
                return mapper.readValue(result.readEntity(String.class), UserDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }

    @Override
    public String getPassword(String username) {
        try {
            try (Response result = securityClient.get(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.getUserPassword(
                    username), MediaType.TEXT_PLAIN)) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserPassword(username), result.getStatus());
                if (Objects.equals(result.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No user with username '" + username + "' found.");
                }
                return result.readEntity(String.class);
            }
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }

    @Override
    public String getPasswordByUid(String uid) {
        try {
            try (Response result = securityClient.get(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.getUserPasswordByUid(
                    uid), MediaType.TEXT_PLAIN)) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserPasswordByUid(uid), result.getStatus());
                if (Objects.equals(result.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No user with uid '" + uid + "' found.");
                }
                return result.readEntity(String.class);
            }
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }


    @Override
    public UserDTO updateUser(CreateUserRequest createUserRequest, String updatedBy) {
        try {
            try (Response result = securityClient.put(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.getUsers(),
                    mapper.writeValueAsString(UserDTOConverter.convert(createUserRequest)))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsers(), result.getStatus());
                return mapper.readValue(result.readEntity(String.class), UserDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }


    @Override
    public long count() {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.count())) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.count(), response.getStatus());
                return mapper.readValue(response.readEntity(String.class), Long.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return -1;
        }
    }


    @Override
    public Collection<UserDTO> findAll() {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getAll())) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getAll(), response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }

    @Override
    public Collection<UserDTO> findAll(int page, int size) {
        try {
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put(PAGE_PARAMETER, page);
            parameters.put(PAGE_SIZE_PARAMETER, size);
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getAll(), parameters, null)) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getAll(), response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }


    @Override
    public boolean deleteUser(String currentUser, String username) {
        if (Objects.equals(currentUser, username)) {
            return false;
        }
        try {
            try (Response result = securityClient.delete(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.deleteByUsername(username))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.deleteByUsername(username), result.getStatus());
                return true;
            }
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return false;
        }
    }


    @Override
    public boolean delete(IAuthenticatedUser authenticatedUser) {
        try {
            try (Response result = securityClient.post(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.delete(),
                    mapper.writeValueAsString(authenticatedUser))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.delete(), result.getStatus());
                return true;
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return false;
        }
    }


    @Override
    public Set<String> getRoles(String username, String applicationName) {
        final Set<String> roles = new HashSet<>();
        try {
            try (Response result = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getRolesByUserAndApplication(username, applicationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor
                                .getRolesByUserAndApplication(username, applicationName), result.getStatus());

                final List<BackendServiceRoleDTO> backendServiceRoleDTOs = Arrays.asList(
                        mapper.readValue(result.readEntity(String.class), BackendServiceRoleDTO[].class));
                backendServiceRoleDTOs.forEach(backendServiceRoleDTO -> roles.add(
                        RoleNameGenerator.createRoleName(backendServiceRoleDTO)));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new HashSet<>();
        }
        return roles;
    }

    public Collection<UserDTO> findByTeam(Long teamId) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUsersByTeam(teamId))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsersByTeam(teamId),
                        response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new ArrayList<>();
        }
    }


    public Collection<UserDTO> findByTeam(Long teamId, int page, int size) {
        try {
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put(PAGE_PARAMETER, page);
            parameters.put(PAGE_SIZE_PARAMETER, size);
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUsersByTeam(teamId), parameters, null)) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsersByTeam(teamId),
                        response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new ArrayList<>();
        }
    }

    public long countByTeam(String organization, String team) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.countUsersByTeam(organization, team))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.countUsersByTeam(organization, team),
                        response.getStatus());
                return mapper.readValue(response.readEntity(String.class), Long.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return 0;
        }
    }


    public Collection<UserDTO> findByTeam(String organization, String team) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUsersByTeam(organization, team))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsersByTeam(organization, team),
                        response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new ArrayList<>();
        }
    }

    public Collection<UserDTO> findByTeam(String organization, String team, int page, int size) {
        try {
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put(PAGE_PARAMETER, page);
            parameters.put(PAGE_SIZE_PARAMETER, size);
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUsersByTeam(organization, team), parameters, null)) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsersByTeam(organization, team),
                        response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new ArrayList<>();
        }
    }

    public long countByOrganization(String organization) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.countUsersByOrganization(organization))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.countUsersByOrganization(organization),
                        response.getStatus());
                return mapper.readValue(response.readEntity(String.class), Long.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return 0;
        }
    }

    public Collection<UserDTO> findByOrganization(String organizationName) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUsersByOrganization(organizationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsersByOrganization(organizationName),
                        response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new ArrayList<>();
        }
    }


    public Collection<UserDTO> findByOrganization(String organizationName, int page, int size) {
        try {
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put(PAGE_PARAMETER, page);
            parameters.put(PAGE_SIZE_PARAMETER, size);
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUsersByOrganization(organizationName), parameters, null)) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsersByOrganization(organizationName),
                        response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new ArrayList<>();
        }
    }


    @Override
    public Optional<UserDTO> findByExternalReference(String externalReference) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUsersByExternalReference(externalReference))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsersByExternalReference(externalReference),
                        response.getStatus());
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return Optional.empty();
        }
    }


    public Collection<UserDTO> findByExternalReferences(List<String> externalReferences) {
        try {
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put(EXTERNAL_REFERENCE_PARAMETER, externalReferences);
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUsersByExternalReferences(), parameters, null)) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsersByExternalReferences(),
                        response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), UserDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new ArrayList<>();
        }
    }


    public boolean usernameExists(String username) {
        try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                userUrlConstructor.checkUsername(username))) {
            UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                    userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.checkUsername(username),
                    response.getStatus());
            if (response.getStatus() == CONFLICT_CODE) {
                return true;
            }
            return !HttpStatus.valueOf(response.getStatus()).is2xxSuccessful();
        }
    }


}

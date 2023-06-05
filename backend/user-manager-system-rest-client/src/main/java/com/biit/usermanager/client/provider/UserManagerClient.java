package com.biit.usermanager.client.provider;

import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.InvalidResponseException;
import com.biit.server.client.SecurityClient;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.server.security.model.UpdatePasswordRequest;
import com.biit.usermanager.client.provider.converters.UserDTOConverter;
import com.biit.usermanager.client.provider.models.Email;
import com.biit.usermanager.client.validators.EmailValidator;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.logger.UserManagerClientLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Service
public class UserManagerClient implements IAuthenticatedUserProvider {

    private final UserUrlConstructor userUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    public UserManagerClient(UserUrlConstructor userUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.userUrlConstructor = userUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
    }


    @Override
    public Optional<IAuthenticatedUser> findByUsername(String username) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByName(username))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByName(username), response.getStatus());
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<IAuthenticatedUser> findByUsername(String username, String applicationName) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByNameAndApplication(username, applicationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByNameAndApplication(username, applicationName),
                        response.getStatus());
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<IAuthenticatedUser> findByEmailAddress(String email) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByEmail(email))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByEmail(email), response.getStatus());
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<IAuthenticatedUser> findByEmailAddress(Email email) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByEmail(email.getEmail()))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByEmail(email.getEmail()), response.getStatus());
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<IAuthenticatedUser> findByEmailAddress(String email, String applicationName) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByEmailAndApplication(EmailValidator.validate(email), applicationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByEmailAndApplication(email, applicationName),
                        response.getStatus());
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<IAuthenticatedUser> findByEmailAddress(Email email, String applicationName) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserByEmailAndApplication(email.getEmail(), applicationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserByEmailAndApplication(email.getEmail(), applicationName),
                        response.getStatus());
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<IAuthenticatedUser> findByUID(String id) {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getUserById(id))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUserById(id), response.getStatus());
                return Optional.of(mapper.readValue(response.readEntity(String.class), UserDTO.class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public IAuthenticatedUser create(CreateUserRequest createUserRequest) {
        try {
            try (Response result = securityClient.post(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.getUsers(),
                    mapper.writeValueAsString(UserDTOConverter.convert(createUserRequest)))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsers(), result.getStatus());
                return mapper.readValue(result.readEntity(String.class), UserDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public IAuthenticatedUser updatePassword(String username, String oldPassword, String newPassword) {
        try {
            try (Response result = securityClient.post(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.updateUserPassword(username),
                    mapper.writeValueAsString(new UpdatePasswordRequest(oldPassword, newPassword)))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.updateUserPassword(username), result.getStatus());
                return mapper.readValue(result.readEntity(String.class), UserDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public IAuthenticatedUser updateUser(CreateUserRequest createUserRequest) {
        try {
            try (Response result = securityClient.put(userUrlConstructor.getUserManagerServerUrl(), userUrlConstructor.getUsers(),
                    mapper.writeValueAsString(UserDTOConverter.convert(createUserRequest)))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getUsers(), result.getStatus());
                return mapper.readValue(result.readEntity(String.class), UserDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
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
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Collection<IAuthenticatedUser> findAll() {
        try {
            try (Response response = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getAll())) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor.getAll(), response.getStatus());
                return new ArrayList<>(mapper.readValue(response.readEntity(String.class), new TypeReference<List<UserDTO>>() {
                }));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
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
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
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
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getRoles(String username, String groupName, String applicationName) {
        final Set<String> roles = new HashSet<>();
        try {
            try (Response result = securityClient.get(userUrlConstructor.getUserManagerServerUrl(),
                    userUrlConstructor.getRolesByUserAndGroupAndApplication(username, groupName, applicationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        userUrlConstructor.getUserManagerServerUrl() + userUrlConstructor
                                .getRolesByUserAndGroupAndApplication(username, groupName, applicationName), result.getStatus());

                final List<UserRoleDTO> userRoles = mapper.readValue(result.readEntity(String.class), new TypeReference<List<UserRoleDTO>>() {
                });
                userRoles.forEach(userRoleDTO -> roles.add(userRoleDTO.getRole().getName()));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        }
        return roles;
    }


}

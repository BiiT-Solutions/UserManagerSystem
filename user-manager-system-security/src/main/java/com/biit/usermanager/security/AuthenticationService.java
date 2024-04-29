package com.biit.usermanager.security;

import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.NotAuthorizedException;
import com.biit.rest.exceptions.NotFoundException;
import com.biit.rest.exceptions.UnprocessableEntityException;
import com.biit.server.client.SecurityClient;
import com.biit.server.security.model.UpdatePasswordRequest;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.logger.AuthenticationServiceLogger;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.usermanager.security.models.CheckCredentialsRequest;
import com.biit.usermanager.security.providers.AuthenticationUrlConstructor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService<Long, Long> {
    private static final int CACHE_EXPIRATION_TIME = 10 * 60 * 1000;

    private final AuthenticationUrlConstructor authenticationUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    @Value("${spring.application.name}")
    private String applicationName;

    public AuthenticationService(AuthenticationUrlConstructor authenticationUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.authenticationUrlConstructor = authenticationUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
    }

    @Override
    public IUser<Long> authenticate(String email, String password) throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        // Login fails if either username or password is null
        if (email == null || password == null) {
            throw new InvalidCredentialsException("No fields filled up.");
        }

        try {
            try (Response response = securityClient.post(authenticationUrlConstructor.getUserManagerServerUrl(),
                    authenticationUrlConstructor.checkCredentials(), mapper.writeValueAsString(new CheckCredentialsRequest(null, email, password)))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authenticationUrlConstructor.getUserManagerServerUrl() + authenticationUrlConstructor.checkCredentials(),
                        response.getStatus());
                if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                    throw new UserDoesNotExistException("No user found with email '" + email + "'");
                }
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), UserDTO.class);
            }
        } catch (NotFoundException e) {
            throw new UserDoesNotExistException("Error connection to the User Manager System", e);
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IGroup<Long> getDefaultGroup(IUser<Long> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Cacheable(value = "users-by-mail", key = "#email")
    @Override
    public IUser<Long> getUserByEmail(String email) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
        if (email == null) {
            throw new UserDoesNotExistException("No fields filled up.");
        }

        try {
            try (Response response = securityClient.get(authenticationUrlConstructor.getUserManagerServerUrl(),
                    authenticationUrlConstructor.getUserByEmail(email))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authenticationUrlConstructor.getUserManagerServerUrl() + authenticationUrlConstructor.getUserByEmail(email),
                        response.getStatus());
                if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                    throw new UserDoesNotExistException("No user found with email '" + email + "'");
                }
                return mapper.readValue(response.readEntity(String.class), UserDTO.class);
            }
        } catch (NotFoundException e) {
            throw new UserDoesNotExistException("Error connection to the User Manager System", e);
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Cacheable(value = "users", key = "#userId")
    @Override
    public IUser<Long> getUserById(long userId) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
        try {
            try (Response response = securityClient.get(authenticationUrlConstructor.getUserManagerServerUrl(),
                    authenticationUrlConstructor.getUserById(userId))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authenticationUrlConstructor.getUserManagerServerUrl() + authenticationUrlConstructor.getUserById(userId),
                        response.getStatus());
                if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                    throw new UserDoesNotExistException("No user found with id '" + userId + "'");
                }
                return mapper.readValue(response.readEntity(String.class), UserDTO.class);
            }
        } catch (NotFoundException e) {
            throw new UserDoesNotExistException("Error connection to the User Manager System", e);
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public boolean isInGroup(IGroup<Long> group, IUser<Long> user) throws UserManagementException, InvalidCredentialsException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @CacheEvict(allEntries = true, value = {"users", "users-by-mail"})
    @Override
    public IUser<Long> updatePassword(IUser<Long> user, String plainTextPassword) throws UserDoesNotExistException,
            InvalidCredentialsException, UserManagementException {
        // Login fails if either username or password is null
        if (user == null || plainTextPassword == null) {
            throw new UserManagementException("Nothing to update.");
        }

        try {
            try (Response response = securityClient.put(authenticationUrlConstructor.getUserManagerServerUrl(),
                    authenticationUrlConstructor.updateUserPassword(user.getUniqueName()), mapper.writeValueAsString(new UpdatePasswordRequest(
                            null, plainTextPassword)))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authenticationUrlConstructor.getUserManagerServerUrl() + authenticationUrlConstructor.updateUserPassword(user.getUniqueName()),
                        response.getStatus());
                if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                    throw new UserDoesNotExistException("No user found with username '" + user.getUniqueName() + "'");
                }
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), UserDTO.class);
            }
        } catch (NotFoundException e) {
            throw new UserDoesNotExistException("Error connection to the User Manager System", e);
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @CacheEvict(allEntries = true, value = {"users", "users-by-mail"})
    @Override
    public IUser<Long> updateUser(IUser<Long> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
        // Login fails if either username or password is null
        if (user == null) {
            throw new UserManagementException("No user to update.");
        }

        try {
            try (Response response = securityClient.put(authenticationUrlConstructor.getUserManagerServerUrl(),
                    authenticationUrlConstructor.updateUser(), mapper.writeValueAsString(user))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authenticationUrlConstructor.getUserManagerServerUrl() + authenticationUrlConstructor.updateUser(),
                        response.getStatus());
                if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                    throw new UserDoesNotExistException("User '" + user.getUniqueName() + "' cannot be updated as does not exists.");
                }
                if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new InvalidCredentialsException("Invalid JWT credentials!");
                }
                return mapper.readValue(response.readEntity(String.class), UserDTO.class);
            }
        } catch (NotFoundException e) {
            throw new UserDoesNotExistException("Error connection to the User Manager System", e);
        } catch (JsonProcessingException | EmptyResultException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }


    @CacheEvict(allEntries = true, value = {"users", "groups", "users-by-mail"})
    @Scheduled(fixedDelay = CACHE_EXPIRATION_TIME)
    @Override
    public void reset() {
        //Only for handling Spring cache.
    }

    @Override
    public IUser<Long> addUser(IGroup<Long> group, String password, String screenName, String emailAddress, String locale, String firstName,
                               String middleName, String lastName) throws UserManagementException, InvalidCredentialsException {
        final UserDTO userDTO = new UserDTO();
        userDTO.setPassword(password);
        userDTO.setUsername(screenName);
        userDTO.setEmail(emailAddress);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);

        return addUser(userDTO);
    }

    @Override
    public IUser<Long> addUser(IUser<Long> userDTO) throws UserManagementException, InvalidCredentialsException {
        try (Response response = securityClient.post(authenticationUrlConstructor.getUserManagerServerUrl(),
                authenticationUrlConstructor.addUser(), mapper.writeValueAsString(userDTO))) {
            AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                    authenticationUrlConstructor.getUserManagerServerUrl() + authenticationUrlConstructor.updateUser(),
                    response.getStatus());
            if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                throw new InvalidCredentialsException("Invalid JWT credentials!");
            }
            return mapper.readValue(response.readEntity(String.class), UserDTO.class);
        } catch (JsonProcessingException | UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @CacheEvict(allEntries = true, value = {"users", "groups", "users-by-mail"})
    @Override
    public void deleteUser(IUser<Long> user) throws UserManagementException, InvalidCredentialsException {
        try (Response response = securityClient.delete(authenticationUrlConstructor.getUserManagerServerUrl(),
                authenticationUrlConstructor.delete(user.getUniqueId()))) {
            AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                    authenticationUrlConstructor.getUserManagerServerUrl() + authenticationUrlConstructor.updateUser(),
                    response.getStatus());
            if (response.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
                throw new InvalidCredentialsException("Invalid JWT credentials!");
            }
        } catch (UnprocessableEntityException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public void createBeans() {

    }
}

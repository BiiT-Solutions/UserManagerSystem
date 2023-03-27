package com.biit.usermanager.security;

import com.biit.rest.exceptions.EmptyResultException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
public class AuthenticationService implements IAuthenticationService<Long, Long> {

    private final AuthenticationUrlConstructor authenticationUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    public AuthenticationService(AuthenticationUrlConstructor authenticationUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.authenticationUrlConstructor = authenticationUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
    }

    @Override
    public IUser<Long> authenticate(String email, String password) throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        // Login fails if either the username or password is null
        if (email == null || password == null) {
            throw new InvalidCredentialsException("No fields filled up.");
        }

        try {
            try (final Response response = securityClient.post(authenticationUrlConstructor.getUserManagerServerUrl(),
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
        } catch (JsonProcessingException | EmptyResultException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IGroup<Long> getDefaultGroup(IUser<Long> user) throws UserManagementException {
        return null;
    }

    @Override
    public IUser<Long> getUserByEmail(String email) throws UserManagementException, UserDoesNotExistException {
        if (email == null) {
            throw new UserDoesNotExistException("No fields filled up.");
        }

        try {
            try (final Response response = securityClient.get(authenticationUrlConstructor.getUserManagerServerUrl(),
                    authenticationUrlConstructor.getUserByEmail(email))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authenticationUrlConstructor.getUserManagerServerUrl() + authenticationUrlConstructor.getUserByEmail(email),
                        response.getStatus());
                if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                    throw new UserDoesNotExistException("No user found with email '" + email + "'");
                }
                return mapper.readValue(response.readEntity(String.class), UserDTO.class);
            }
        } catch (JsonProcessingException | EmptyResultException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IUser<Long> getUserById(long userId) throws UserManagementException, UserDoesNotExistException {
        try {
            try (final Response response = securityClient.get(authenticationUrlConstructor.getUserManagerServerUrl(),
                    authenticationUrlConstructor.getUserById(userId))) {
                AuthenticationServiceLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        authenticationUrlConstructor.getUserManagerServerUrl() + authenticationUrlConstructor.getUserById(userId),
                        response.getStatus());
                if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
                    throw new UserDoesNotExistException("No user found with id '" + userId + "'");
                }
                return mapper.readValue(response.readEntity(String.class), UserDTO.class);
            }
        } catch (JsonProcessingException | EmptyResultException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public boolean isInGroup(IGroup<Long> group, IUser<Long> user) throws UserManagementException {
        return false;
    }

    @Override
    public IUser<Long> updatePassword(IUser<Long> user, String plainTextPassword) throws UserDoesNotExistException,
            InvalidCredentialsException, UserManagementException {
        // Login fails if either the username or password is null
        if (user == null || plainTextPassword == null) {
            throw new UserManagementException("No fields filled up.");
        }

        try {
            try (final Response response = securityClient.post(authenticationUrlConstructor.getUserManagerServerUrl(),
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
        } catch (JsonProcessingException | EmptyResultException e) {
            throw new UserManagementException("Error connection to the User Manager System", e);
        }
    }

    @Override
    public IUser<Long> updateUser(IUser<Long> user) throws UserManagementException {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public IUser<Long> addUser(IGroup<Long> company, String password, String screenName, String emailAddress, String locale, String firstName,
                               String middleName, String lastName) throws UserManagementException {
        return null;
    }

    @Override
    public void deleteUser(IUser<Long> user) throws UserManagementException {

    }

    @Override
    public void createBeans() {

    }
}

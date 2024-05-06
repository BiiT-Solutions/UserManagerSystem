package com.biit.usermanager.client.security;

import com.biit.server.security.IAuthenticatedUser;
import com.biit.usermanager.client.exceptions.ActionNotAllowedException;
import com.biit.usermanager.client.provider.UserManagerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class SecurityController {

    @Value("${spring.application.name:}")
    private String backendServiceName;

    private final UserManagerClient userManagerClient;

    public SecurityController(UserManagerClient userManagerClient) {
        this.userManagerClient = userManagerClient;
    }

    /**
     * Checks if a user has at least one required role to perform an action. Is using current application for roles.
     *
     * @param applicationName the application where the roles must be checked.
     * @param jwtUserName     user logged in.
     * @param userToCheck     user to read data from.
     * @param requiredRoles   roles needed to perform the action.
     */
    public void checkIfCanSeeUserData(String applicationName, String jwtUserName, UUID userToCheck, String... requiredRoles) {
        checkIfCanSeeUserData(applicationName, backendServiceName, jwtUserName, userToCheck, requiredRoles);
    }

    /**
     * Checks if a user has at least one required role to perform an action.
     *
     * @param backendServiceName the backend service where the roles are obtained.
     * @param applicationName    the application where the roles must be checked.
     * @param jwtUserName        user logged in.
     * @param userToCheck        user to read data from.
     * @param requiredRoles      roles needed to perform the action.
     */
    public void checkIfCanSeeUserData(String applicationName, String backendServiceName, String jwtUserName, UUID userToCheck, String... requiredRoles) {
        checkIfCanSeeUserData(jwtUserName, userToCheck, userManagerClient.getRoles(jwtUserName, applicationName),
                checkRolesFormat(backendServiceName, requiredRoles));
    }

    private String[] checkRolesFormat(String backendServiceName, String[] roles) {
        //Roles are always BACKEND_ROLE. Ensuring that includes it.
        final String[] checkedRoles = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            if (!roles[i].toUpperCase().startsWith(backendServiceName.toUpperCase())) {
                checkedRoles[i] = backendServiceName.toUpperCase() + "_" + roles[i];
            } else {
                checkedRoles[i] = roles[i];
            }
        }
        return checkedRoles;
    }

    private void checkIfCanSeeUserData(String jwtUserName, UUID userToCheck, Set<String> userRoles, String... requiredRoles) {
        final Optional<IAuthenticatedUser> user = userManagerClient.findByUsername(jwtUserName);
        if (user.isEmpty()) {
            throw new ActionNotAllowedException(this.getClass(), "No user exists with username '" + jwtUserName + "'.");
        }
        if (Objects.equals(UUID.fromString(user.get().getUID()), userToCheck)) {
            //Same user. Allow.
            return;
        }
        //If it is not admin, cannot see other users' data.
        final Set<String> rolesFromUser = userRoles.stream().map(String::toUpperCase).collect(Collectors.toSet());
        final Set<String> rolesToCheck = Arrays.stream(requiredRoles).map(String::toUpperCase).collect(Collectors.toSet());
        if (Collections.disjoint(rolesFromUser, rolesToCheck)) {
            throw new ActionNotAllowedException(this.getClass(), "You are not allowed to get this data.");
        }
    }
}

package com.biit.usermanager.client.security;

import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.server.security.ISecurityController;
import com.biit.server.security.exceptions.ActionNotAllowedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@Order(Ordered.HIGHEST_PRECEDENCE)
@Qualifier("securityController")
public class OnMemorySecurityController implements ISecurityController {

    @Value("${spring.application.name:}")
    private String backendServiceName;

    private final IAuthenticatedUserProvider authenticatedUserProvider;

    public OnMemorySecurityController(IAuthenticatedUserProvider authenticatedUserProvider) {
        this.authenticatedUserProvider = authenticatedUserProvider;
    }


    @Override
    public void checkIfCanSeeUserData(String jwtUserName, UUID userToCheck, String... requiredRoles) {
        checkIfCanSeeUserData(backendServiceName, backendServiceName, jwtUserName, userToCheck, requiredRoles);
    }

    @Override
    public void checkIfCanSeeUserData(String applicationName, String jwtUserName, UUID userToCheck, String... requiredRoles) {
        checkIfCanSeeUserData(applicationName, backendServiceName, jwtUserName, userToCheck, requiredRoles);
    }

    @Override
    public void checkIfCanSeeUserData(String applicationName, String backendServiceName, String jwtUserName, UUID userToCheck, String... requiredRoles) {
        checkIfCanSeeUserData(jwtUserName, userToCheck, authenticatedUserProvider.getRoles(jwtUserName, applicationName),
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
        final Optional<IAuthenticatedUser> user = authenticatedUserProvider.findByUsername(jwtUserName);
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

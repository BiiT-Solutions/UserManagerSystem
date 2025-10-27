package com.biit.usermanager.client.security;

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

import com.biit.server.security.ISecurityController;
import com.biit.server.security.exceptions.ActionNotAllowedException;
import com.biit.usermanager.client.providers.UserManagerClient;
import com.biit.usermanager.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
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
@Order(1)
public class UserClientSecurityController implements ISecurityController {

    @Value("${spring.application.name:}")
    private String backendServiceName;

    private final UserManagerClient userManagerClient;

    public UserClientSecurityController(UserManagerClient userManagerClient) {
        this.userManagerClient = userManagerClient;
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
        final Optional<UserDTO> user = userManagerClient.findByUsername(jwtUserName);
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

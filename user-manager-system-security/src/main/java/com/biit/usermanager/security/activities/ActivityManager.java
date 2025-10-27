package com.biit.usermanager.security.activities;

/*-
 * #%L
 * User Manager System (Security)
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

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.security.AuthorizationService;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IActivityManager;
import com.biit.usermanager.security.IRoleActivities;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.OrganizationDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ActivityManager implements IActivityManager<Long, Long, String> {

    private final AuthorizationService authorizationService;

    private final IRoleActivities<String> roleActivities;

    public ActivityManager(AuthorizationService authorizationService, IRoleActivities<String> roleActivities) {
        this.authorizationService = authorizationService;
        this.roleActivities = roleActivities;
    }

    @Override
    public Set<IActivity> getRoleActivities(IRole<String> role) {
        return roleActivities.getRoleActivities(role);
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) throws UserManagementException, UserDoesNotExistException,
            InvalidCredentialsException {
        if (user == null) {
            return false;
        }
        if (getUserActivitiesAllowed(user).contains(activity)) {
            UserManagerLogger.info(this.getClass().getName(), "User  '" + user + "' authorized for '" + activity + "'.");
            return true;
        } else {
            UserManagerLogger.warning(this.getClass().getName(), "User  '" + user + "' not authorized for '" + activity + "'.");
            return false;
        }
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> group, IActivity activity) throws UserManagementException,
            UserDoesNotExistException, InvalidCredentialsException, OrganizationDoesNotExistException {
        if (user == null) {
            UserManagerLogger.warning(this.getClass().getName(), "Provided user is null.");
            return false;
        }
        // If user has the permission... no need to check the group.
        if (isAuthorizedActivity(user, activity)) {
            UserManagerLogger.debug(this.getClass().getName(), "User  '" + user + "' is authorized for '" + activity + "' in any group.");
            return true;
        }

        final boolean authorized = getUserActivitiesAllowed(user, group).contains(activity);
        UserManagerLogger.debug(this.getClass().getName(),
                "User  '" + user + "' authorized '" + authorized + "' for '" + activity + "' in group '" + group + "'.");
        return authorized;
    }

    private Set<IActivity> getUserActivitiesAllowed(IUser<Long> user) throws UserManagementException, InvalidCredentialsException,
            UserDoesNotExistException {
        final Set<IActivity> activities = new HashSet<>();
        if (user != null) {
            final Set<IRole<String>> roles = authorizationService.getUserRoles(user);

            // Add roles obtained by group.
            final Set<IGroup<Long>> userGroups = authorizationService.getUserGroups(user);
            for (final IGroup<Long> group : userGroups) {
                try {
                    roles.addAll(authorizationService.getUserGroupRoles(group));
                } catch (OrganizationDoesNotExistException e) {
                    UserManagerLogger.errorMessage(this.getClass(), e);
                }
            }

            // Activities by role.
            for (final IRole<String> role : roles) {
                final Set<IActivity> roleActivities = getRoleActivities(role);
                activities.addAll(roleActivities);
            }
        }
        UserManagerLogger.debug(this.getClass().getName(), "Activities allowed for user '" + user + "' are '" + activities + "'.");
        return activities;
    }

    private Set<IActivity> getUserActivitiesAllowed(IUser<Long> user, IGroup<Long> group) throws UserManagementException, InvalidCredentialsException,
            UserDoesNotExistException, OrganizationDoesNotExistException {
        final Set<IActivity> groupActivities = new HashSet<>();
        if (user != null && group != null) {
            // Add roles obtained by group.
            for (final IRole<String> role : authorizationService.getUserRoles(user, group)) {
                groupActivities.addAll(getRoleActivities(role));
            }
        }
        return groupActivities;
    }

    @Override
    public IRoleActivities<String> getRoleActivities() {
        return roleActivities;
    }

    @Override
    public void setRoleActivities(IRoleActivities roleActivities) {
        throw new UnsupportedOperationException("Using bean injection for this!");
    }
}

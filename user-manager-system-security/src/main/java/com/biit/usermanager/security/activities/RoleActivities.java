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

import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IRoleActivities;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class RoleActivities implements IRoleActivities<String> {

    private final Set<IActivity> activities = new HashSet<>();

    private final Set<RoleDefinition> definedRoles = new HashSet<>();

    @Override
    public Set<IActivity> getRoleActivities(IRole<String> role) {
        if (role != null) {
            return getRoleActivities(role.getUniqueName());
        }
        return null;
    }

    @Override
    public Set<IActivity> getRoleActivities(String roleName) {
        if (roleName != null) {
            final RoleDefinition roleDefinition = getRole(roleName);
            if (roleDefinition != null) {
                UserManagerLogger.debug(this.getClass().getName(), "Activities found for role '" + roleName + "' are '"
                        + roleDefinition.getActivities() + "'.");
                return roleDefinition.getActivities();
            }
        }
        return new HashSet<>();
    }

    @PostConstruct
    private void updateRoles() {
        for (final String roleName : RoleConfigurationReader.getInstance().getAllRolesDefinitions()) {
            UserManagerLogger.debug(this.getClass().getName(), "Role definition '" + roleName + "' found.");
            final RoleDefinition role = new RoleDefinition(roleName,
                    activitiesConverter(RoleConfigurationReader.getInstance().getRoleActivities(roleName)),
                    RoleConfigurationReader.getInstance().getRoleTranslationCode(roleName));
            definedRoles.add(role);
            UserManagerLogger.debug(this.getClass().getName(), "Added role definition '" + role + "'.");
        }
    }

    private Set<IActivity> activitiesConverter(String... activitiesTags) {
        final Set<IActivity> activities = new HashSet<>();
        for (final String tag : activitiesTags) {
            final IActivity activity = getByTag(tag);
            if (activity == null) {
                final IActivity newActivity = new Activity(tag);
                activities.add(newActivity);
                this.activities.add(newActivity);
            } else {
                activities.add(activity);
            }
        }
        return activities;
    }

    public IActivity getByTag(String tag) {
        for (final IActivity activity : activities) {
            if (activity.getTag().equalsIgnoreCase(tag)) {
                return activity;
            }
        }
        return null;
    }

    public RoleDefinition getRole(String name) {
        return definedRoles.stream().filter(roleDefinition -> roleDefinition.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}

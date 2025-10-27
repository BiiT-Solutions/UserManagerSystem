package com.biit.usermanager.security.providers;

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

import com.biit.usermanager.security.exceptions.InvalidConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUrlConstructor {

    @Value("${usermanager.server.url:null}")
    private String userManagerServerUrl;

    public String getUserManagerServerUrl() {
        if (userManagerServerUrl == null) {
            throw new InvalidConfigurationException(this.getClass(), "Value 'usermanager.server.url' not set on 'application.properties'!");
        }
        return userManagerServerUrl;
    }

    private String getUsers() {
        return "/users";
    }

    public String getUserByUsername(String username) {
        return "/users/usernames/" + username;
    }

    private String getRoles() {
        return "/roles";
    }


    private String getGroups() {
        return "/groups";
    }

    public String getAllUsers() {
        return getUsers();
    }


    public String getGroups(Long id) {
        return getGroups() + "/" + id;
    }

    public String getGroupByName(String groupName, String applicationName) {
        return getGroups() + "/groups/" + groupName + "/applications/" + applicationName;
    }

    public String getAllGroups() {
        return getGroups();
    }

    public String getAllGroupsWithoutParent() {
        return getGroups() + "/no-parent";
    }

    public String getRoleByName(String name) {
        return getRoles() + "/" + name;
    }

}

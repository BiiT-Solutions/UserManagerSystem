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

import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Component
public class TeamUrlConstructor {

    @Value("${usermanager.server.url:#{null}}")
    private String userManagerServerUrl;

    public String getUserManagerServerUrl() {
        if (userManagerServerUrl == null) {
            throw new InvalidConfigurationException(this.getClass(), "Value 'usermanager.server.url' not set on 'application.properties'!");
        }
        return userManagerServerUrl;
    }

    public String getTeams() {
        return "/teams";
    }

    public String getTeamsByUser(String userUuid) {
        return getTeams() + "/users/" + UriUtils.encode(userUuid, StandardCharsets.UTF_8);
    }

    public String getTeamsByOrganization(String organizationName) {
        return getTeams() + "/organizations/" + UriUtils.encode(organizationName, StandardCharsets.UTF_8);
    }

    public String addUsersByUsername(String teamName, String organizationName) {
        return getTeams() + "/names/" + UriUtils.encode(teamName, StandardCharsets.UTF_8)
                + "/organizations/" + UriUtils.encode(organizationName, StandardCharsets.UTF_8)
                + "/usernames";
    }


}

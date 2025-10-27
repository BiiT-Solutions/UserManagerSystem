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

import com.biit.rest.exceptions.InvalidResponseException;
import com.biit.server.client.SecurityClient;
import com.biit.server.security.IUserTeamProvider;
import com.biit.usermanager.client.exceptions.ElementNotFoundException;
import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.logger.UserManagerClientLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Order(2)
@Qualifier("teamManagerClient")
public class TeamManagerClient implements IUserTeamProvider<TeamDTO> {

    private final TeamUrlConstructor teamUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    public TeamManagerClient(TeamUrlConstructor teamUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.teamUrlConstructor = teamUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
    }


    public Collection<TeamDTO> findByUser(UUID userUuid) {
        if (userUuid == null) {
            return List.of();
        }
        return findByUser(userUuid.toString());
    }


    @Override
    public Collection<TeamDTO> findByUser(String userUuid) {
        try {
            try (Response response = securityClient.get(teamUrlConstructor.getUserManagerServerUrl(),
                    teamUrlConstructor.getTeamsByUser(userUuid))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        teamUrlConstructor.getUserManagerServerUrl() + teamUrlConstructor.getTeamsByUser(userUuid), response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No user found with uuid '" + userUuid + "'.");
                }
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), TeamDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }

    @Override
    public Collection<TeamDTO> findByOrganization(String organizationName) throws ElementNotFoundException {
        try {
            try (Response response = securityClient.get(teamUrlConstructor.getUserManagerServerUrl(),
                    teamUrlConstructor.getTeamsByOrganization(organizationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        teamUrlConstructor.getUserManagerServerUrl() + teamUrlConstructor.getTeamsByOrganization(organizationName), response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No organization found with name '" + organizationName + "'.");
                }
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), TeamDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }

    @Override
    public TeamDTO assign(String teamName, String organizationName, String... userNames) throws ElementNotFoundException {
        try {
            try (Response response = securityClient.post(teamUrlConstructor.getUserManagerServerUrl(),
                    teamUrlConstructor.addUsersByUsername(teamName, organizationName), mapper.writeValueAsString(userNames))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        teamUrlConstructor.getUserManagerServerUrl()
                                + teamUrlConstructor.addUsersByUsername(teamName, organizationName), response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No team with name '" + teamName
                            + "' found on organization '" + organizationName + "'.");
                }
                return mapper.readValue(response.readEntity(String.class), TeamDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }
}

package com.biit.usermanager.client.providers;

import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.InvalidResponseException;
import com.biit.server.client.SecurityClient;
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
import java.util.UUID;

@Service
@Order(2)
@Qualifier("teamManagerClient")
public class TeamManagerClient {

    private final TeamUrlConstructor teamUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    public TeamManagerClient(TeamUrlConstructor teamUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.teamUrlConstructor = teamUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
    }


    public Collection<TeamDTO> findByUser(UUID userUuid) {
        try {
            try (Response response = securityClient.get(teamUrlConstructor.getUserManagerServerUrl(),
                    teamUrlConstructor.getTeamsByUser(userUuid))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        teamUrlConstructor.getUserManagerServerUrl() + teamUrlConstructor.getTeamsByUser(userUuid), response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), TeamDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }


    public Collection<TeamDTO> findByOrganization(String organizationName) {
        try {
            try (Response response = securityClient.get(teamUrlConstructor.getUserManagerServerUrl(),
                    teamUrlConstructor.getTeamsByOrganization(organizationName))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        teamUrlConstructor.getUserManagerServerUrl() + teamUrlConstructor.getTeamsByOrganization(organizationName), response.getStatus());
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), TeamDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }


    public TeamDTO assign(String teamName, String organizationName, Collection<String> userNames) {
        try {
            try (Response response = securityClient.post(teamUrlConstructor.getUserManagerServerUrl(),
                    teamUrlConstructor.addUsersByUsername(teamName, organizationName), mapper.writeValueAsString(userNames))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        teamUrlConstructor.getUserManagerServerUrl()
                                + teamUrlConstructor.addUsersByUsername(teamName, organizationName), response.getStatus());
                return mapper.readValue(response.readEntity(String.class), TeamDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (EmptyResultException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }
}

package com.biit.usermanager.client.providers;

import com.biit.rest.exceptions.InvalidResponseException;
import com.biit.server.client.SecurityClient;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.usermanager.client.exceptions.ElementNotFoundException;
import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import com.biit.usermanager.dto.OrganizationDTO;
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
@Qualifier("organizationManagerClient")
public class OrganizationManagerClient implements IUserOrganizationProvider<OrganizationDTO> {

    private final OrganizationUrlConstructor organizationUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    public OrganizationManagerClient(OrganizationUrlConstructor organizationUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.organizationUrlConstructor = organizationUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
    }


    @Override
    public OrganizationDTO findByName(String name) {
        try {
            try (Response response = securityClient.get(organizationUrlConstructor.getUserManagerServerUrl(),
                    organizationUrlConstructor.getOrganization(name))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        organizationUrlConstructor.getUserManagerServerUrl() + organizationUrlConstructor.getOrganization(name),
                        response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No organizations found with name '" + name + "'.");
                }
                return mapper.readValue(response.readEntity(String.class), OrganizationDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }


    @Override
    public Collection<OrganizationDTO> findByUsername(String username) {
        try {
            try (Response response = securityClient.get(organizationUrlConstructor.getUserManagerServerUrl(),
                    organizationUrlConstructor.getOrganizationsByUserName(username))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        organizationUrlConstructor.getUserManagerServerUrl() + organizationUrlConstructor.getOrganizationsByUserName(username),
                        response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No organizations found for username '" + username + "'.");
                }
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), OrganizationDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }


    public Collection<OrganizationDTO> findByUserUID(UUID userUuid) {
        if (userUuid == null) {
            return List.of();
        }
        return findByUserUID(userUuid.toString());
    }


    @Override
    public Collection<OrganizationDTO> findByUserUID(String uuid) {
        try {
            try (Response response = securityClient.get(organizationUrlConstructor.getUserManagerServerUrl(),
                    organizationUrlConstructor.getOrganizationsByUser(uuid))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        organizationUrlConstructor.getUserManagerServerUrl() + organizationUrlConstructor.getOrganizationsByUser(uuid),
                        response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No organizations found for uuid '" + uuid + "'.");
                }
                return Arrays.asList(mapper.readValue(response.readEntity(String.class), OrganizationDTO[].class));
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }
}

package com.biit.usermanager.client.providers;

import com.biit.rest.exceptions.InvalidResponseException;
import com.biit.server.client.SecurityClient;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.usermanager.client.exceptions.ElementNotFoundException;
import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.logger.UserManagerClientLogger;
import com.biit.utils.pool.BasePool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Order(2)
@Qualifier("organizationManagerClient")
public class OrganizationManagerClient implements IUserOrganizationProvider<OrganizationDTO> {

    private static final Long CACHE_EXPIRATION_TIME = 30000L;

    private final OrganizationUrlConstructor organizationUrlConstructor;

    private final SecurityClient securityClient;

    private final ObjectMapper mapper;

    private final OrganizationsByUserCache organizationsByUserCache = new OrganizationsByUserCache();
    private final OrganizationByNameCache organizationByNameCache = new OrganizationByNameCache();

    static class OrganizationByNameCache extends BasePool<String, OrganizationDTO> {

        @Override
        public long getExpirationTime() {
            return CACHE_EXPIRATION_TIME;
        }

        @Override
        public boolean isDirty(OrganizationDTO element) {
            return false;
        }
    }

    static class OrganizationsByUserCache extends BasePool<String, Collection<OrganizationDTO>> {

        @Override
        public long getExpirationTime() {
            return CACHE_EXPIRATION_TIME;
        }

        @Override
        public boolean isDirty(Collection<OrganizationDTO> elements) {
            return false;
        }
    }

    public OrganizationManagerClient(OrganizationUrlConstructor organizationUrlConstructor, SecurityClient securityClient, ObjectMapper mapper) {
        this.organizationUrlConstructor = organizationUrlConstructor;
        this.securityClient = securityClient;
        this.mapper = mapper;
    }


    @Override
    public OrganizationDTO findByName(String name) {
        try {
            final OrganizationDTO cachedOrganization = organizationByNameCache.getElement(name);
            if (cachedOrganization != null) {
                return cachedOrganization;
            }
            try (Response response = securityClient.get(organizationUrlConstructor.getUserManagerServerUrl(),
                    organizationUrlConstructor.getOrganization(name))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        organizationUrlConstructor.getUserManagerServerUrl() + organizationUrlConstructor.getOrganization(name),
                        response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No organizations found with name '" + name + "'.");
                }
                final OrganizationDTO result = mapper.readValue(response.readEntity(String.class), OrganizationDTO.class);
                if (result != null) {
                    organizationByNameCache.addElement(result, name);
                }
                return result;
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return null;
        }
    }

    /**
     * Organizations are sorted by time when the user has been assigned to DESC.
     *
     * @param username the user that belongs to these organizations.
     * @return
     */
    @Override
    public Collection<OrganizationDTO> findByUsername(String username) {
        try {
            final Collection<OrganizationDTO> cachedOrganizations = organizationsByUserCache.getElement(username);
            if (cachedOrganizations != null) {
                return cachedOrganizations;
            }
            try (Response response = securityClient.get(organizationUrlConstructor.getUserManagerServerUrl(),
                    organizationUrlConstructor.getOrganizationsByUserName(username))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        organizationUrlConstructor.getUserManagerServerUrl() + organizationUrlConstructor.getOrganizationsByUserName(username),
                        response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No organizations found for username '" + username + "'.");
                }
                final List<OrganizationDTO> results = Arrays.asList(mapper.readValue(response.readEntity(String.class), OrganizationDTO[].class));
                organizationsByUserCache.addElement(results, username);
                return results;
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new ArrayList<>();
        }
    }


    public Collection<OrganizationDTO> findByUserUID(UUID userUuid) {
        if (userUuid == null) {
            return List.of();
        }
        return findByUserUID(userUuid.toString());
    }

    /**
     * Organizations are sorted by time when the user has been assigned to DESC.
     *
     * @param uuid the user that belongs to these organizations.
     * @return
     */
    @Override
    public Collection<OrganizationDTO> findByUserUID(String uuid) {
        try {
            final Collection<OrganizationDTO> cachedOrganizations = organizationsByUserCache.getElement(uuid);
            if (cachedOrganizations != null) {
                return cachedOrganizations;
            }
            try (Response response = securityClient.get(organizationUrlConstructor.getUserManagerServerUrl(),
                    organizationUrlConstructor.getOrganizationsByUser(uuid))) {
                UserManagerClientLogger.debug(this.getClass(), "Response obtained from '{}' is '{}'.",
                        organizationUrlConstructor.getUserManagerServerUrl() + organizationUrlConstructor.getOrganizationsByUser(uuid),
                        response.getStatus());
                if (Objects.equals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode())) {
                    throw new ElementNotFoundException(this.getClass(), "No organizations found for uuid '" + uuid + "'.");
                }
                final List<OrganizationDTO> results = Arrays.asList(mapper.readValue(response.readEntity(String.class), OrganizationDTO[].class));
                organizationsByUserCache.addElement(results, uuid);
                return results;
            }
        } catch (JsonProcessingException e) {
            throw new InvalidResponseException(e);
        } catch (InvalidConfigurationException e) {
            UserManagerClientLogger.warning(this.getClass(), e.getMessage());
            return new ArrayList<>();
        }
    }
}

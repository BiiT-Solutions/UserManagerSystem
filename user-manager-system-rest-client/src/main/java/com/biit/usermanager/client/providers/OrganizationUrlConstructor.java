package com.biit.usermanager.client.providers;

import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Component
public class OrganizationUrlConstructor {

    @Value("${usermanager.server.url:#{null}}")
    private String userManagerServerUrl;

    public String getUserManagerServerUrl() {
        if (userManagerServerUrl == null) {
            throw new InvalidConfigurationException(this.getClass(), "Value 'usermanager.server.url' not set on 'application.properties'!");
        }
        return userManagerServerUrl;
    }

    public String getOrganizations() {
        return "/organizations";
    }

    public String getOrganizationsByUser(String userUuid) {
        return getOrganizations() + "/users/" + UriUtils.encode(userUuid, StandardCharsets.UTF_8);
    }

    public String getOrganizationsByUserName(String username) {
        return getOrganizations() + "/users/names/" + UriUtils.encode(String.valueOf(username), StandardCharsets.UTF_8);
    }

    public String getOrganization(String name) {
        return getOrganizations() + "/" + UriUtils.encode(name, StandardCharsets.UTF_8);
    }
}

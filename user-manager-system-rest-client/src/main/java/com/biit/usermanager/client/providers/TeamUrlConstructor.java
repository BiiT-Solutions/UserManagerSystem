package com.biit.usermanager.client.providers;

import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

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

    public String getTeamsByUser(UUID userUuid) {
        return getTeams() + "/users/" + UriUtils.encode(String.valueOf(userUuid), StandardCharsets.UTF_8);
    }

    public String getTeamsByOrganization(String organizationName) {
        return getTeams() + "/organizations/" + UriUtils.encode(String.valueOf(organizationName), StandardCharsets.UTF_8);
    }

    public String addUsersByUsername(String teamName, String organizationName) {
        return getTeams() + "/names/" + UriUtils.encode(teamName, StandardCharsets.UTF_8).replace("+", "%20")
                + "/organizations/" + UriUtils.encode(organizationName, StandardCharsets.UTF_8).replace("+", "%20")
                + "/usernames";
    }


}

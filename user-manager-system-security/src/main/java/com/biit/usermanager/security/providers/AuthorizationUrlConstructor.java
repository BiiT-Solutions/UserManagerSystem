package com.biit.usermanager.security.providers;

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
        return getRoles() + "/names/" + name;
    }

}

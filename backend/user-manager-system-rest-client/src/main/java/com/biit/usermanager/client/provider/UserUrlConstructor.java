package com.biit.usermanager.client.provider;

import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserUrlConstructor {

    @Value("${usermanager.server.url:null}")
    private String userManagerServerUrl;

    public String getUserManagerServerUrl() {
        if (userManagerServerUrl == null) {
            throw new InvalidConfigurationException(this.getClass(), "Value 'usermanager.server.url' not set on 'application.properties'!");
        }
        return userManagerServerUrl;
    }

    public String getUsers() {
        return "/users";
    }

    public String getUserRoles() {
        return "/roles/users";
    }

    public String getUserByName(String username) {
        return getUsers() + "/usernames/" + username;
    }

    public String getUserByEmail(String email) {
        return getUsers() + "/emails/" + email;
    }

    public String getUserByNameAndApplication(String username, String applicationName) {
        if (applicationName == null) {
            return getUserByName(username);
        }
        return getUsers() + "/usernames/" + username + "/applications/" + applicationName;
    }

    public String getUserByEmailAndApplication(String email, String applicationName) {
        if (applicationName == null) {
            return getUserByEmail(email);
        }
        return getUsers() + "/emails/" + email + "/applications/" + applicationName;
    }

    public String getUserById(String id) {
        return getUsers() + "/ids/" + id;
    }

    public String updatePassword() {
        return getUsers() + "/password";
    }

    public String updateUserPassword(String username) {
        return getUsers() + "/" + username + "/password";
    }

    public String count() {
        return getUsers() + "/count";
    }

    public String getAll() {
        return getUsers();
    }

    public String delete() {
        return getUsers() + "/delete";
    }

    public String deleteByUsername(String username) {
        return getUsers() + "/" + username;
    }

    public String getRolesByUserAndGroupAndApplication(String username, String groupName, String applicationName) {
        return getUserRoles() + "/usernames/" + username + "/groups/" + groupName + "/applications/" + applicationName;
    }

}

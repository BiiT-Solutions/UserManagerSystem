package com.biit.usermanager.security.providers;

import com.biit.usermanager.security.exceptions.InvalidConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUrlConstructor {

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

    private String getUserRoles() {
        return "/roles/users";
    }

    public String getUserByName(String username) {
        return getUsers() + "/usernames/" + username;
    }

    public String getUserByEmail(String email) {
        return getUsers() + "/emails/" + email;
    }

    public String getUserById(Long id) {
        return getUsers() + "/ids/" + id;
    }

    public String checkCredentials() {
        return getUsers() + "/credentials";
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

    public String updateUser() {
        return getUsers();
    }

    public String addUser() {
        return getUsers();
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

    public String delete(Long userId) {
        return getUsers() + "/" + userId;
    }

    public String deleteByUsername(String username) {
        return getUsers() + "/" + username;
    }

    public String getRolesByUser(String username) {
        return getUserRoles() + "/usernames/" + username;
    }

    public String getRolesByUserAndGroupAndApplication(String username, String groupName, String applicationName) {
        return getUserRoles() + "/usernames/" + username + "/groups/" + groupName + "/applications/" + applicationName;
    }

}

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

    public String getUsers() {
        return "/users";
    }

    public String getUserRoles() {
        return "/roles/users";
    }

    public String getUserByName(String username) {
        return getUsers() + "/username/" + username;
    }

    public String getUserByEmail(String email) {
        return getUsers() + "/email/" + email;
    }

    public String checkCredentials() {
        return getUsers() + "/credentials";
    }

    public String getUserByNameAndApplication(String username, String applicationName) {
        if (applicationName == null) {
            return getUserByName(username);
        }
        return getUsers() + "/username/" + username + "/application/" + applicationName;
    }

    public String getUserByEmailAndApplication(String email, String applicationName) {
        if (applicationName == null) {
            return getUserByEmail(email);
        }
        return getUsers() + "/email/" + email + "/application/" + applicationName;
    }

    public String getUserById(String id) {
        return getUsers() + "/id/" + id;
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

    public String getRolesByUserAndOrganizationAndApplication(String username, String organizationName, String applicationName) {
        return getUserRoles() + "/username/" + username + "/organization/" + organizationName + "/application/" + applicationName;
    }

}

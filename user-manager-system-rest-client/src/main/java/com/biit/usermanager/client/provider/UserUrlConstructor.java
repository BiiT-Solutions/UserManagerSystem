package com.biit.usermanager.client.provider;

import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class UserUrlConstructor {

    @Value("${usermanager.server.url:#{null}}")
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

    public String getBackendServiceRoles() {
        return "/backend-service-roles";
    }

    public String getUserByName(String username) {
        return getUsers() + "/usernames/" + URLEncoder.encode(username, StandardCharsets.UTF_8);
    }

    public String getUserByEmail(String email) {
        return getUsers() + "/emails/" + email;
    }

    public String getUserByNameAndApplication(String username, String applicationName) {
        if (applicationName == null) {
            return getUserByName(username);
        }
        return getUsers() + "/usernames/" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "/applications/"
                + URLEncoder.encode(applicationName, StandardCharsets.UTF_8);
    }

    public String getUserByNameAndBackendService(String username, String backendServiceName) {
        if (backendServiceName == null) {
            return getUserByName(username);
        }
        return getUsers() + "/usernames/" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "/service/"
                + URLEncoder.encode(backendServiceName, StandardCharsets.UTF_8);
    }

    public String getUserByEmailAndApplication(String email, String applicationName) {
        if (applicationName == null) {
            return getUserByEmail(email);
        }
        return getUsers() + "/emails/" + URLEncoder.encode(email, StandardCharsets.UTF_8) + "/applications/"
                + URLEncoder.encode(applicationName, StandardCharsets.UTF_8);
    }

    public String getUserById(String id) {
        return getUsers() + "/ids/" + id;
    }

    public String updatePassword() {
        return getUsers() + "/password";
    }

    public String updateUserPassword(String username) {
        return getUsers() + "/" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "/password";
    }

    public String getUserPassword(String username) {
        return getUsers() + "/" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "/password";
    }

    public String getUserPasswordByUid(String uid) {
        return getUsers() + "/uids/" + URLEncoder.encode(uid, StandardCharsets.UTF_8) + "/password";
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
        return getUsers() + "/" + URLEncoder.encode(username, StandardCharsets.UTF_8);
    }

    public String getRolesByUserAndGroupAndApplication(String username, String groupName, String applicationName) {
        return getBackendServiceRoles() + "/users/" + URLEncoder.encode(username, StandardCharsets.UTF_8) + "/groups/" + groupName
                + "/applications/" + applicationName;
    }

}

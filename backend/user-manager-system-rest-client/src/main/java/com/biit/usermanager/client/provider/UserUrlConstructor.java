package com.biit.usermanager.client.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserUrlConstructor {

    @Value("${usermanager.server.url}")
    private String userManagerServerUrl;

    public String getUserManagerServerUrl() {
        return userManagerServerUrl;
    }

    public String getUsers() {
        return "/users";
    }

    public String getUserRoles() {
        return "/roles/users";
    }

    public String getUserByName(String username) {
        return getUsers() + "/name/" + username;
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

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

    private String getRoles() {
        return "/roles";
    }

    private String getUserRoles() {
        return "/roles/users";
    }

    private String getGroups() {
        return "/groups";
    }

    public String getAllUsers() {
        return getUsers();
    }

    public String getUserInGroup() {
        return getUsers();
    }

    public String getUserRolesByGroup(String groupName) {
        return getUserRoles() + "/groups/" + groupName;
    }

    public String getUserByGroupAndRole(String groupName, String roleName) {
        return getUserRoles() + "/groups/" + groupName + "/roles/" + roleName;
    }

    public String getUserRolesByUser(String username) {
        return getUserRoles() + "/usernames/" + username;
    }

    public String getGroups(Long id) {
        return getGroups() + "/" + id;
    }

    public String getGroupByName(String groupName) {
        return getGroups() + "/names/" + groupName;
    }

    public String getAllGroups() {
        return getGroups();
    }

    public String getAllGroupsWithoutParent() {
        return getGroups() + "/no-parent";
    }

    public String getAllGroupsWithParent() {
        return getGroups() + "/has-parent";
    }

    public String getUserRoleById(Long id) {
        return getUserRoles() + "/" + id;
    }

    public String getRoleById(Long id) {
        return getRoles() + "/" + id;
    }

    public String getRoleByName(String name) {
        return getRoles() + "/names/" + name;
    }

    public String getUserRolesFromUserGroupAndApplication(String username, String groupName, String applicationName) {
        return getUserRoles() + "/usernames/" + username + "/groups/" + groupName + "/applications/" + applicationName;
    }

    public String addUserRoles() {
        return getUserRoles();
    }
}

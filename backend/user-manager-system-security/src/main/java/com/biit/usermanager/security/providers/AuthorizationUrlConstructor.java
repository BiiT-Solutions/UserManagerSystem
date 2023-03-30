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

    private String getOrganization() {
        return "/organizations";
    }

    public String getAllUsers() {
        return getUsers();
    }

    public String getUserInOrganization() {
        return getUsers();
    }

    public String getUserRolesByOrganization(String organizationName) {
        return getUserRoles() + "/organization/" + organizationName;
    }

    public String getUserByOrganizationAndRole(String organizationName, String roleName) {
        return getUserRoles() + "/organization/" + organizationName + "/role/" + roleName;
    }

    public String getUserRolesByUser(String username) {
        return getUserRoles() + "/username/" + username;
    }

    public String getOrganization(Long id) {
        return getOrganization() + "/" + id;
    }

    public String getOrganizationByName(String organizationName) {
        return getOrganization() + "/name/" + organizationName;
    }

    public String getAllOrganizations() {
        return getOrganization();
    }

    public String getUserRoleById(Long id) {
        return getUserRoles() + "/" + id;
    }

    public String getRoleById(Long id) {
        return getRoles() + "/" + id;
    }

    public String getRoleByName(String name) {
        return getRoles() + "/name/" + name;
    }

    public String getUserRolesFromUserOrganizationAndApplication(String username, String organizationName, String applicationName) {
        return getUserRoles() + "/username/" + username + "/organization/" + organizationName + "/application/" + applicationName;
    }

    public String addUserRoles() {
        return getUserRoles();
    }
}

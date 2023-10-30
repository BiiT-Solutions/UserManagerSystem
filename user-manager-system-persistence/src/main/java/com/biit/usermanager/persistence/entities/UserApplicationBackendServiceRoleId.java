package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserApplicationBackendServiceRoleId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "application_role_application", nullable = false)
    private String applicationName;

    @Column(name = "application_role_role", nullable = false)
    private String roleName;

    @Column(name = "backend_service_role_service", nullable = false)
    private String backendServiceName;

    @Column(name = "backend_service_role_name", nullable = false)
    private String backendServiceRole;

    public UserApplicationBackendServiceRoleId() {
        super();
    }

    public UserApplicationBackendServiceRoleId(Long userId, String applicationName, String roleName, String backendServiceName, String backendServiceRole) {
        this();
        this.userId = userId;
        this.applicationName = applicationName;
        this.roleName = roleName;
        this.backendServiceName = backendServiceName;
        this.backendServiceRole = backendServiceRole;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getBackendServiceName() {
        return backendServiceName;
    }

    public void setBackendServiceName(String backendServiceName) {
        this.backendServiceName = backendServiceName;
    }

    public String getBackendServiceRole() {
        return backendServiceRole;
    }

    public void setBackendServiceRole(String backendServiceRole) {
        this.backendServiceRole = backendServiceRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserApplicationBackendServiceRoleId that = (UserApplicationBackendServiceRoleId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(applicationName, that.applicationName) && Objects.equals(roleName, that.roleName)
                && Objects.equals(backendServiceName, that.backendServiceName) && Objects.equals(backendServiceRole, that.backendServiceRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, applicationName, roleName, backendServiceName, backendServiceRole);
    }

    @Override
    public String toString() {
        return "UserApplicationBackendServiceRoleId{"
                + "userId=" + userId
                + ", applicationName='" + applicationName + '\''
                + ", roleName='" + roleName + '\''
                + ", backendServiceName='" + backendServiceName + '\''
                + ", backendServiceRole='" + backendServiceRole + '\''
                + '}';
    }
}

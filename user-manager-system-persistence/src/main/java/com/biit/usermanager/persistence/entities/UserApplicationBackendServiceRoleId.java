package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserApplicationBackendServiceRoleId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "application_backend_service_role_application", nullable = false)
    private String applicationName;

    @Column(name = "application_backend_service_role_role", nullable = false)
    private String roleName;

    @Column(name = "application_backend_service_role_service", nullable = false)
    private String backendServiceName;

    @Column(name = "application_backend_service_role_name", nullable = false)
    private String backendServiceRole;


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

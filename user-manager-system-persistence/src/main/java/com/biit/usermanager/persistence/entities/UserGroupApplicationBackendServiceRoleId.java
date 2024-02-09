package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserGroupApplicationBackendServiceRoleId implements Serializable {

    protected static final int MAX_UNIQUE_COLUMN_LENGTH = 190;

    @Column(name = "user_group_id", nullable = false)
    private Long userGroupId;

    @Column(name = "application_role_application", nullable = false, length = MAX_UNIQUE_COLUMN_LENGTH)
    private String applicationName;

    @Column(name = "application_role_role", nullable = false, length = MAX_UNIQUE_COLUMN_LENGTH)
    private String roleName;

    @Column(name = "backend_service_role_service", nullable = false, length = MAX_UNIQUE_COLUMN_LENGTH)
    private String backendServiceName;

    @Column(name = "backend_service_role_name", nullable = false, length = MAX_UNIQUE_COLUMN_LENGTH)
    private String backendServiceRole;

    public UserGroupApplicationBackendServiceRoleId() {
        super();
    }

    public UserGroupApplicationBackendServiceRoleId(Long userGroupId, String applicationName, String roleName, String backendServiceName,
                                                    String backendServiceRole) {
        this();
        this.userGroupId = userGroupId;
        this.applicationName = applicationName;
        this.roleName = roleName;
        this.backendServiceName = backendServiceName;
        this.backendServiceRole = backendServiceRole;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userId) {
        this.userGroupId = userId;
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
        final UserGroupApplicationBackendServiceRoleId that = (UserGroupApplicationBackendServiceRoleId) o;
        return Objects.equals(userGroupId, that.userGroupId) && Objects.equals(applicationName, that.applicationName) && Objects.equals(roleName, that.roleName)
                && Objects.equals(backendServiceName, that.backendServiceName) && Objects.equals(backendServiceRole, that.backendServiceRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userGroupId, applicationName, roleName, backendServiceName, backendServiceRole);
    }

    @Override
    public String toString() {
        return "UserGroupApplicationBackendServiceRoleId{"
                + "userGroupId=" + userGroupId
                + ", applicationName='" + applicationName + '\''
                + ", roleName='" + roleName + '\''
                + ", backendServiceName='" + backendServiceName + '\''
                + ", backendServiceRole='" + backendServiceRole + '\''
                + '}';
    }
}

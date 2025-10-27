package com.biit.usermanager.persistence.entities;

/*-
 * #%L
 * User Manager System (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserApplicationBackendServiceRoleId implements Serializable {

    @Serial
    private static final long serialVersionUID = -1315986192532624395L;

    protected static final int MAX_UNIQUE_COLUMN_LENGTH = 190;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "application_role_application", nullable = false, length = MAX_UNIQUE_COLUMN_LENGTH)
    private String applicationName;

    @Column(name = "application_role_role", nullable = false, length = MAX_UNIQUE_COLUMN_LENGTH)
    private String roleName;

    @Column(name = "backend_service_role_service", nullable = false, length = MAX_UNIQUE_COLUMN_LENGTH)
    private String backendServiceName;

    @Column(name = "backend_service_role_name", nullable = false, length = MAX_UNIQUE_COLUMN_LENGTH)
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

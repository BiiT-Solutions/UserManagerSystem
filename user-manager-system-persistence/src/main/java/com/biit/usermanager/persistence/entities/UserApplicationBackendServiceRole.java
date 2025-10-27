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

import com.biit.server.persistence.entities.StorableObject;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "users_by_application_backend_service_roles", indexes = {
        @Index(name = "ind_user", columnList = "user_id"),
})
public class UserApplicationBackendServiceRole extends StorableObject {

    @EmbeddedId
    private UserApplicationBackendServiceRoleId id;

    public UserApplicationBackendServiceRole() {
        super();
    }

    public UserApplicationBackendServiceRole(Long userId, ApplicationRole applicationRole, BackendServiceRole backendServiceRole) {
        setId(new UserApplicationBackendServiceRoleId(userId, applicationRole.getId().getApplication().getName(), applicationRole.getId().getRole().getName(),
                backendServiceRole.getId().getBackendService().getName(), backendServiceRole.getName()));
    }

    public UserApplicationBackendServiceRole(Long userId, String applicationName, String roleName, String backendServiceName, String backendServiceRole) {
        this();
        setId(new UserApplicationBackendServiceRoleId(userId, applicationName, roleName, backendServiceName, backendServiceRole));
    }

    public UserApplicationBackendServiceRoleId getId() {
        return id;
    }

    public void setId(UserApplicationBackendServiceRoleId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserApplicationBackendServiceRole that = (UserApplicationBackendServiceRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserApplicationBackendServiceRole{"
                + "id=" + id
                + '}';
    }
}

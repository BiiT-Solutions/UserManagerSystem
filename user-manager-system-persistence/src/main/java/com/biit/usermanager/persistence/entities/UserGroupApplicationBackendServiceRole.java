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
@Table(name = "user_groups_by_application_backend_service_roles", indexes = {
        @Index(name = "ind_usergroup", columnList = "user_group_id"),
})
public class UserGroupApplicationBackendServiceRole extends StorableObject {

    @EmbeddedId
    private UserGroupApplicationBackendServiceRoleId id;

    public UserGroupApplicationBackendServiceRole() {
        super();
    }

    public UserGroupApplicationBackendServiceRole(Long userGroupId, String applicationName, String roleName, String backendServiceName,
                                                  String backendServiceRole) {
        this();
        setId(new UserGroupApplicationBackendServiceRoleId(userGroupId, applicationName, roleName, backendServiceName, backendServiceRole));
    }

    public UserGroupApplicationBackendServiceRoleId getId() {
        return id;
    }

    public void setId(UserGroupApplicationBackendServiceRoleId id) {
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
        final UserGroupApplicationBackendServiceRole that = (UserGroupApplicationBackendServiceRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserGroupApplicationBackendServiceRole{"
                + "id=" + id
                + '}';
    }
}

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

import com.biit.server.persistence.entities.CreatedElement;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "application_backend_service_roles")
public class ApplicationBackendServiceRole extends CreatedElement {

    @EmbeddedId
    private ApplicationBackendServiceRoleId id;

    public ApplicationBackendServiceRole() {
        super();
    }

    public ApplicationBackendServiceRole(ApplicationRole applicationRole, BackendServiceRole backendServiceRole) {
        this();
        setId(new ApplicationBackendServiceRoleId(applicationRole, backendServiceRole));
    }

    public ApplicationBackendServiceRoleId getId() {
        return id;
    }

    public void setId(ApplicationBackendServiceRoleId id) {
        this.id = id;
    }

    @Override
    public void setCreatedOn(String createdOn) {
        //Do nothing, as application backend service roles are not linked to organization.
    }

    @Override
    public String toString() {
        return "ApplicationBackendServiceRole{"
                + "id=" + id
                + '}';
    }
}

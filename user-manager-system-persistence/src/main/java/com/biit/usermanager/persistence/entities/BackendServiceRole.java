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
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "backend_service_roles")
public class BackendServiceRole extends CreatedElement implements Serializable {

    @Serial
    private static final long serialVersionUID = 2293890542632212445L;

    @EmbeddedId
    private BackendServiceRoleId id;

    public BackendServiceRole() {
        super();
    }

    public BackendServiceRole(BackendService backendService, String name) {
        super();
        setId(new BackendServiceRoleId(backendService, name));
    }

    public BackendServiceRoleId getId() {
        return id;
    }

    public void setId(BackendServiceRoleId id) {
        this.id = id;
    }

    @Override
    public void setCreatedOn(String createdOn) {
        //Do nothing, as backend service roles are not linked to organization.
    }

    @JsonIgnore
    public String getName() {
        if (id == null) {
            return null;
        }
        return id.getName();
    }

    @JsonIgnore
    public BackendService getBackendService() {
        if (id == null) {
            return null;
        }
        return id.getBackendService();
    }

    @Override
    public String toString() {
        return "BackendServiceRole{"
                + "id=" + id
                + '}';
    }
}

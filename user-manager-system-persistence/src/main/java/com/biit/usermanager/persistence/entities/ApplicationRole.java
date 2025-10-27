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

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "application_roles")
public class ApplicationRole extends CreatedElement implements Serializable {

    @Serial
    private static final long serialVersionUID = -2291890542632281242L;

    @EmbeddedId
    private ApplicationRoleId id;

    public ApplicationRole() {
        super();
    }

    public ApplicationRole(Application application, Role role) {
        setId(new ApplicationRoleId(application, role));
    }

    public ApplicationRoleId getId() {
        return id;
    }

    public void setId(ApplicationRoleId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ApplicationRole{"
                + "id=" + id
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationRole that = (ApplicationRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public void setCreatedOn(String createdOn) {
        //Do nothing, as application roles are not linked to organization.
    }
}

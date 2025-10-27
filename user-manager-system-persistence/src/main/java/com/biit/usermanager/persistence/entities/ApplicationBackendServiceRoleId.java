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

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ApplicationBackendServiceRoleId implements Serializable {

    @ManyToOne
    @Fetch(value = FetchMode.JOIN)
    @JoinColumns({
            @JoinColumn(name = "application_role_application", referencedColumnName = "application_name"),
            @JoinColumn(name = "application_role_role", referencedColumnName = "application_role")
    })
    private ApplicationRole applicationRole;


    @ManyToOne
    @Fetch(value = FetchMode.JOIN)
    @JoinColumns({
            @JoinColumn(name = "backend_service_role_service", referencedColumnName = "backend_service"),
            @JoinColumn(name = "backend_service_role_name", referencedColumnName = "name")
    })
    private BackendServiceRole backendServiceRole;

    public ApplicationBackendServiceRoleId() {
        super();
    }

    public ApplicationBackendServiceRoleId(ApplicationRole applicationRole, BackendServiceRole backendServiceRole) {
        this();
        setApplicationRole(applicationRole);
        setBackendServiceRole(backendServiceRole);
    }

    public ApplicationRole getApplicationRole() {
        return applicationRole;
    }

    public void setApplicationRole(ApplicationRole applicationRole) {
        this.applicationRole = applicationRole;
    }

    public BackendServiceRole getBackendServiceRole() {
        return backendServiceRole;
    }

    public void setBackendServiceRole(BackendServiceRole backendServiceRole) {
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
        final ApplicationBackendServiceRoleId that = (ApplicationBackendServiceRoleId) o;
        return Objects.equals(applicationRole, that.applicationRole) && Objects.equals(backendServiceRole, that.backendServiceRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationRole, backendServiceRole);
    }

    @Override
    public String toString() {
        return "ApplicationBackendServiceRoleId{"
                + "applicationRole=" + applicationRole
                + ", backendServiceRole=" + backendServiceRole
                + '}';
    }
}

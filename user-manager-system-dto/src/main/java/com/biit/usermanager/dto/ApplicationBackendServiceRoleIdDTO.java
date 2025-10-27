package com.biit.usermanager.dto;

/*-
 * #%L
 * User Manager System (DTO)
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class ApplicationBackendServiceRoleIdDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -820077018138711651L;

    @Valid
    @NotNull
    private ApplicationRoleDTO applicationRole;

    @Valid
    @NotNull
    private BackendServiceRoleDTO serviceRole;

    public ApplicationBackendServiceRoleIdDTO() {
        super();
    }

    public ApplicationBackendServiceRoleIdDTO(ApplicationRoleDTO applicationRole, BackendServiceRoleDTO serviceRole) {
        super();
        setApplicationRole(applicationRole);
        setBackendServiceRole(serviceRole);
    }

    public ApplicationRoleDTO getApplicationRole() {
        return applicationRole;
    }

    public void setApplicationRole(ApplicationRoleDTO applicationRole) {
        this.applicationRole = applicationRole;
    }

    public BackendServiceRoleDTO getBackendServiceRole() {
        return serviceRole;
    }

    public void setBackendServiceRole(BackendServiceRoleDTO serviceRole) {
        this.serviceRole = serviceRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationBackendServiceRoleIdDTO that = (ApplicationBackendServiceRoleIdDTO) o;
        return Objects.equals(applicationRole, that.applicationRole) && Objects.equals(serviceRole, that.serviceRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationRole, serviceRole);
    }

    @Override
    public String toString() {
        return "ApplicationServiceRoleIdDTO{"
                + "applicationRole=" + applicationRole
                + ", serviceRole=" + serviceRole
                + '}';
    }
}

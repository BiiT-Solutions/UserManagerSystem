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

import java.util.Objects;

public class ApplicationRoleIdDTO {

    @Valid
    @NotNull
    private ApplicationDTO application;

    @Valid
    @NotNull
    private RoleDTO role;

    public ApplicationRoleIdDTO() {
        super();
    }

    public ApplicationRoleIdDTO(ApplicationDTO application, RoleDTO role) {
        super();
        setApplication(application);
        setRole(role);
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationRoleIdDTO that = (ApplicationRoleIdDTO) o;
        return Objects.equals(application, that.application) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(application, role);
    }

    @Override
    public String toString() {
        return "ApplicationRoleIdDTO{"
                + "application=" + application
                + ", role=" + role
                + '}';
    }
}

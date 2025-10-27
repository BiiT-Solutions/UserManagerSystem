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

import com.biit.server.controllers.models.ElementDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class BackendServiceRoleDTO extends ElementDTO<BackendServiceRoleIdDTO> {

    @Valid
    @NotNull
    private BackendServiceRoleIdDTO id;

    public BackendServiceRoleDTO() {
        super();
    }

    public BackendServiceRoleDTO(BackendServiceDTO service, String name) {
        this();
        setId(new BackendServiceRoleIdDTO(service, name));
    }

    @Override
    public BackendServiceRoleIdDTO getId() {
        return id;
    }

    @Override
    public void setId(BackendServiceRoleIdDTO id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BackendServiceRoleDTO{"
                + "name=" + id
                + '}';
    }
}

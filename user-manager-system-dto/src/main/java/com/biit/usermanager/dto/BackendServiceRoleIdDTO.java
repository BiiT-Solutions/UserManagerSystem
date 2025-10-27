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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class BackendServiceRoleIdDTO {

    @NotNull
    private BackendServiceDTO backendService;

    @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
    @NotBlank
    private String name;

    public BackendServiceRoleIdDTO() {
        super();
    }

    public BackendServiceRoleIdDTO(BackendServiceDTO backendService, String name) {
        super();
        setBackendService(backendService);
        setName(name);
    }

    public BackendServiceDTO getBackendService() {
        return backendService;
    }

    public void setBackendService(BackendServiceDTO backendService) {
        this.backendService = backendService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BackendServiceRoleIdDTO that = (BackendServiceRoleIdDTO) o;
        return Objects.equals(backendService, that.backendService) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backendService, name);
    }

    @Override
    public String toString() {
        return "BackendServiceRoleIdDTO{"
                + "service=" + backendService
                + ", name='" + name + '\''
                + '}';
    }
}

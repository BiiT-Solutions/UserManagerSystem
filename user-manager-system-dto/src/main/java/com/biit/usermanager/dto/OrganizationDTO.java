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
import com.biit.server.security.model.IUserOrganization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;

public class OrganizationDTO extends ElementDTO<String> implements IUserOrganization {

    @Serial
    private static final long serialVersionUID = -5404731170811261650L;

    @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
    @NotBlank
    private String name = "";

    @Size(max = ElementDTO.MAX_BIG_FIELD_LENGTH)
    private String description = "";

    public OrganizationDTO() {
    }

    public OrganizationDTO(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
        this.name = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OrganizationDTO{"
                + "name='" + name + '\''
                + '}';
    }
}

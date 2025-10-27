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
import com.biit.usermanager.entity.IRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;

public class RoleDTO extends ElementDTO<String> implements IRole<String> {

    @Serial
    private static final long serialVersionUID = -1038904769010325039L;

    @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
    @NotBlank(message = "Name is mandatory.")
    private String name;

    @Size(max = ElementDTO.MAX_BIG_FIELD_LENGTH)
    private String description = "";

    public RoleDTO() {
        super();
    }

    public RoleDTO(String name, String description) {
        this();
        setId(name);
        setDescription(description);
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
        this.name = id;
    }

    @JsonIgnore
    public String getName() {
        return getId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getUniqueName() {
        return getName();
    }

    @Override
    public String getUniqueId() {
        return getId();
    }

    @Override
    public String toString() {
        return "RoleDTO{"
                + "name='" + getName() + '\''
                + "}";
    }
}

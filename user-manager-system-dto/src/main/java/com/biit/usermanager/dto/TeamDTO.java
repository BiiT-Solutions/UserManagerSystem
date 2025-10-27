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
import com.biit.server.security.model.IUserTeam;
import com.biit.usermanager.entity.IGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TeamDTO extends ElementDTO<Long> implements IGroup<Long>, Comparable<TeamDTO>, IUserTeam {

    private Long id;

    @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
    @NotBlank(message = "Name is mandatory.")
    private String name = "";

    @Size(max = ElementDTO.MAX_BIG_FIELD_LENGTH)
    private String description = "";

    private Long parentId;

    @NotNull
    private OrganizationDTO organization;

    public TeamDTO() {
        super();
    }

    public TeamDTO(String name, OrganizationDTO organization) {
        this();
        setName(name);
        setOrganization(organization);
    }

    public TeamDTO(String name, Long parentId, OrganizationDTO organization) {
        this();
        setName(name);
        setOrganization(organization);
        setParentId(parentId);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

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
    public String getUniqueName() {
        return name;
    }

    @Override
    public Long getUniqueId() {
        return getId();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
        setCreatedOn(organization.getName());
    }

    @Override
    public String toString() {
        return "TeamDTO{"
                + "name='" + name + '\''
                + "}";
    }

    @Override
    public int compareTo(TeamDTO otherTeam) {
        if (otherTeam == null) {
            return 1;
        }
        if (this.name != null) {
            return this.name.compareTo(otherTeam.getName());
        }
        if (this.id != null) {
            return this.id.compareTo(otherTeam.getId());
        }
        return 0;
    }
}

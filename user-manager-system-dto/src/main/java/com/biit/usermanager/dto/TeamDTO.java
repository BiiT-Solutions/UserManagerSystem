package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IGroup;

public class TeamDTO extends ElementDTO<Long> implements IGroup<Long>, Comparable<TeamDTO> {

    private Long id;

    private String name = "";

    private String description = "";

    private Long parentId;

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

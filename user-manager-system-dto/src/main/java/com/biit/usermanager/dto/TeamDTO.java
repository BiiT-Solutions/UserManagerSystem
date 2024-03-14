package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IGroup;

public class TeamDTO extends ElementDTO<Long> implements IGroup<Long> {

    private Long id;

    private String name = "";

    private String description = "";

    private TeamDTO parent;

    private OrganizationDTO organization;

    public TeamDTO() {
        super();
    }

    public TeamDTO(String name, OrganizationDTO organization) {
        this();
        setName(name);
        setOrganization(organization);
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

    public TeamDTO getParent() {
        return parent;
    }

    public void setParent(TeamDTO parent) {
        this.parent = parent;
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
}

package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IGroup;

import java.util.Set;

public class OrganizationDTO extends ElementDTO implements IGroup<Long> {

    private String name = "";

    private Set<OrganizationDTO> subOrganizations;

    private OrganizationDTO parent;

    public OrganizationDTO() {
        super();
    }

    public OrganizationDTO(String name) {
        this();
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUniqueName() {
        return name;
    }

    @Override
    public Long getUniqueId() {
        return getId();
    }

    public Set<OrganizationDTO> getSubOrganizations() {
        return subOrganizations;
    }

    public void setSubOrganizations(Set<OrganizationDTO> subOrganizations) {
        this.subOrganizations = subOrganizations;
    }

    public OrganizationDTO getParent() {
        return parent;
    }

    public void setParent(OrganizationDTO parent) {
        this.parent = parent;
    }
}

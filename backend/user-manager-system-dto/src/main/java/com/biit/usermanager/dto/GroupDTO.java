package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IGroup;

import java.util.Set;

public class GroupDTO extends ElementDTO implements IGroup<Long> {

    private String name = "";

    private Set<GroupDTO> subGroups;

    private GroupDTO parent;

    public GroupDTO() {
        super();
    }

    public GroupDTO(String name) {
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

    public Set<GroupDTO> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(Set<GroupDTO> subGroups) {
        this.subGroups = subGroups;
    }

    public GroupDTO getParent() {
        return parent;
    }

    public void setParent(GroupDTO parent) {
        this.parent = parent;
    }
}

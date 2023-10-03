package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IGroup;

import java.util.Set;

public class GroupDTO extends ElementDTO implements IGroup<Long> {

    private String name = "";

    private Set<GroupDTO> subGroups;

    private GroupDTO parent;

    private ApplicationDTO application;

    public GroupDTO() {
        super();
    }

    public GroupDTO(String name, ApplicationDTO application) {
        this();
        setName(name);
        setApplication(application);
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

    public ApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return "GroupDTO{"
                + "name='" + name + '\''
                + "}";
    }
}

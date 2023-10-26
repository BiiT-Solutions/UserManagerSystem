package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IGroup;

public class GroupDTO extends ElementDTO<Long> implements IGroup<Long> {

    private Long id;

    private String name = "";

    private String description = "";

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

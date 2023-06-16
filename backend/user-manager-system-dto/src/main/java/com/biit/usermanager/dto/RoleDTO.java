package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IRole;

public class RoleDTO extends ElementDTO implements IRole<Long> {
    private String name = "";
    private String description = "";

    public RoleDTO() {
        super();
    }

    public RoleDTO(String name, String description) {
        this();
        setName(name);
        setDescription(description);
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
        return getName();
    }

    @Override
    public Long getUniqueId() {
        return getId();
    }

    @Override
    public String toString() {
        return "RoleDTO{"
                + "name='" + name + '\''
                + "}";
    }
}

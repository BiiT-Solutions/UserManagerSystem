package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.usermanager.entity.IRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serial;

public class RoleDTO extends ElementDTO<String> implements IRole<String> {

    @Serial
    private static final long serialVersionUID = -1038904769010325039L;

    private String name;

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

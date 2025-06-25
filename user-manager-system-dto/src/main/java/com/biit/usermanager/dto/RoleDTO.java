package com.biit.usermanager.dto;

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

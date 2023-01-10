package com.biit.usermanager.core.controller.models;

import com.biit.server.controllers.models.ElementDTO;

public class RoleDTO extends ElementDTO {
    private String name = "";
    private String description = "";

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
}

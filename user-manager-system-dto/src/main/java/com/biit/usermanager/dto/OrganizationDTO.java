package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;

public class OrganizationDTO extends ElementDTO<String> {

    private String name = "";

    private String description = "";

    public OrganizationDTO() {
    }

    public OrganizationDTO(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
        this.name = id;
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
    public String toString() {
        return "OrganizationDTO{"
                + "name='" + name + '\''
                + '}';
    }
}

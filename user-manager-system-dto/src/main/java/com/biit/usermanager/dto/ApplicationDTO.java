package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApplicationDTO extends ElementDTO<String> {

    private String name;
    private String description = "";

    public ApplicationDTO() {
        super();
    }

    public ApplicationDTO(String name) {
        this();
        setId(name);
    }

    public ApplicationDTO(String name, String description) {
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
    public String toString() {
        return "ApplicationDTO{"
                + "name='" + getName() + '\''
                + "}";
    }
}

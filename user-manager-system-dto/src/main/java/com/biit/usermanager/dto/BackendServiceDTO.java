package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BackendServiceDTO extends ElementDTO<String> {

    private String name;

    private String description = "";

    public BackendServiceDTO() {
        super();
    }

    public BackendServiceDTO(String name) {
        this();
        setId(name);
    }

    @JsonIgnore
    @Override
    public String getId() {
        return name;
    }

    @JsonIgnore
    @Override
    public void setId(String id) {
        this.name = id;
    }

    public String getName() {
        return getId();
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
        return "ServiceDTO{"
                + "name='" + getId() + '\''
                + '}';
    }
}

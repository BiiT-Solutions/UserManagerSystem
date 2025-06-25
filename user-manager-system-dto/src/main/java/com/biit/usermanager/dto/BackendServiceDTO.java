package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BackendServiceDTO extends ElementDTO<String> {

    @Size(max = ElementDTO.MAX_NORMAL_FIELD_LENGTH, min = ElementDTO.MIN_FIELD_LENGTH)
    @NotNull
    private String name;

    @Size(max = ElementDTO.MAX_BIG_FIELD_LENGTH)
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

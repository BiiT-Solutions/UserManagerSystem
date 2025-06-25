package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class BackendServiceRoleDTO extends ElementDTO<BackendServiceRoleIdDTO> {

    @Valid
    @NotNull
    private BackendServiceRoleIdDTO id;

    public BackendServiceRoleDTO() {
        super();
    }

    public BackendServiceRoleDTO(BackendServiceDTO service, String name) {
        this();
        setId(new BackendServiceRoleIdDTO(service, name));
    }

    @Override
    public BackendServiceRoleIdDTO getId() {
        return id;
    }

    @Override
    public void setId(BackendServiceRoleIdDTO id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BackendServiceRoleDTO{"
                + "name=" + id
                + '}';
    }
}

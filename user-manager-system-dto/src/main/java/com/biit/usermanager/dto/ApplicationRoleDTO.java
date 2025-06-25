package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class ApplicationRoleDTO extends ElementDTO<ApplicationRoleIdDTO> {

    @Valid
    @NotNull
    private ApplicationRoleIdDTO id;

    public ApplicationRoleDTO() {
        super();
    }

    public ApplicationRoleDTO(ApplicationDTO application, RoleDTO role) {
        this();
        setId(new ApplicationRoleIdDTO(application, role));
    }

    @Override
    public ApplicationRoleIdDTO getId() {
        return id;
    }

    @Override
    public void setId(ApplicationRoleIdDTO id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ApplicationRoleDTO{"
                + "id=" + id
                + '}';
    }
}

package com.biit.usermanager.dto;

import com.biit.server.controllers.models.CreatedElementDTO;

public class ApplicationRoleDTO extends CreatedElementDTO {

    private ApplicationRoleIdDTO id;

    public ApplicationRoleIdDTO getId() {
        return id;
    }

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

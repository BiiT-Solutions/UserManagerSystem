package com.biit.usermanager.dto;

import com.biit.server.controllers.models.CreatedElementDTO;

public class ApplicationServiceRoleDTO extends CreatedElementDTO {

    private ApplicationServiceRoleIdDTO id;

    public ApplicationServiceRoleIdDTO getId() {
        return id;
    }

    public void setId(ApplicationServiceRoleIdDTO id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ApplicationServiceRoleDTO{"
                + "id=" + id
                + '}';
    }
}

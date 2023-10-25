package com.biit.usermanager.dto;

import com.biit.server.controllers.models.CreatedElementDTO;

public class BackendServiceRoleDTO extends CreatedElementDTO {

    private BackendServiceRoleIdDTO id;

    public BackendServiceRoleIdDTO getId() {
        return id;
    }

    public void setId(BackendServiceRoleIdDTO id) {
        this.id = id;
    }
}

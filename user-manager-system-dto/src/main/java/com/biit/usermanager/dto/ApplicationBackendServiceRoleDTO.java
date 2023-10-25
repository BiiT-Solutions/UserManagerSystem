package com.biit.usermanager.dto;

import com.biit.server.controllers.models.CreatedElementDTO;

public class ApplicationBackendServiceRoleDTO extends CreatedElementDTO {

    private ApplicationBackendServiceRoleIdDTO id;

    public ApplicationBackendServiceRoleDTO() {
        super();
    }

    public ApplicationBackendServiceRoleDTO(ApplicationRoleDTO applicationRole, BackendServiceRoleDTO serviceRole) {
        this();
        setId(new ApplicationBackendServiceRoleIdDTO(applicationRole, serviceRole));
    }

    public ApplicationBackendServiceRoleIdDTO getId() {
        return id;
    }

    public void setId(ApplicationBackendServiceRoleIdDTO id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ApplicationServiceRoleDTO{"
                + "id=" + id
                + '}';
    }
}

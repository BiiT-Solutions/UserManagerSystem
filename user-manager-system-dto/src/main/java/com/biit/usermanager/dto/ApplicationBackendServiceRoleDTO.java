package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;

public class ApplicationBackendServiceRoleDTO extends ElementDTO<ApplicationBackendServiceRoleIdDTO> {

    private ApplicationBackendServiceRoleIdDTO id;

    public ApplicationBackendServiceRoleDTO() {
        super();
    }

    public ApplicationBackendServiceRoleDTO(ApplicationRoleDTO applicationRole, BackendServiceRoleDTO serviceRole) {
        this();
        setId(new ApplicationBackendServiceRoleIdDTO(applicationRole, serviceRole));
    }

    @Override
    public ApplicationBackendServiceRoleIdDTO getId() {
        return id;
    }

    @Override
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

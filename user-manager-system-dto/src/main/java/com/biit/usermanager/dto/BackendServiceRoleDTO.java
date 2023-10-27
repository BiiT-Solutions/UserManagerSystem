package com.biit.usermanager.dto;

import com.biit.server.controllers.models.CreatedElementDTO;

public class BackendServiceRoleDTO extends CreatedElementDTO {

    private BackendServiceRoleIdDTO id;

    public BackendServiceRoleDTO() {
        super();
    }

    public BackendServiceRoleDTO(BackendServiceDTO service, String name) {
        this();
        setId(new BackendServiceRoleIdDTO(service, name));
    }


    public BackendServiceRoleIdDTO getId() {
        return id;
    }

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

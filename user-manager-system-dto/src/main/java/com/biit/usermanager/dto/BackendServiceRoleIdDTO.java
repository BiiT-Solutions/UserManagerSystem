package com.biit.usermanager.dto;

import com.biit.server.controllers.models.CreatedElementDTO;

public class BackendServiceRoleIdDTO extends CreatedElementDTO {

    private BackendServiceDTO backendService;

    private String name;

    public BackendServiceRoleIdDTO() {
        super();
    }

    public BackendServiceRoleIdDTO(BackendServiceDTO backendService, String name) {
        super();
        setBackendService(backendService);
        setName(name);
    }

    public BackendServiceDTO getBackendService() {
        return backendService;
    }

    public void setBackendService(BackendServiceDTO backendService) {
        this.backendService = backendService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BackendServiceRoleIdDTO{"
                + "service=" + backendService
                + ", name='" + name + '\''
                + '}';
    }
}

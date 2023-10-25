package com.biit.usermanager.dto;

import com.biit.server.controllers.models.CreatedElementDTO;

public class BackendServiceRoleIdDTO extends CreatedElementDTO {

    private BackendServiceDTO service;

    private String name;

    public BackendServiceRoleIdDTO() {
        super();
    }

    public BackendServiceRoleIdDTO(BackendServiceDTO service, String name) {
        super();
        setService(service);
        setName(name);
    }

    public BackendServiceDTO getService() {
        return service;
    }

    public void setService(BackendServiceDTO service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServiceRoleIdDTO{" +
                "service=" + service +
                ", name='" + name + '\'' +
                '}';
    }
}

package com.biit.usermanager.dto;

import com.biit.server.controllers.models.CreatedElementDTO;

public class ServiceRoleIdDTO extends CreatedElementDTO {

    private ServiceDTO service;

    private String name;

    public ServiceRoleIdDTO() {
        super();
    }

    public ServiceRoleIdDTO(ServiceDTO service, String name) {
        super();
        setService(service);
        setName(name);
    }

    public ServiceDTO getService() {
        return service;
    }

    public void setService(ServiceDTO service) {
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

package com.biit.usermanager.dto;

import java.util.Objects;

public class BackendServiceRoleIdDTO {

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BackendServiceRoleIdDTO that = (BackendServiceRoleIdDTO) o;
        return Objects.equals(backendService, that.backendService) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backendService, name);
    }

    @Override
    public String toString() {
        return "BackendServiceRoleIdDTO{"
                + "service=" + backendService
                + ", name='" + name + '\''
                + '}';
    }
}

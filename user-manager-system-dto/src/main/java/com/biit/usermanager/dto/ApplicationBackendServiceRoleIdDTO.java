package com.biit.usermanager.dto;

import java.util.Objects;

public class ApplicationBackendServiceRoleIdDTO {

    private ApplicationRoleDTO applicationRole;

    private BackendServiceRoleDTO serviceRole;

    public ApplicationBackendServiceRoleIdDTO() {
        super();
    }

    public ApplicationBackendServiceRoleIdDTO(ApplicationRoleDTO applicationRole, BackendServiceRoleDTO serviceRole) {
        super();
        setApplicationRole(applicationRole);
        setBackendServiceRole(serviceRole);
    }

    public ApplicationRoleDTO getApplicationRole() {
        return applicationRole;
    }

    public void setApplicationRole(ApplicationRoleDTO applicationRole) {
        this.applicationRole = applicationRole;
    }

    public BackendServiceRoleDTO getBackendServiceRole() {
        return serviceRole;
    }

    public void setBackendServiceRole(BackendServiceRoleDTO serviceRole) {
        this.serviceRole = serviceRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationBackendServiceRoleIdDTO that = (ApplicationBackendServiceRoleIdDTO) o;
        return Objects.equals(applicationRole, that.applicationRole) && Objects.equals(serviceRole, that.serviceRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationRole, serviceRole);
    }

    @Override
    public String toString() {
        return "ApplicationServiceRoleIdDTO{"
                + "applicationRole=" + applicationRole
                + ", serviceRole=" + serviceRole
                + '}';
    }
}

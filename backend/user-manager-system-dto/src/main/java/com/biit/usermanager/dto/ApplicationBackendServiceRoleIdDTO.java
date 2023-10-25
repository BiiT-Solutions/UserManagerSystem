package com.biit.usermanager.dto;

public class ApplicationBackendServiceRoleIdDTO {

    private ApplicationRoleDTO applicationRole;

    private BackendServiceRoleDTO serviceRole;

    public ApplicationBackendServiceRoleIdDTO() {
        super();
    }

    public ApplicationBackendServiceRoleIdDTO(ApplicationRoleDTO applicationRole, BackendServiceRoleDTO serviceRole) {
        super();
        setApplicationRole(applicationRole);
        setServiceRole(serviceRole);
    }

    public ApplicationRoleDTO getApplicationRole() {
        return applicationRole;
    }

    public void setApplicationRole(ApplicationRoleDTO applicationRole) {
        this.applicationRole = applicationRole;
    }

    public BackendServiceRoleDTO getServiceRole() {
        return serviceRole;
    }

    public void setServiceRole(BackendServiceRoleDTO serviceRole) {
        this.serviceRole = serviceRole;
    }

    @Override
    public String toString() {
        return "ApplicationServiceRoleIdDTO{"
                + "applicationRole=" + applicationRole
                + ", serviceRole=" + serviceRole
                + '}';
    }
}

package com.biit.usermanager.dto;

public class ApplicationServiceRoleIdDTO {

    private ApplicationRoleDTO applicationRole;

    private ServiceRoleDTO serviceRole;

    public ApplicationServiceRoleIdDTO() {
        super();
    }

    public ApplicationServiceRoleIdDTO(ApplicationRoleDTO applicationRole, ServiceRoleDTO serviceRole) {
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

    public ServiceRoleDTO getServiceRole() {
        return serviceRole;
    }

    public void setServiceRole(ServiceRoleDTO serviceRole) {
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

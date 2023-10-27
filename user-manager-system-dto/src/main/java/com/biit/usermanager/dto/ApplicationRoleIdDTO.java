package com.biit.usermanager.dto;

public class ApplicationRoleIdDTO {

    private ApplicationDTO application;

    private RoleDTO role;

    public ApplicationRoleIdDTO() {
        super();
    }

    public ApplicationRoleIdDTO(ApplicationDTO application, RoleDTO role) {
        super();
        setApplication(application);
        setRole(role);
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ApplicationRoleIdDTO{"
                + "application=" + application
                + ", role=" + role
                + '}';
    }
}

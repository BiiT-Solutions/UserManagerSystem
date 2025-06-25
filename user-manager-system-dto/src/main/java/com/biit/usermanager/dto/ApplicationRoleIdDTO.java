package com.biit.usermanager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class ApplicationRoleIdDTO {

    @Valid
    @NotNull
    private ApplicationDTO application;

    @Valid
    @NotNull
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationRoleIdDTO that = (ApplicationRoleIdDTO) o;
        return Objects.equals(application, that.application) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(application, role);
    }

    @Override
    public String toString() {
        return "ApplicationRoleIdDTO{"
                + "application=" + application
                + ", role=" + role
                + '}';
    }
}

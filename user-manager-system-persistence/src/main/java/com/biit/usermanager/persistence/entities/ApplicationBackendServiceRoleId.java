package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ApplicationBackendServiceRoleId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "application_role_application", referencedColumnName = "application"),
            @JoinColumn(name = "application_role_role", referencedColumnName = "role")
    })
    private ApplicationRole applicationRole;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "backend_service_role_service", referencedColumnName = "backend_service"),
            @JoinColumn(name = "backend_service_role_name", referencedColumnName = "name")
    })
    private BackendServiceRole backendServiceRole;

    public ApplicationBackendServiceRoleId() {
        super();
    }

    public ApplicationBackendServiceRoleId(ApplicationRole applicationRole, BackendServiceRole backendServiceRole) {
        this();
        setApplicationRole(applicationRole);
        setBackendServiceRole(backendServiceRole);
    }

    public ApplicationRole getApplicationRole() {
        return applicationRole;
    }

    public void setApplicationRole(ApplicationRole applicationRole) {
        this.applicationRole = applicationRole;
    }

    public BackendServiceRole getBackendServiceRole() {
        return backendServiceRole;
    }

    public void setBackendServiceRole(BackendServiceRole backendServiceRole) {
        this.backendServiceRole = backendServiceRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationBackendServiceRoleId that = (ApplicationBackendServiceRoleId) o;
        return Objects.equals(applicationRole, that.applicationRole) && Objects.equals(backendServiceRole, that.backendServiceRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationRole, backendServiceRole);
    }

    @Override
    public String toString() {
        return "ApplicationBackendServiceRoleId{"
                + "applicationRole=" + applicationRole
                + ", backendServiceRole=" + backendServiceRole
                + '}';
    }
}

package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
public class ApplicationBackendServiceRoleId implements Serializable {

    @Serial
    private static final long serialVersionUID = 7873629144113150212L;

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
        setServiceRole(backendServiceRole);
    }

    public ApplicationRole getApplicationRole() {
        return applicationRole;
    }

    public void setApplicationRole(ApplicationRole applicationRole) {
        this.applicationRole = applicationRole;
    }

    public BackendServiceRole getServiceRole() {
        return backendServiceRole;
    }

    public void setServiceRole(BackendServiceRole backendServiceRole) {
        this.backendServiceRole = backendServiceRole;
    }
}

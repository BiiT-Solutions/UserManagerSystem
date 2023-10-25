package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
public class ApplicationServiceRoleId implements Serializable {

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
            @JoinColumn(name = "service_role_service", referencedColumnName = "service"),
            @JoinColumn(name = "service_role_name", referencedColumnName = "name")
    })
    private ServiceRole serviceRole;

    public ApplicationServiceRoleId() {
        super();
    }

    public ApplicationServiceRoleId(ApplicationRole applicationRole, ServiceRole serviceRole) {
        this();
        setApplicationRole(applicationRole);
        setServiceRole(serviceRole);
    }

    public ApplicationRole getApplicationRole() {
        return applicationRole;
    }

    public void setApplicationRole(ApplicationRole applicationRole) {
        this.applicationRole = applicationRole;
    }

    public ServiceRole getServiceRole() {
        return serviceRole;
    }

    public void setServiceRole(ServiceRole serviceRole) {
        this.serviceRole = serviceRole;
    }
}

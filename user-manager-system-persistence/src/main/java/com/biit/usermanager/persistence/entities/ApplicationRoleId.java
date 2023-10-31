package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
public class ApplicationRoleId implements Serializable {
    @Serial
    private static final long serialVersionUID = -2291690526732281642L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "application")
    private Application application;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "role")
    private Role role;

    public ApplicationRoleId() {
        super();
    }

    public ApplicationRoleId(Application application, Role role) {
        setApplication(application);
        setRole(role);
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

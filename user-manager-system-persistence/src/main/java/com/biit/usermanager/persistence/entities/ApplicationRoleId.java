package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ApplicationRoleId implements Serializable {
    @Serial
    private static final long serialVersionUID = -2291690526732281642L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "application_name")
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "application_role")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationRoleId that = (ApplicationRoleId) o;
        return Objects.equals(application, that.application) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(application, role);
    }

    @Override
    public String toString() {
        return "ApplicationRoleId{"
                + "application=" + application
                + ", role=" + role
                + '}';
    }
}

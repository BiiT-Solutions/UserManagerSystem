package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.CreatedElement;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "application_backend_service_roles")
public class ApplicationBackendServiceRole extends CreatedElement {

    @EmbeddedId
    private ApplicationBackendServiceRoleId id;

    public ApplicationBackendServiceRole() {
        super();
    }

    public ApplicationBackendServiceRole(ApplicationRole applicationRole, BackendServiceRole backendServiceRole) {
        this();
        setId(new ApplicationBackendServiceRoleId(applicationRole, backendServiceRole));
    }

    public ApplicationBackendServiceRoleId getId() {
        return id;
    }

    public void setId(ApplicationBackendServiceRoleId id) {
        this.id = id;
    }

    @Override
    public void setCreatedOn(String createdOn) {
        //Do nothing, as application backend service roles are not linked to organization.
    }

    @Override
    public String toString() {
        return "ApplicationBackendServiceRole{"
                + "id=" + id
                + '}';
    }
}

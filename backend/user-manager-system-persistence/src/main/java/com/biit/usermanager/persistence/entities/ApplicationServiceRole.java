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
@Table(name = "application_service_roles")
public class ApplicationServiceRole extends CreatedElement {

    @EmbeddedId
    private ApplicationServiceRoleId id;

    public ApplicationServiceRole() {
        super();
    }

    public ApplicationServiceRole(ApplicationRole applicationRole, ServiceRole serviceRole) {
        this();
        setId(new ApplicationServiceRoleId(applicationRole, serviceRole));
    }

    public ApplicationServiceRoleId getId() {
        return id;
    }

    public void setId(ApplicationServiceRoleId id) {
        this.id = id;
    }

}

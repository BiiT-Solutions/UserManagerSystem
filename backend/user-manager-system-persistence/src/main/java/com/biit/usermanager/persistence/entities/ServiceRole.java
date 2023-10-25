package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.CreatedElement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "service_roles")
public class ServiceRole extends CreatedElement {

    @EmbeddedId
    private ServiceRoleId id;

    public ServiceRole() {
        super();
    }

    public ServiceRole(Service service, String name) {
        super();
        setId(new ServiceRoleId(service, name));
    }

    public ServiceRoleId getId() {
        return id;
    }

    public void setId(ServiceRoleId id) {
        this.id = id;
    }

    @JsonIgnore
    public String getName() {
        if (id == null) {
            return null;
        }
        return id.getName();
    }

    @JsonIgnore
    public Service getService() {
        if (id == null) {
            return null;
        }
        return id.getService();
    }

    public String getGrantedAuthority() {
        return id.getService().getName().toUpperCase() + "_" + id.getName().toUpperCase();
    }
}

package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.CreatedElement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "backend_service_roles")
public class BackendServiceRole extends CreatedElement implements Serializable {

    @Serial
    private static final long serialVersionUID = 2293890542632212445L;

    @EmbeddedId
    private BackendServiceRoleId id;

    public BackendServiceRole() {
        super();
    }

    public BackendServiceRole(BackendService backendService, String name) {
        super();
        setId(new BackendServiceRoleId(backendService, name));
    }

    public BackendServiceRoleId getId() {
        return id;
    }

    public void setId(BackendServiceRoleId id) {
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
    public BackendService getBackendService() {
        if (id == null) {
            return null;
        }
        return id.getBackendService();
    }

    @Override
    public String toString() {
        return "BackendServiceRole{"
                + "id=" + id
                + '}';
    }
}

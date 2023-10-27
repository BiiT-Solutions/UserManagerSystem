package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.StorableObject;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "users_by_application_backend_service_roles", indexes = {
        @Index(name = "ind_user", columnList = "user_id"),
})
public class UserApplicationBackendServiceRole extends StorableObject {

    @EmbeddedId
    private UserApplicationBackendServiceRoleId id;

    public UserApplicationBackendServiceRole() {
        super();
    }

    public UserApplicationBackendServiceRole(Long userId, String applicationName, String roleName, String backendServiceName, String backendServiceRole) {
        this();
        setId(new UserApplicationBackendServiceRoleId(userId, applicationName, roleName, backendServiceName, backendServiceRole));
    }

    public UserApplicationBackendServiceRoleId getId() {
        return id;
    }

    public void setId(UserApplicationBackendServiceRoleId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserApplicationBackendServiceRole{"
                + "id=" + id
                + '}';
    }
}

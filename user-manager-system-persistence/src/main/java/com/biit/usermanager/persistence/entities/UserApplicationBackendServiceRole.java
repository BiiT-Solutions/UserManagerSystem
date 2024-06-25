package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.StorableObject;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

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

    public UserApplicationBackendServiceRole(Long userId, ApplicationRole applicationRole, BackendServiceRole backendServiceRole) {
        setId(new UserApplicationBackendServiceRoleId(userId, applicationRole.getId().getApplication().getName(), applicationRole.getId().getRole().getName(),
                backendServiceRole.getId().getBackendService().getName(), backendServiceRole.getName()));
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserApplicationBackendServiceRole that = (UserApplicationBackendServiceRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserApplicationBackendServiceRole{"
                + "id=" + id
                + '}';
    }
}

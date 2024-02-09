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
@Table(name = "user_groups_by_application_backend_service_roles", indexes = {
        @Index(name = "ind_usergroup", columnList = "user_group_id"),
})
public class UserGroupApplicationBackendServiceRole extends StorableObject {

    @EmbeddedId
    private UserGroupApplicationBackendServiceRoleId id;

    public UserGroupApplicationBackendServiceRole() {
        super();
    }

    public UserGroupApplicationBackendServiceRole(Long userGroupId, String applicationName, String roleName, String backendServiceName,
                                                  String backendServiceRole) {
        this();
        setId(new UserGroupApplicationBackendServiceRoleId(userGroupId, applicationName, roleName, backendServiceName, backendServiceRole));
    }

    public UserGroupApplicationBackendServiceRoleId getId() {
        return id;
    }

    public void setId(UserGroupApplicationBackendServiceRoleId id) {
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
        final UserGroupApplicationBackendServiceRole that = (UserGroupApplicationBackendServiceRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserGroupApplicationBackendServiceRole{"
                + "id=" + id
                + '}';
    }
}

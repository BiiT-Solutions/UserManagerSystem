package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.CreatedElement;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "application_roles")
public class ApplicationRole extends CreatedElement implements Serializable {

    @Serial
    private static final long serialVersionUID = -2291890542632281242L;

    @EmbeddedId
    private ApplicationRoleId id;

    public ApplicationRole() {
        super();
    }

    public ApplicationRole(Application application, Role role) {
        setId(new ApplicationRoleId(application, role));
    }

    public ApplicationRoleId getId() {
        return id;
    }

    public void setId(ApplicationRoleId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ApplicationRole{"
                + "id=" + id
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationRole that = (ApplicationRole) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

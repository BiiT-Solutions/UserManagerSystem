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
}

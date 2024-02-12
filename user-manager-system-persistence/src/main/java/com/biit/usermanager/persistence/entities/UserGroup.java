package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Set;

/**
 * This group is used as a group for permissions.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "user_groups", indexes = {
        @Index(name = "ind_groupname", columnList = "name")
})
public class UserGroup extends Element<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";

    @Column(name = "description")
    @Convert(converter = StringCryptoConverter.class)
    private String description = "";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_groups_by_application_backend_service_roles",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "application_role_application", referencedColumnName = "application_role_application"),
                    @JoinColumn(name = "application_role_role", referencedColumnName = "application_role_role"),
                    @JoinColumn(name = "backend_service_role_service", referencedColumnName = "backend_service_role_service"),
                    @JoinColumn(name = "backend_service_role_name", referencedColumnName = "backend_service_role_name"),
            })
    private Set<ApplicationBackendServiceRole> applicationBackendServiceRoles;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_groups_users",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")
            })
    private Set<User> users;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return users;
    }
}

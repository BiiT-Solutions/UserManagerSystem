package com.biit.usermanager.persistence.entities;

/*-
 * #%L
 * User Manager System (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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

    @Column(name = "name", nullable = false, unique = true, length = MAX_UNIQUE_COLUMN_LENGTH)
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

    @Override
    public void setCreatedOn(String createdOn) {
        //Do nothing, as user groups are not linked to organization.
    }

    @Override
    public String toString() {
        return "UserGroup{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}

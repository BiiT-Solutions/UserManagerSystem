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

import com.biit.server.persistence.entities.CreatedElement;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "team_members", indexes = {
        @Index(name = "ind_team_member", columnList = "team_id"),
})
public class TeamMember extends CreatedElement {

    @EmbeddedId
    private TeamMemberId id;

    @Column(name = "organization_name")
    private String organizationName;

    public TeamMember() {
        super();
    }

    public TeamMember(Long teamId, Long userId, String organizationName, String createdBy) {
        this();
        setId(new TeamMemberId(teamId, userId));
        setOrganizationName(organizationName);
        setCreatedBy(createdBy);
    }

    public TeamMemberId getId() {
        return id;
    }

    public void setId(TeamMemberId id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TeamMember that = (TeamMember) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

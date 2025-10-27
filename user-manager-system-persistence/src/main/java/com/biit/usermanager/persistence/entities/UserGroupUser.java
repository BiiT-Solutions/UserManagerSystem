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

import com.biit.server.persistence.entities.StorableObject;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "user_groups_users", indexes = {
        @Index(name = "ind_usergroup_users", columnList = "user_group_id"),
})
public class UserGroupUser extends StorableObject {

    @Serial
    private static final long serialVersionUID = -5378190918989695625L;

    @EmbeddedId
    private UserGroupUserId id;

    public UserGroupUser() {
        super();
    }

    public UserGroupUser(Long userGroupId, Long userId) {
        this();
        setId(new UserGroupUserId(userGroupId, userId));
    }

    public UserGroupUserId getId() {
        return id;
    }

    public void setId(UserGroupUserId id) {
        this.id = id;
    }
}

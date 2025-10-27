package com.biit.usermanager.core.providers;

/*-
 * #%L
 * User Manager System (core)
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

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.UserGroup;
import com.biit.usermanager.persistence.repositories.UserGroupRepository;
import com.biit.usermanager.persistence.repositories.UserGroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserGroupProvider extends ElementProvider<UserGroup, Long, UserGroupRepository> {

    private final UserGroupUserRepository userGroupUserRepository;

    @Autowired
    public UserGroupProvider(UserGroupRepository repository, UserGroupUserRepository userGroupUserRepository) {
        super(repository);
        this.userGroupUserRepository = userGroupUserRepository;
    }

    public Optional<UserGroup> findByName(String name) {
        return getRepository().findByNameIgnoreCase(name);
    }

    public long deleteByName(String name) {
        return getRepository().deleteByNameIgnoreCase(name);
    }


    public List<UserGroup> getByUserGroup(Long groupId) {
        return findByIdIn(userGroupUserRepository.findByIdUserGroupId(groupId).stream()
                .map(userGroupUsers -> userGroupUsers.getId().getUserGroupId()).toList());
    }

    public List<UserGroup> getByUser(Long userId) {
        return findByIdIn(userGroupUserRepository.findByIdUserId(userId).stream()
                .map(userGroupUsers -> userGroupUsers.getId().getUserGroupId()).toList());
    }
}

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

import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserGroup;
import com.biit.usermanager.persistence.entities.UserGroupUser;
import com.biit.usermanager.persistence.entities.UserGroupUserId;
import com.biit.usermanager.persistence.repositories.UserGroupRepository;
import com.biit.usermanager.persistence.repositories.UserGroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserGroupUserProvider extends StorableObjectProvider<UserGroupUser, UserGroupUserId, UserGroupUserRepository> {


    @Value("${user.default.role.group:#{null}}")
    private String defaultGroupName;

    private final UserGroupRepository userGroupRepository;

    @Autowired
    public UserGroupUserProvider(UserGroupUserRepository repository, UserGroupRepository userGroupRepository) {
        super(repository);
        this.userGroupRepository = userGroupRepository;
    }

    public Set<UserGroupUser> findByIdUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public Set<UserGroupUser> findByIdUserGroupId(Long userGroupId) {
        return getRepository().findByIdUserGroupId(userGroupId);
    }

    public void assignToDefaultGroup(User user) {
        if (defaultGroupName != null && !defaultGroupName.isBlank() && user != null) {
            final UserGroup defaultGroup = userGroupRepository.findByNameIgnoreCase(defaultGroupName).orElse(null);
            if (defaultGroup != null) {
                try {
                    getRepository().save(new UserGroupUser(defaultGroup.getId(), user.getId()));
                    UserManagerLogger.info(this.getClass(), "User '{}' added to default group '{}'.", user, defaultGroup);
                } catch (Exception e) {
                    UserManagerLogger.warning(this.getClass(), "User '{}' cannot be added to default group '{}'.", user, defaultGroup);
                }
            } else {
                UserManagerLogger.debug(this.getClass(), "No default group defined or non existing. Property value '{}'.", defaultGroupName);
            }
        }
    }
}

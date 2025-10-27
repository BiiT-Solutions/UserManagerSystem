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
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.UserGroupApplicationBackendServiceRoleRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserGroupApplicationBackendServiceRoleProvider extends StorableObjectProvider<UserGroupApplicationBackendServiceRole,
        UserGroupApplicationBackendServiceRoleId, UserGroupApplicationBackendServiceRoleRepository> {

    public UserGroupApplicationBackendServiceRoleProvider(UserGroupApplicationBackendServiceRoleRepository repository) {
        super(repository);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByUserGroupId(Long userGroupId) {
        return getRepository().findByIdUserGroupId(userGroupId);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByUserGroupIdIn(Collection<Long> userGroupIds) {
        return getRepository().findByIdUserGroupIdIn(userGroupIds);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByApplicationName(String applicationName) {
        return getRepository().findByIdApplicationName(applicationName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByApplicationRoleName(String roleName) {
        return getRepository().findByIdRoleName(roleName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByBackendServiceName(String backendServiceName) {
        return getRepository().findByIdBackendServiceName(backendServiceName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findByBackendServiceNameAndBackendServiceRole(String backendServiceName, String backendServiceRoleName) {
        return getRepository().findByIdBackendServiceNameAndIdBackendServiceRole(backendServiceName, backendServiceRoleName);
    }


    public Set<UserGroupApplicationBackendServiceRole> findByBackendServiceRole(String roleName) {
        return getRepository().findByIdBackendServiceRole(roleName);
    }

    public Optional<UserGroupApplicationBackendServiceRole> findBy(
            Long userId, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        return getRepository().findByIdUserGroupIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                userId, applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findBy(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        return getRepository().findByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public void deleteBy(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        getRepository().deleteByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public List<UserGroupApplicationBackendServiceRole> findBy(
            Long userId, String applicationName, String applicationRoleName) {
        return getRepository().findByIdUserGroupIdAndIdApplicationNameAndIdRoleName(userId, applicationName, applicationRoleName);
    }

    public Set<UserGroupApplicationBackendServiceRole> findBy(ApplicationRole applicationRole) {
        return findBy(applicationRole.getId().getApplication().getName(), applicationRole.getId().getRole().getName());
    }

    public Set<UserGroupApplicationBackendServiceRole> findBy(String applicationName, String applicationRoleName) {
        return getRepository().findByIdApplicationNameAndIdRoleName(applicationName, applicationRoleName);
    }

    public Optional<UserGroupApplicationBackendServiceRole> findBy(
            Long userId, ApplicationBackendServiceRole applicationBackendServiceRole) {
        return getRepository().findByIdUserGroupIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                userId, applicationBackendServiceRole.getId().getApplicationRole().getId().getApplication().getName(),
                applicationBackendServiceRole.getId().getApplicationRole().getId().getRole().getName(),
                applicationBackendServiceRole.getId().getBackendServiceRole().getId().getBackendService().getName(),
                applicationBackendServiceRole.getId().getBackendServiceRole().getId().getName()
        );
    }

}

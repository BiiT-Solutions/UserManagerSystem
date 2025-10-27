package com.biit.usermanager.persistence.repositories;

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

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRoleId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface UserApplicationBackendServiceRoleRepository extends StorableObjectRepository<UserApplicationBackendServiceRole,
        UserApplicationBackendServiceRoleId> {

    Set<UserApplicationBackendServiceRole> findByIdUserId(Long userId);

    Set<UserApplicationBackendServiceRole> findByIdUserIdAndIdApplicationName(Long userId, String applicationName);

    Set<UserApplicationBackendServiceRole> findByIdApplicationName(String applicationName);

    void deleteByIdApplicationName(String applicationName);

    void deleteByIdApplicationNameIn(Collection<String> applicationName);

    void deleteByIdUserId(Long userId);

    Set<UserApplicationBackendServiceRole> findByIdRoleName(String applicationRoleName);

    Set<UserApplicationBackendServiceRole> findByIdApplicationNameAndIdRoleName(String applicationName, String applicationRoleName);

    Set<UserApplicationBackendServiceRole> deleteByIdRoleName(String applicationRoleName);

    void deleteByIdRoleNameIn(Collection<String> applicationRoleName);

    Set<UserApplicationBackendServiceRole> findByIdBackendServiceName(String backendServiceName);

    Set<UserApplicationBackendServiceRole> findByIdBackendServiceRole(String backendServiceRoleName);

    Set<UserApplicationBackendServiceRole> findByIdBackendServiceNameAndIdBackendServiceRole(String backendServiceName, String backendServiceRoleName);

    void deleteByIdBackendServiceNameAndIdBackendServiceRole(String backendServiceName, String backendServiceRoleName);

    void deleteByIdApplicationNameAndIdRoleName(String applicationName, String roleName);

    Set<UserApplicationBackendServiceRole> deleteByIdBackendServiceRoleIn(Collection<String> backendServiceRoleName);

    void deleteByIdBackendServiceName(String backendServiceName);

    void deleteByIdBackendServiceNameIn(Collection<String> backendServiceName);

    Optional<UserApplicationBackendServiceRole> findByIdUserIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            Long userId, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);

    Set<UserApplicationBackendServiceRole> findByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);

    void deleteByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);


    List<UserApplicationBackendServiceRole> findByIdUserIdAndIdApplicationNameAndIdRoleName(Long userId, String applicationName, String applicationRoleName);
}

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

import com.biit.server.persistence.repositories.CreatedElementRepository;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ApplicationBackendServiceRoleRepository extends CreatedElementRepository<ApplicationBackendServiceRole, ApplicationBackendServiceRoleId> {

    Optional<ApplicationBackendServiceRole> findByIdApplicationRoleAndIdBackendServiceRole(
            ApplicationRole applicationRole, BackendServiceRole backendServiceRole);

    Optional<ApplicationBackendServiceRole>
    findByIdApplicationRoleIdApplicationIdAndIdApplicationRoleIdRoleIdAndIdBackendServiceRoleIdBackendServiceIdAndIdBackendServiceRoleIdName(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);

    List<ApplicationBackendServiceRole> findByIdApplicationRoleIdApplicationIdAndIdApplicationRoleIdRoleId(
            String applicationName, String applicationRoleName);

    List<ApplicationBackendServiceRole> findByIdApplicationRole(ApplicationRole applicationRole);

    List<ApplicationBackendServiceRole> findByIdBackendServiceRole(BackendServiceRole backendServiceRole);

    void deleteByIdBackendServiceRole(BackendServiceRole backendServiceRole);

    void deleteByIdBackendServiceRoleIn(Collection<BackendServiceRole> backendServiceRole);

    void deleteByIdApplicationRole(ApplicationRole applicationRole);

    List<ApplicationBackendServiceRole> deleteByIdApplicationRoleIdRoleId(String applicationRoleName);

    void deleteByIdApplicationRoleIn(Collection<ApplicationRole> applicationRole);

}

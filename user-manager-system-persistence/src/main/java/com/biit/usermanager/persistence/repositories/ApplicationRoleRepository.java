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
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import com.biit.usermanager.persistence.entities.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ApplicationRoleRepository extends CreatedElementRepository<ApplicationRole, ApplicationRoleId> {

    List<ApplicationRole> findByIdApplication(Application application);

    List<ApplicationRole> findByIdApplicationIn(Collection<Application> application);

    List<ApplicationRole> findByIdApplicationId(String applicationId);

    List<ApplicationRole> findByIdRole(Role role);

    List<ApplicationRole> deleteByIdRole(Role role);

    List<ApplicationRole> findByIdRoleIn(Collection<Role> role);

    List<ApplicationRole> findByIdRoleId(String roleId);

    Optional<ApplicationRole> findByIdApplicationIdAndIdRoleId(String applicationId, String roleId);
}

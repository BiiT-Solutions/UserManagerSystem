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

import com.biit.server.providers.CreatedElementProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationRoleProvider extends CreatedElementProvider<ApplicationRole, ApplicationRoleId, ApplicationRoleRepository> {

    private final ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository;

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    @Autowired
    public ApplicationRoleProvider(ApplicationRoleRepository repository, ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository,
                                   UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository) {
        super(repository);
        this.applicationBackendServiceRoleRepository = applicationBackendServiceRoleRepository;
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
    }

    public List<ApplicationRole> findByApplication(Application application) {
        return getRepository().findByIdApplication(application);
    }

    public List<ApplicationRole> findByApplicationId(String applicationId) {
        return getRepository().findByIdApplicationId(applicationId);
    }

    @Cacheable(value = "application_roles", key = "T(java.util.Objects).hash(#applicationId,#roleId)")
    public Optional<ApplicationRole> findByApplicationIdAndRoleId(String applicationId, String roleId) {
        return getRepository().findByIdApplicationIdAndIdRoleId(applicationId, roleId);
    }

    public List<ApplicationRole> findByRole(Role role) {
        return getRepository().findByIdRole(role);
    }

    public List<ApplicationRole> findByRoleId(String roleId) {
        return getRepository().findByIdRoleId(roleId);
    }

    @Override
    @Transactional
    public void delete(ApplicationRole entity) {
        if (entity == null) {
            return;
        }
        userApplicationBackendServiceRoleRepository.deleteByIdApplicationNameAndIdRoleName(
                entity.getId().getApplication().getName(), entity.getId().getRole().getName());
        applicationBackendServiceRoleRepository.deleteByIdApplicationRole(entity);
        super.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll(Collection<ApplicationRole> entities) {
        if (entities == null) {
            return;
        }
        entities.forEach(entity -> userApplicationBackendServiceRoleRepository.deleteByIdApplicationNameAndIdRoleName(
                entity.getId().getApplication().getName(), entity.getId().getRole().getName()));
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(entities);
        super.deleteAll(entities);
    }

    @Override
    @Transactional
    public void deleteById(ApplicationRoleId id) {
        delete(getRepository().findById(id).orElse(null));
    }
}

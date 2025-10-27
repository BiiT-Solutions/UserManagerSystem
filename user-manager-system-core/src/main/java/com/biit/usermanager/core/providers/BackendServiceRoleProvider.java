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
import com.biit.usermanager.core.exceptions.RoleAlreadyExistsException;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.BackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class BackendServiceRoleProvider extends CreatedElementProvider<BackendServiceRole, BackendServiceRoleId, BackendServiceRoleRepository> {

    private final ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository;

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    public BackendServiceRoleProvider(BackendServiceRoleRepository repository,
                                      ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository,
                                      UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository) {
        super(repository);
        this.applicationBackendServiceRoleRepository = applicationBackendServiceRoleRepository;
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
    }

    public List<BackendServiceRole> findByBackendService(BackendService backendService) {
        return getRepository().findByIdBackendService(backendService);
    }

    public List<BackendServiceRole> findByName(String name) {
        return getRepository().findByIdName(name);
    }

    public Optional<BackendServiceRole> findByBackendServiceAndName(BackendService backendService, String name) {
        return getRepository().findByIdBackendServiceAndIdName(backendService, name);
    }

    @Cacheable(value = "backend_service_roles", key = "T(java.util.Objects).hash(#backendServiceName,#roleName)")
    public Optional<BackendServiceRole> findByBackendServiceAndName(String backendServiceName, String roleName) {
        return getRepository().findByIdBackendServiceIdAndIdName(backendServiceName, roleName);
    }

    @Override
    public BackendServiceRole save(BackendServiceRole entity) {
        if (entity == null) {
            return null;
        }
        //Check if exists.
        if (getRepository().findById(entity.getId()).isPresent()) {
            throw new RoleAlreadyExistsException(this.getClass(), "The service '" + entity.getId().getBackendService().getName()
                    + "' already has role '" + entity.getId().getName() + "'!");
        }
        return super.save(entity);
    }

    @Override
    @Transactional
    public void delete(BackendServiceRole entity) {
        if (entity == null) {
            return;
        }
        userApplicationBackendServiceRoleRepository.deleteByIdBackendServiceNameAndIdBackendServiceRole(entity.getBackendService().getName(), entity.getName());
        applicationBackendServiceRoleRepository.deleteByIdBackendServiceRole(entity);
        super.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll(Collection<BackendServiceRole> entities) {
        if (entities == null) {
            return;
        }
        entities.forEach(entity -> userApplicationBackendServiceRoleRepository
                .deleteByIdBackendServiceNameAndIdBackendServiceRole(entity.getBackendService().getName(), entity.getName()));
        applicationBackendServiceRoleRepository.deleteByIdBackendServiceRoleIn(entities);
        super.deleteAll(entities);
    }

    @Override
    @Transactional
    public void deleteById(BackendServiceRoleId id) {
        delete(getRepository().findById(id).orElse(null));
    }

    public List<BackendServiceRole> findByApplicationAndRole(String applicationName, String applicationRoleName) {
        final List<ApplicationBackendServiceRole> applicationBackendServiceRoles = applicationBackendServiceRoleRepository
                .findByIdApplicationRoleIdApplicationIdAndIdApplicationRoleIdRoleId(applicationName, applicationRoleName);

        final List<BackendServiceRole> backendServiceRoles = new ArrayList<>();

        for (ApplicationBackendServiceRole applicationBackendServiceRole : applicationBackendServiceRoles) {
            final Optional<BackendServiceRole> backendServiceRole = findByBackendServiceAndName(
                    applicationBackendServiceRole.getId().getBackendServiceRole().getBackendService().getName(),
                    applicationBackendServiceRole.getId().getBackendServiceRole().getName()
            );
            backendServiceRole.ifPresent(backendServiceRoles::add);
        }

        return backendServiceRoles;
    }

}

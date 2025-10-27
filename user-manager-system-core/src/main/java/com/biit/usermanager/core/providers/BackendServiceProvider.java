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
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.BackendServiceRepository;
import com.biit.usermanager.persistence.repositories.BackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class BackendServiceProvider extends ElementProvider<BackendService, String, BackendServiceRepository> {

    private final ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository;

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    private final BackendServiceRoleRepository backendServiceRoleRepository;

    public BackendServiceProvider(BackendServiceRepository repository,
                                  ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository,
                                  UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository,
                                  BackendServiceRoleRepository backendServiceRoleRepository) {
        super(repository);
        this.applicationBackendServiceRoleRepository = applicationBackendServiceRoleRepository;
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
        this.backendServiceRoleRepository = backendServiceRoleRepository;
    }

    public Optional<BackendService> findByName(String name) {
        return getRepository().findById(name);
    }

    @Override
    @Transactional
    public void delete(BackendService entity) {
        if (entity == null) {
            return;
        }
        final List<BackendServiceRole> backendServiceRoles = backendServiceRoleRepository.findByIdBackendService(entity);
        userApplicationBackendServiceRoleRepository.deleteByIdBackendServiceName(entity.getName());
        applicationBackendServiceRoleRepository.deleteByIdBackendServiceRoleIn(backendServiceRoles);
        backendServiceRoleRepository.deleteAll(backendServiceRoles);
        super.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll(Collection<BackendService> entities) {
        if (entities == null) {
            return;
        }
        final List<BackendServiceRole> backendServiceRoles = backendServiceRoleRepository.findByIdBackendServiceIn(entities);
        userApplicationBackendServiceRoleRepository.deleteByIdBackendServiceNameIn(entities.stream().map(BackendService::getName).toList());
        applicationBackendServiceRoleRepository.deleteByIdBackendServiceRoleIn(backendServiceRoles);
        backendServiceRoleRepository.deleteAll(backendServiceRoles);
        super.deleteAll(entities);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        delete(getRepository().findById(id).orElse(null));
    }
}

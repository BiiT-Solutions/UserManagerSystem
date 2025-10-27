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
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationProvider extends ElementProvider<Application, String, ApplicationRepository> {

    private final ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository;

    private final ApplicationRoleRepository applicationRoleRepository;

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    @Autowired
    public ApplicationProvider(ApplicationRepository repository,
                               ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository,
                               ApplicationRoleRepository applicationRoleRepository,
                               UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository) {
        super(repository);
        this.applicationBackendServiceRoleRepository = applicationBackendServiceRoleRepository;
        this.applicationRoleRepository = applicationRoleRepository;
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
    }

    public Optional<Application> findByName(String name) {
        return getRepository().findById(name);
    }

    @Override
    @Transactional
    public void delete(Application entity) {
        if (entity == null) {
            return;
        }
        userApplicationBackendServiceRoleRepository.deleteByIdApplicationName(entity.getName());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdApplication(entity);
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll(Collection<Application> entities) {
        if (entities == null) {
            return;
        }
        userApplicationBackendServiceRoleRepository.deleteByIdApplicationNameIn(entities.stream().map(Application::getName).toList());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdApplicationIn(entities);
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.deleteAll(entities);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        delete(getRepository().findById(id).orElse(null));
    }
}

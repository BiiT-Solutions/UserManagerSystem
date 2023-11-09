package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.BackendServiceRepository;
import com.biit.usermanager.persistence.repositories.BackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
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
    public void deleteAll(Collection<BackendService> entities) {
        final List<BackendServiceRole> backendServiceRoles = backendServiceRoleRepository.findByIdBackendServiceIn(entities);
        userApplicationBackendServiceRoleRepository.deleteByIdBackendServiceNameIn(entities.stream().map(BackendService::getName).toList());
        applicationBackendServiceRoleRepository.deleteByIdBackendServiceRoleIn(backendServiceRoles);
        backendServiceRoleRepository.deleteAll(backendServiceRoles);
        super.deleteAll(entities);
    }
}

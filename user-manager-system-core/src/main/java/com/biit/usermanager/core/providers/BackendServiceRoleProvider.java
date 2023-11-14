package com.biit.usermanager.core.providers;

import com.biit.server.providers.CreatedElementProvider;
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

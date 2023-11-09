package com.biit.usermanager.core.providers;

import com.biit.server.providers.CreatedElementProvider;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationBackendServiceRoleProvider extends CreatedElementProvider<ApplicationBackendServiceRole,
        ApplicationBackendServiceRoleId, ApplicationBackendServiceRoleRepository> {

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    @Autowired
    public ApplicationBackendServiceRoleProvider(ApplicationBackendServiceRoleRepository repository,
                                                 UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository) {
        super(repository);
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
    }

    public Optional<ApplicationBackendServiceRole> findByApplicationRoleAndServiceRole(ApplicationRole applicationRole, BackendServiceRole backendServiceRole) {
        return getRepository().findByIdApplicationRoleAndIdBackendServiceRole(applicationRole, backendServiceRole);
    }

    public Optional<ApplicationBackendServiceRole> findByApplicationRoleAndServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        return getRepository()
                .findByIdApplicationRoleIdApplicationIdAndIdApplicationRoleIdRoleIdAndIdBackendServiceRoleIdBackendServiceIdAndIdBackendServiceRoleIdName(
                        applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public List<ApplicationBackendServiceRole> findByApplicationNameAndApplicationRole(String applicationName, String applicationRoleName) {
        return getRepository().findByIdApplicationRoleIdApplicationIdAndIdApplicationRoleIdRoleId(applicationName, applicationRoleName);
    }

    public List<ApplicationBackendServiceRole> findByApplicationRole(ApplicationRole applicationRole) {
        return getRepository().findByIdApplicationRole(applicationRole);
    }

    public List<ApplicationBackendServiceRole> findByServiceRole(BackendServiceRole backendServiceRole) {
        return getRepository().findByIdBackendServiceRole(backendServiceRole);
    }

    @Override
    @Transactional
    public void delete(ApplicationBackendServiceRole entity) {
        if (entity == null) {
            return;
        }
        userApplicationBackendServiceRoleRepository.deleteByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                entity.getId().getApplicationRole().getId().getApplication().getName(),
                entity.getId().getApplicationRole().getId().getRole().getName(),
                entity.getId().getBackendServiceRole().getId().getBackendService().getName(),
                entity.getId().getBackendServiceRole().getId().getName()
        );
        super.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll(Collection<ApplicationBackendServiceRole> entities) {
        if (entities == null) {
            return;
        }
        entities.forEach(entity ->
                userApplicationBackendServiceRoleRepository.deleteByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                        entity.getId().getApplicationRole().getId().getApplication().getName(),
                        entity.getId().getApplicationRole().getId().getRole().getName(),
                        entity.getId().getBackendServiceRole().getId().getBackendService().getName(),
                        entity.getId().getBackendServiceRole().getId().getName()
                ));
        super.deleteAll(entities);
    }

    @Override
    @Transactional
    public void deleteById(ApplicationBackendServiceRoleId id) {
        delete(getRepository().findById(id).orElse(null));
    }
}

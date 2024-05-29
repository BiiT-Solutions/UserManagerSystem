package com.biit.usermanager.core.providers;

import com.biit.server.persistence.exceptions.DatabaseOperationNotAllowed;
import com.biit.server.providers.CreatedElementProvider;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationBackendServiceRoleProvider extends CreatedElementProvider<ApplicationBackendServiceRole,
        ApplicationBackendServiceRoleId, ApplicationBackendServiceRoleRepository> {

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    private final UserProvider userProvider;

    @Autowired
    public ApplicationBackendServiceRoleProvider(ApplicationBackendServiceRoleRepository repository,
                                                 UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository,
                                                 UserProvider userProvider) {
        super(repository);
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
        this.userProvider = userProvider;
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
    public ApplicationBackendServiceRole save(ApplicationBackendServiceRole entity) {
        final ApplicationBackendServiceRole applicationBackendServiceRole = this.getRepository().save(entity);
        //We need to update users with ApplicationRoles to have the new ApplicationBackendServiceRole.
        final Set<UserApplicationBackendServiceRole> usersWithApplicationBackendServiceRoles = userApplicationBackendServiceRoleRepository
                .findByIdApplicationNameAndIdRoleName(
                        entity.getId().getApplicationRole().getId().getApplication().getName(),
                        entity.getId().getApplicationRole().getId().getRole().getName()
                );
        final Set<Long> usersIds = usersWithApplicationBackendServiceRoles.stream().map(userApplicationBackendServiceRole ->
                userApplicationBackendServiceRole.getId().getUserId()).collect(Collectors.toSet());
        final List<UserApplicationBackendServiceRole> newUserApplicationBackendServiceRoles = new ArrayList<>();
        usersIds.forEach(userId ->
                newUserApplicationBackendServiceRoles.add(new UserApplicationBackendServiceRole(userId,
                        entity.getId().getApplicationRole().getId().getApplication().getName(),
                        entity.getId().getApplicationRole().getId().getRole().getName(),
                        entity.getId().getBackendServiceRole().getId().getBackendService().getName(),
                        entity.getId().getBackendServiceRole().getId().getName()
                )));
        userApplicationBackendServiceRoleRepository.saveAll(newUserApplicationBackendServiceRoles);
        return applicationBackendServiceRole;
    }

    @Override
    @Transactional
    public void delete(ApplicationBackendServiceRole entity) {
        if (entity == null) {
            return;
        }
        try {
            userApplicationBackendServiceRoleRepository.deleteByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                    entity.getId().getApplicationRole().getId().getApplication().getName(),
                    entity.getId().getApplicationRole().getId().getRole().getName(),
                    entity.getId().getBackendServiceRole().getId().getBackendService().getName(),
                    entity.getId().getBackendServiceRole().getId().getName()
            );
            super.delete(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationNotAllowed(this.getClass(), "You cannot perform this action, as other elements is using this entity", e);
        }
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

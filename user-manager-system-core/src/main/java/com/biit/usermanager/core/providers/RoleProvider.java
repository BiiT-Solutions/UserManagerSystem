package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
import com.biit.usermanager.persistence.repositories.RoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class RoleProvider extends ElementProvider<Role, String, RoleRepository> {

    private final ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository;

    private final ApplicationRoleRepository applicationRoleRepository;

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    @Autowired
    public RoleProvider(RoleRepository repository, ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository,
                        ApplicationRoleRepository applicationRoleRepository,
                        UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository) {
        super(repository);
        this.applicationBackendServiceRoleRepository = applicationBackendServiceRoleRepository;
        this.applicationRoleRepository = applicationRoleRepository;
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
    }

    public Optional<Role> findByName(String name) {
        return getRepository().findById(name);
    }

    @Override
    public void delete(Role entity) {
        userApplicationBackendServiceRoleRepository.deleteByIdRoleName(entity.getName());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdRole(entity);
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.delete(entity);
    }

    @Override
    public void deleteAll(Collection<Role> entities) {
        userApplicationBackendServiceRoleRepository.deleteByIdRoleNameIn(entities.stream().map(Role::getName).toList());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdRoleIn(entities);
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.deleteAll(entities);
    }
}

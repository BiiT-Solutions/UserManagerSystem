package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.core.exceptions.RoleAlreadyExistsException;
import com.biit.usermanager.core.exceptions.RoleNotFoundException;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
import com.biit.usermanager.persistence.repositories.BackendServiceRepository;
import com.biit.usermanager.persistence.repositories.BackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.RoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.UserGroupApplicationBackendServiceRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class RoleProvider extends ElementProvider<Role, String, RoleRepository> {

    private final ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository;

    private final ApplicationRoleRepository applicationRoleRepository;
    private final ApplicationRepository applicationRepository;
    private final BackendServiceRoleRepository backendServiceRoleRepository;
    private final BackendServiceRepository backendServiceRepository;

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;
    private final UserGroupApplicationBackendServiceRoleRepository userGroupApplicationBackendServiceRoleRepository;

    @Value("${spring.application.name}")
    private String backendServiceName;

    @Autowired
    public RoleProvider(RoleRepository repository, ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository,
                        ApplicationRoleRepository applicationRoleRepository, ApplicationRepository applicationRepository,
                        BackendServiceRoleRepository backendServiceRoleRepository,
                        BackendServiceRepository backendServiceRepository,
                        UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository,
                        UserGroupApplicationBackendServiceRoleRepository userGroupApplicationBackendServiceRoleRepository) {
        super(repository);
        this.applicationBackendServiceRoleRepository = applicationBackendServiceRoleRepository;
        this.applicationRoleRepository = applicationRoleRepository;
        this.applicationRepository = applicationRepository;
        this.backendServiceRoleRepository = backendServiceRoleRepository;
        this.backendServiceRepository = backendServiceRepository;
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
        this.userGroupApplicationBackendServiceRoleRepository = userGroupApplicationBackendServiceRoleRepository;
    }

    public Optional<Role> findByName(String name) {
        return getRepository().findById(name);
    }

    @Override
    public Role save(Role entity) {
        if (entity == null) {
            return null;
        }
        //Check if exists.
        if (getRepository().findById(entity.getId()).isPresent()) {
            throw new RoleAlreadyExistsException(this.getClass(), "The role '" + entity.getName() + "' already exists!");
        }
        return super.save(entity);
    }

    @Override
    public void delete(Role entity) {
        userApplicationBackendServiceRoleRepository.deleteByIdRoleName(entity.getName());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdRole(entity);
        userGroupApplicationBackendServiceRoleRepository.deleteByIdRoleName(entity.getName());
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.delete(entity);
    }

    @Override
    public void deleteById(String id) {
        final Role entity = getRepository().findById(id).orElseThrow(() -> new RoleNotFoundException(this.getClass(), "No Role found with id '" + id + "'"));
        delete(entity);
    }

    @Override
    public void deleteAll(Collection<Role> entities) {
        userApplicationBackendServiceRoleRepository.deleteByIdRoleNameIn(entities.stream().map(Role::getName).toList());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdRoleIn(entities);
        userGroupApplicationBackendServiceRoleRepository.deleteByIdRoleNameIn(entities.stream().map(Role::getName).toList());
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.deleteAll(entities);
    }

    public void createDefaultRoleAdmin(Long userId, String roleName) {
        UserManagerLogger.warning(this.getClass(), "Creating role '{}' for user '{}'.", roleName, userId);
        final Role role = super.save(new Role(roleName));
        final Application application = applicationRepository.save(new Application(backendServiceName));
        final ApplicationRole applicationRole = applicationRoleRepository.save(new ApplicationRole(application, role));
        final BackendService backendService = backendServiceRepository.save(new BackendService(backendServiceName));
        final BackendServiceRole backendServiceRole = backendServiceRoleRepository.save(new BackendServiceRole(backendService, roleName));
        applicationBackendServiceRoleRepository.save(new ApplicationBackendServiceRole(applicationRole, backendServiceRole));
        userApplicationBackendServiceRoleRepository.save(new UserApplicationBackendServiceRole(userId, applicationRole, backendServiceRole));
    }
}

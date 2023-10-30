package com.biit.usermanager.core.providers;

import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserApplicationBackendServiceRoleProvider extends StorableObjectProvider<UserApplicationBackendServiceRole,
        UserApplicationBackendServiceRoleId, UserApplicationBackendServiceRoleRepository> {

    public UserApplicationBackendServiceRoleProvider(UserApplicationBackendServiceRoleRepository repository) {
        super(repository);
    }

    public Set<UserApplicationBackendServiceRole> findByUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public Set<UserApplicationBackendServiceRole> findByApplicationName(String applicationName) {
        return getRepository().findByIdApplicationName(applicationName);
    }

    public Set<UserApplicationBackendServiceRole> findByApplicationRoleName(String roleName) {
        return getRepository().findByIdRoleName(roleName);
    }

    public Set<UserApplicationBackendServiceRole> findByBackendServiceName(String backendServiceName) {
        return getRepository().findByIdBackendServiceName(backendServiceName);
    }

    public Set<UserApplicationBackendServiceRole> findByBackendServiceRole(String roleName) {
        return getRepository().findByIdBackendServiceRole(roleName);
    }

    public Optional<UserApplicationBackendServiceRole> findBy(
            Long userId, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        return getRepository().findByIdUserIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                userId, applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public List<UserApplicationBackendServiceRole> findBy(
            Long userId, String applicationName, String applicationRoleName) {
        return getRepository().findByIdUserIdAndIdApplicationNameAndIdRoleName(userId, applicationName, applicationRoleName);
    }

    public Optional<UserApplicationBackendServiceRole> findBy(
            Long userId, ApplicationBackendServiceRole applicationBackendServiceRole) {
        return getRepository().findByIdUserIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
                userId, applicationBackendServiceRole.getId().getApplicationRole().getId().getApplication().getName(),
                applicationBackendServiceRole.getId().getApplicationRole().getId().getRole().getName(),
                applicationBackendServiceRole.getId().getBackendServiceRole().getId().getBackendService().getName(),
                applicationBackendServiceRole.getId().getBackendServiceRole().getId().getName()
        );
    }

}

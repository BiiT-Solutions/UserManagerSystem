package com.biit.usermanager.core.providers;

import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserApplicationBackendServiceRoleProvider extends StorableObjectProvider<UserApplicationBackendServiceRole,
        UserApplicationBackendServiceRoleId, UserApplicationBackendServiceRoleRepository> {

    public UserApplicationBackendServiceRoleProvider(UserApplicationBackendServiceRoleRepository repository) {
        super(repository);
    }

    public List<UserApplicationBackendServiceRole> findByUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public List<UserApplicationBackendServiceRole> findByApplicationName(String applicationName) {
        return getRepository().findByIdApplicationName(applicationName);
    }

    public List<UserApplicationBackendServiceRole> findByApplicationRoleName(String roleName) {
        return getRepository().findByIdRoleName(roleName);
    }

    public List<UserApplicationBackendServiceRole> findByBackendServiceName(String backendServiceName) {
        return getRepository().findByIdBackendServiceName(backendServiceName);
    }

    public List<UserApplicationBackendServiceRole> findByBackendServiceRole(String roleName) {
        return getRepository().findByIdBackendServiceRole(roleName);
    }
}

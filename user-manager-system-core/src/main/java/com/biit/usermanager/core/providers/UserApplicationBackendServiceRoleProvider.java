package com.biit.usermanager.core.providers;

import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;

import java.util.List;

public class UserApplicationBackendServiceRoleProvider extends StorableObjectProvider<UserApplicationBackendServiceRole,
        UserApplicationBackendServiceRoleId, UserApplicationBackendServiceRoleRepository> {

    public UserApplicationBackendServiceRoleProvider(UserApplicationBackendServiceRoleRepository repository) {
        super(repository);
    }

    public List<UserApplicationBackendServiceRole> findByUserId(Long userId) {
        return getRepository().findByIdUserId(userId);
    }

    public List<UserApplicationBackendServiceRole> findByApplicationId(Long applicationId) {
        return getRepository().findByIdApplicationId(applicationId);
    }

    public List<UserApplicationBackendServiceRole> findByBackendServiceId(Long backendServiceId) {
        return getRepository().findByIdBackendServiceId(backendServiceId);
    }

    public List<UserApplicationBackendServiceRole> findByBackendServiceRole(String roleName) {
        return getRepository().findByIdBackendServiceRole(roleName);
    }
}

package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRoleId;

import java.util.List;

public interface UserApplicationBackendServiceRoleRepository extends StorableObjectRepository<UserApplicationBackendServiceRole,
        UserApplicationBackendServiceRoleId> {

    List<UserApplicationBackendServiceRole> findByIdUserId(Long userId);

    List<UserApplicationBackendServiceRole> findByIdApplicationName(String applicationName);

    List<UserApplicationBackendServiceRole> findByIdRoleName(String roleName);

    List<UserApplicationBackendServiceRole> findByIdBackendServiceName(String backendServiceName);

    List<UserApplicationBackendServiceRole> findByIdBackendServiceRole(String roleName);
}

package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRoleId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserApplicationBackendServiceRoleRepository extends StorableObjectRepository<UserApplicationBackendServiceRole,
        UserApplicationBackendServiceRoleId> {

    Set<UserApplicationBackendServiceRole> findByIdUserId(Long userId);

    Set<UserApplicationBackendServiceRole> findByIdApplicationName(String applicationName);

    Set<UserApplicationBackendServiceRole> findByIdRoleName(String applicationRoleName);

    Set<UserApplicationBackendServiceRole> findByIdBackendServiceName(String backendServiceName);

    Set<UserApplicationBackendServiceRole> findByIdBackendServiceRole(String backendServiceRoleName);

    Optional<UserApplicationBackendServiceRole> findByIdUserIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            Long userId, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);

    List<UserApplicationBackendServiceRole> findByIdUserIdAndIdApplicationNameAndIdRoleName(Long userId, String applicationName, String applicationRoleName);
}

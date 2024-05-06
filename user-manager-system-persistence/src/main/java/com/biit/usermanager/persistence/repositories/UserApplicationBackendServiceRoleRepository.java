package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRoleId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserApplicationBackendServiceRoleRepository extends StorableObjectRepository<UserApplicationBackendServiceRole,
        UserApplicationBackendServiceRoleId> {

    Set<UserApplicationBackendServiceRole> findByIdUserId(Long userId);

    Set<UserApplicationBackendServiceRole> findByIdUserIdAndIdApplicationName(Long userId, String applicationName);

    Set<UserApplicationBackendServiceRole> findByIdApplicationName(String applicationName);

    void deleteByIdApplicationName(String applicationName);

    void deleteByIdApplicationNameIn(Collection<String> applicationName);

    Set<UserApplicationBackendServiceRole> findByIdRoleName(String applicationRoleName);

    Set<UserApplicationBackendServiceRole> findByIdApplicationNameAndIdRoleName(String applicationName, String applicationRoleName);

    void deleteByIdRoleName(String applicationRoleName);

    void deleteByIdRoleNameIn(Collection<String> applicationRoleName);

    Set<UserApplicationBackendServiceRole> findByIdBackendServiceName(String backendServiceName);

    Set<UserApplicationBackendServiceRole> findByIdBackendServiceRole(String backendServiceRoleName);

    Set<UserApplicationBackendServiceRole> findByIdBackendServiceNameAndIdBackendServiceRole(String backendServiceName, String backendServiceRoleName);

    void deleteByIdBackendServiceNameAndIdBackendServiceRole(String backendServiceName, String backendServiceRoleName);

    void deleteByIdApplicationNameAndIdRoleName(String applicationName, String roleName);

    Set<UserApplicationBackendServiceRole> deleteByIdBackendServiceRoleIn(Collection<String> backendServiceRoleName);

    void deleteByIdBackendServiceName(String backendServiceName);

    void deleteByIdBackendServiceNameIn(Collection<String> backendServiceName);

    Optional<UserApplicationBackendServiceRole> findByIdUserIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            Long userId, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);

    Set<UserApplicationBackendServiceRole> findByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);

    void deleteByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);


    List<UserApplicationBackendServiceRole> findByIdUserIdAndIdApplicationNameAndIdRoleName(Long userId, String applicationName, String applicationRoleName);
}

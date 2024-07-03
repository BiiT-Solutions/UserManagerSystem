package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRoleId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface UserGroupApplicationBackendServiceRoleRepository extends StorableObjectRepository<UserGroupApplicationBackendServiceRole,
        UserGroupApplicationBackendServiceRoleId> {

    Set<UserGroupApplicationBackendServiceRole> findByIdUserGroupId(Long userGroupId);

    Set<UserGroupApplicationBackendServiceRole> findByIdUserGroupIdIn(Collection<Long> userGroupId);

    Set<UserGroupApplicationBackendServiceRole> findByIdApplicationName(String applicationName);

    void deleteByIdApplicationName(String applicationName);

    void deleteByIdApplicationNameIn(Collection<String> applicationName);

    Set<UserGroupApplicationBackendServiceRole> findByIdRoleName(String applicationRoleName);

    Set<UserGroupApplicationBackendServiceRole> findByIdApplicationNameAndIdRoleName(String applicationName, String applicationRoleName);

    void deleteByIdRoleName(String applicationRoleName);

    void deleteByIdRoleNameIn(Collection<String> applicationRoleName);

    Set<UserGroupApplicationBackendServiceRole> findByIdBackendServiceName(String backendServiceName);

    Set<UserGroupApplicationBackendServiceRole> findByIdBackendServiceRole(String backendServiceRoleName);

    Set<UserGroupApplicationBackendServiceRole> findByIdBackendServiceNameAndIdBackendServiceRole(String backendServiceName, String backendServiceRoleName);

    void deleteByIdBackendServiceNameAndIdBackendServiceRole(String backendServiceName, String backendServiceRoleName);

    void deleteByIdApplicationNameAndIdRoleName(String applicationName, String roleName);

    Set<UserGroupApplicationBackendServiceRole> deleteByIdBackendServiceRoleIn(Collection<String> backendServiceRoleName);

    void deleteByIdBackendServiceName(String backendServiceName);

    void deleteByIdBackendServiceNameIn(Collection<String> backendServiceName);

    Optional<UserGroupApplicationBackendServiceRole> findByIdUserGroupIdAndIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            Long userGroupId, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);

    Set<UserGroupApplicationBackendServiceRole> findByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);

    void deleteByIdApplicationNameAndIdRoleNameAndIdBackendServiceNameAndIdBackendServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);


    List<UserGroupApplicationBackendServiceRole> findByIdUserGroupIdAndIdApplicationNameAndIdRoleName(Long userGroupId, String applicationName,
                                                                                                      String applicationRoleName);
}

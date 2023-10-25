package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.StorableObjectRepository;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRoleId;

import java.util.List;

public interface UserApplicationBackendServiceRoleRepository extends StorableObjectRepository<UserApplicationBackendServiceRole, UserApplicationBackendServiceRoleId> {

    List<UserApplicationBackendServiceRole> findByIdUserId(Long userId);

    List<UserApplicationBackendServiceRole> findByIdApplicationId(Long applicationId);

    List<UserApplicationBackendServiceRole> findByIdBackendServiceId(Long roleId);

    List<UserApplicationBackendServiceRole> findByIdBackendServiceRole(String roleName);
}

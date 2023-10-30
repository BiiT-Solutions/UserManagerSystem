package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.CreatedElementRepository;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ApplicationBackendServiceRoleRepository extends CreatedElementRepository<ApplicationBackendServiceRole, ApplicationBackendServiceRoleId> {

    Optional<ApplicationBackendServiceRole> findByIdApplicationRoleAndIdBackendServiceRole(
            ApplicationRole applicationRole, BackendServiceRole backendServiceRole);

    Optional<ApplicationBackendServiceRole>
    findByIdApplicationRoleIdApplicationIdAndIdApplicationRoleIdRoleIdAndIdBackendServiceRoleIdBackendServiceIdAndIdBackendServiceRoleIdName(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName);

    List<ApplicationBackendServiceRole> findByIdApplicationRoleIdApplicationIdAndIdApplicationRoleIdRoleId(
            String applicationName, String applicationRoleName);

    List<ApplicationBackendServiceRole> findByIdApplicationRole(ApplicationRole applicationRole);

    List<ApplicationBackendServiceRole> findByIdBackendServiceRole(BackendServiceRole backendServiceRole);

}

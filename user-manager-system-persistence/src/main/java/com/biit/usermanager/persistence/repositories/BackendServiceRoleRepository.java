package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.CreatedElementRepository;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BackendServiceRoleRepository extends CreatedElementRepository<BackendServiceRole, BackendServiceRoleId> {

    List<BackendServiceRole> findByIdBackendService(BackendService backendService);

    List<BackendServiceRole> findByIdName(String name);

    Optional<BackendServiceRole> findByIdBackendServiceAndIdName(BackendService backendService, String name);

    Optional<BackendServiceRole> findByIdBackendServiceIdAndIdName(String backendServiceName, String name);
}

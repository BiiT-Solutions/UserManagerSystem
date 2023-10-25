package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.CreatedElementRepository;
import com.biit.usermanager.persistence.entities.Service;
import com.biit.usermanager.persistence.entities.ServiceRole;
import com.biit.usermanager.persistence.entities.ServiceRoleId;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ServiceRoleRepository extends CreatedElementRepository<ServiceRole, ServiceRoleId> {

    List<ServiceRole> findByIdService(Service service);

    List<ServiceRole> findByIdName(String name);

    Optional<ServiceRole> findByIdServiceAndIdName(Service service, String name);
}

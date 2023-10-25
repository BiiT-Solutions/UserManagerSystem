package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.CreatedElementRepository;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationServiceRoleId;
import com.biit.usermanager.persistence.entities.ServiceRole;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ApplicationServiceRoleRepository extends CreatedElementRepository<ApplicationServiceRole, ApplicationServiceRoleId> {

    Optional<ApplicationServiceRole> findByIdApplicationRoleAndIdServiceRole(ApplicationRole applicationRole, ServiceRole serviceRole);

    List<ApplicationServiceRole> findByIdApplicationRole(ApplicationRole applicationRole);

    List<ApplicationServiceRole> findByIdServiceRole(ServiceRole serviceRole);

}

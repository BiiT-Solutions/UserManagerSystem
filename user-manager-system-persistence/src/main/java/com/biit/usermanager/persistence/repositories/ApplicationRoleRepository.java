package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.CreatedElementRepository;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import com.biit.usermanager.persistence.entities.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ApplicationRoleRepository extends CreatedElementRepository<ApplicationRole, ApplicationRoleId> {

    List<ApplicationRole> findByIdApplication(Application application);

    List<ApplicationRole> findByIdApplicationId(Long applicationId);

    List<ApplicationRole> findByIdRole(Role role);
}

package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.CreatedElementRepository;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import com.biit.usermanager.persistence.entities.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ApplicationRoleRepository extends CreatedElementRepository<ApplicationRole, ApplicationRoleId> {

    List<ApplicationRole> findByIdApplication(Application application);

    List<ApplicationRole> findByIdApplicationIn(Collection<Application> application);

    List<ApplicationRole> findByIdApplicationId(String applicationId);

    List<ApplicationRole> findByIdRole(Role role);

    List<ApplicationRole> findByIdRoleIn(Collection<Role> role);

    List<ApplicationRole> findByIdRoleId(String roleId);

    Optional<ApplicationRole> findByIdApplicationIdAndIdRoleId(String applicationId, String roleId);
}

package com.biit.usermanager.core.providers;

import com.biit.server.providers.CreatedElementProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationRoleProvider extends CreatedElementProvider<ApplicationRole, ApplicationRoleId, ApplicationRoleRepository> {

    @Autowired
    public ApplicationRoleProvider(ApplicationRoleRepository repository) {
        super(repository);
    }

    public List<ApplicationRole> findByApplication(Application application) {
        return getRepository().findByIdApplication(application);
    }

    public List<ApplicationRole> findByApplicationId(String applicationId) {
        return getRepository().findByIdApplicationId(applicationId);
    }

    public Optional<ApplicationRole> findByApplicationIdAndRoleId(String applicationId, String roleId) {
        return getRepository().findByIdApplicationIdAndIdRoleId(applicationId, roleId);
    }

    public List<ApplicationRole> findByRole(Role role) {
        return getRepository().findByIdRole(role);
    }

    public List<ApplicationRole> findByRoleId(String roleId) {
        return getRepository().findByIdRoleId(roleId);
    }
}

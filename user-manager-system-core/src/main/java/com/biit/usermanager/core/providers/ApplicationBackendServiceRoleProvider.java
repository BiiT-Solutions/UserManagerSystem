package com.biit.usermanager.core.providers;

import com.biit.server.providers.CreatedElementProvider;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationBackendServiceRoleProvider extends CreatedElementProvider<ApplicationBackendServiceRole,
        ApplicationBackendServiceRoleId, ApplicationBackendServiceRoleRepository> {

    @Autowired
    public ApplicationBackendServiceRoleProvider(ApplicationBackendServiceRoleRepository repository) {
        super(repository);
    }

    public Optional<ApplicationBackendServiceRole> findByApplicationRoleAndServiceRole(ApplicationRole applicationRole, BackendServiceRole backendServiceRole) {
        return getRepository().findByIdApplicationRoleAndIdBackendServiceRole(applicationRole, backendServiceRole);
    }

    public Optional<ApplicationBackendServiceRole> findByApplicationRoleAndServiceRole(
            String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        return getRepository()
                .findByIdApplicationRoleIdApplicationIdAndIdApplicationRoleIdRoleIdAndIdBackendServiceRoleIdBackendServiceIdAndIdBackendServiceRoleIdName(
                        applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    public List<ApplicationBackendServiceRole> findByApplicationNameAndApplicationRole(String applicationName, String applicationRoleName) {
        return getRepository().findByIdApplicationRoleIdApplicationIdAndIdApplicationRoleIdRoleId(applicationName, applicationRoleName);
    }

    public List<ApplicationBackendServiceRole> findByApplicationRole(ApplicationRole applicationRole) {
        return getRepository().findByIdApplicationRole(applicationRole);
    }

    public List<ApplicationBackendServiceRole> findByServiceRole(BackendServiceRole backendServiceRole) {
        return getRepository().findByIdBackendServiceRole(backendServiceRole);
    }
}

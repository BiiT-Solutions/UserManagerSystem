package com.biit.usermanager.core.providers;

import com.biit.server.providers.CreatedElementProvider;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.BackendServiceRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BackendServiceRoleProvider extends CreatedElementProvider<BackendServiceRole, BackendServiceRoleId, BackendServiceRoleRepository> {

    public BackendServiceRoleProvider(BackendServiceRoleRepository repository) {
        super(repository);
    }

    public List<BackendServiceRole> findByService(BackendService backendService) {
        return getRepository().findByIdBackendService(backendService);
    }

    public List<BackendServiceRole> findByName(String name) {
        return getRepository().findByIdName(name);
    }

    public Optional<BackendServiceRole> findByServiceAndName(BackendService backendService, String name) {
        return getRepository().findByIdBackendServiceAndIdName(backendService, name);
    }

}

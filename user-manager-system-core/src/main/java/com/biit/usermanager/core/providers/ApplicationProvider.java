package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationProvider extends ElementProvider<Application, String, ApplicationRepository> {

    private final ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository;

    private final ApplicationRoleRepository applicationRoleRepository;

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    @Autowired
    public ApplicationProvider(ApplicationRepository repository,
                               ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository,
                               ApplicationRoleRepository applicationRoleRepository,
                               UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository) {
        super(repository);
        this.applicationBackendServiceRoleRepository = applicationBackendServiceRoleRepository;
        this.applicationRoleRepository = applicationRoleRepository;
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
    }

    public Optional<Application> findByName(String name) {
        return getRepository().findById(name);
    }

    @Override
    public void delete(Application entity) {
        userApplicationBackendServiceRoleRepository.deleteByIdApplicationName(entity.getName());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdApplication(entity);
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.delete(entity);
    }

    @Override
    public void deleteAll(Collection<Application> entities) {
        userApplicationBackendServiceRoleRepository.deleteByIdApplicationNameIn(entities.stream().map(Application::getName).toList());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdApplicationIn(entities);
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.deleteAll(entities);
    }
}

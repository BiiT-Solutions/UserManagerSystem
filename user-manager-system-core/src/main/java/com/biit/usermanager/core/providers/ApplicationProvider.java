package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public void delete(Application entity) {
        if (entity == null) {
            return;
        }
        userApplicationBackendServiceRoleRepository.deleteByIdApplicationName(entity.getName());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdApplication(entity);
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll(Collection<Application> entities) {
        if (entities == null) {
            return;
        }
        userApplicationBackendServiceRoleRepository.deleteByIdApplicationNameIn(entities.stream().map(Application::getName).toList());
        final List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdApplicationIn(entities);
        applicationBackendServiceRoleRepository.deleteByIdApplicationRoleIn(applicationRoles);
        applicationRoleRepository.deleteAll(applicationRoles);
        super.deleteAll(entities);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        delete(getRepository().findById(id).orElse(null));
    }
}

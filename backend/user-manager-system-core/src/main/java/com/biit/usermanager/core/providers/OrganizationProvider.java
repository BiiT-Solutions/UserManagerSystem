package com.biit.usermanager.core.providers;

import com.biit.server.providers.CrudProvider;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationProvider extends CrudProvider<Organization, Long, OrganizationRepository> {

    @Autowired
    public OrganizationProvider(OrganizationRepository repository) {
        super(repository);
    }

    public Optional<Organization> findByName(String name) {
        return repository.findByName(name);
    }

    public int deleteByName(String name) {
        return repository.deleteByName(name);
    }
}

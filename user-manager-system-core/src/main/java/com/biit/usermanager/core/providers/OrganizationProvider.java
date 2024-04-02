package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.Team;
import com.biit.usermanager.persistence.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationProvider extends ElementProvider<Organization, String, OrganizationRepository> {

    @Autowired
    public OrganizationProvider(OrganizationRepository repository) {
        super(repository);
    }

    public Optional<Organization> findByName(String name) {
        return getRepository().findById(name);
    }

    public Optional<Organization> findByTeam(Team team) {
        return getRepository().findByTeam(team.getId());
    }
}

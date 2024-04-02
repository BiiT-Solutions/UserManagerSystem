package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.Team;
import com.biit.usermanager.persistence.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamProvider extends ElementProvider<Team, Long, TeamRepository> {

    @Autowired
    public TeamProvider(TeamRepository repository) {
        super(repository);
    }

    public List<Team> findByParent(Team parent) {
        return getRepository().findByParent(parent);
    }

    public List<Team> findByParentIsNull() {
        return getRepository().findByParentIsNull();
    }

    public List<Team> findByParentIsNotNull() {
        return getRepository().findByParentIsNotNull();
    }

    public List<Team> findByOrganization(Organization organization) {
        return getRepository().findByOrganization(organization);
    }

    public Optional<Team> findByNameAndOrganization(String name, Organization organization) {
        return getRepository().findByNameAndOrganization(name, organization);
    }

    public int deleteByName(String name, Organization organization) {
        return getRepository().deleteByNameAndOrganization(name, organization);
    }
}

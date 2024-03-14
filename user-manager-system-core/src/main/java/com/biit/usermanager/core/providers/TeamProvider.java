package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.Application;
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

    public List<Team> findByParentIsNull() {
        return getRepository().findByParentIsNull();
    }

    public List<Team> findByParentIsNotNull() {
        return getRepository().findByParentIsNotNull();
    }

    public List<Team> findByApplication(Application application) {
        return getRepository().findByApplication(application);
    }

    public Optional<Team> findByNameAndApplication(String name, Application application) {
        return getRepository().findByNameAndApplication(name, application);
    }

    public int deleteByName(String name, Application application) {
        return getRepository().deleteByNameAndApplication(name, application);
    }
}

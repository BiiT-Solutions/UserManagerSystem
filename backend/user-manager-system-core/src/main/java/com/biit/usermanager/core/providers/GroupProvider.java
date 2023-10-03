package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Group;
import com.biit.usermanager.persistence.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupProvider extends ElementProvider<Group, Long, GroupRepository> {

    @Autowired
    public GroupProvider(GroupRepository repository) {
        super(repository);
    }

    public List<Group> findByParentIsNull() {
        return getRepository().findByParentIsNull();
    }

    public List<Group> findByParentIsNotNull() {
        return getRepository().findByParentIsNotNull();
    }

    public List<Group> findByApplication(Application application) {
        return getRepository().findByApplication(application);
    }

    public Optional<Group> findByNameAndApplication(String name, Application application) {
        return getRepository().findByNameAndApplication(name, application);
    }

    public int deleteByName(String name, Application application) {
        return getRepository().deleteByNameAndApplication(name, application);
    }
}

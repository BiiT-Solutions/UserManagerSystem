package com.biit.usermanager.core.providers;

import com.biit.server.providers.CrudProvider;
import com.biit.usermanager.persistence.entities.Group;
import com.biit.usermanager.persistence.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupProvider extends CrudProvider<Group, Long, GroupRepository> {

    @Autowired
    public GroupProvider(GroupRepository repository) {
        super(repository);
    }

    public Optional<Group> findByName(String name) {
        return getRepository().findByName(name);
    }

    public List<Group> findByParentIsNull() {
        return getRepository().findByParentIsNull();
    }

    public List<Group> findByParentIsNotNull() {
        return getRepository().findByParentIsNotNull();
    }

    public int deleteByName(String name) {
        return getRepository().deleteByName(name);
    }
}

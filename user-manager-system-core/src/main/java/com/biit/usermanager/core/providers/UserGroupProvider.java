package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.UserGroup;
import com.biit.usermanager.persistence.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserGroupProvider extends ElementProvider<UserGroup, Long, UserGroupRepository> {

    @Autowired
    public UserGroupProvider(UserGroupRepository repository) {
        super(repository);
    }

    public Optional<UserGroup> findByName(String name) {
        return getRepository().findByName(name);
    }

    public long deleteByName(String name) {
        return getRepository().deleteByName(name);
    }

}

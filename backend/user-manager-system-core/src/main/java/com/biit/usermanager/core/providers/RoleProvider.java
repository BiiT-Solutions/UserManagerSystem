package com.biit.usermanager.core.providers;

import com.biit.server.providers.CrudProvider;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleProvider extends CrudProvider<Role, Long, RoleRepository> {

    @Autowired
    public RoleProvider(RoleRepository repository) {
        super(repository);
    }

    public Optional<Role> findByName(String name) {
        return getRepository().findByName(name);
    }
}

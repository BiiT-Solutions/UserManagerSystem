package com.biit.usermanager.core.providers;

import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleProvider extends CrudProvider<UserRole, Long, UserRoleRepository> {

    @Autowired
    public UserRoleProvider(UserRoleRepository repository) {
        super(repository);
    }
}

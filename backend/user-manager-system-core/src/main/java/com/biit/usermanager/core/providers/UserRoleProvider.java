package com.biit.usermanager.core.providers;

import com.biit.server.converters.providers.CrudProvider;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleProvider extends CrudProvider<UserRole, Long, UserRoleRepository> {

    @Autowired
    public UserRoleProvider(UserRoleRepository repository) {
        super(repository);
    }

    public List<UserRole> findByUserAndOrganization(User user, Organization organization) {
        return repository.findByUserAndOrganization(user, organization);
    }

}

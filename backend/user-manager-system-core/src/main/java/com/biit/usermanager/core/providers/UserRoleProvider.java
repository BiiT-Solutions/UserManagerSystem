package com.biit.usermanager.core.providers;

import com.biit.server.providers.CrudProvider;
import com.biit.usermanager.persistence.entities.*;
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

    public List<UserRole> findByUserAndGroupAndApplication(User user, Group group, Application application) {
        if (group != null) {
            if (application != null) {
                return repository.findByUserAndGroupAndApplication(user, group, application);
            } else {
                return repository.findByUserAndGroup(user, group);
            }
        } else {
            if (application != null) {
                return repository.findByUserAndApplication(user, application);
            } else {
                return repository.findByUser(user);
            }
        }
//        return repository.findByUserAndGroupAndApplication(user, group, application);
    }

    public List<UserRole> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<UserRole> findByGroup(Group group) {
        return repository.findByGroup(group);
    }

    public List<UserRole> findByGroupAndRole(Group group, Role role) {
        return repository.findByGroupAndRole(group, role);
    }


}

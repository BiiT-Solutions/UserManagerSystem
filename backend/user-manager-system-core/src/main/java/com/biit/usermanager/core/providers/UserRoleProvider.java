package com.biit.usermanager.core.providers;

import com.biit.server.providers.CrudProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Group;
import com.biit.usermanager.persistence.entities.Role;
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

    public List<UserRole> findByUserAndGroupAndApplication(User user, Group group, Application application) {
        if (group != null) {
            if (application != null) {
                return getRepository().findByUserAndGroupAndApplication(user, group, application);
            } else {
                return getRepository().findByUserAndGroup(user, group);
            }
        } else {
            if (application != null) {
                return getRepository().findByUserAndApplication(user, application);
            } else {
                return getRepository().findByUser(user);
            }
        }
    }

    public List<UserRole> findByUser(User user) {
        return getRepository().findByUser(user);
    }

    public List<UserRole> findByGroup(Group group) {
        return getRepository().findByGroup(group);
    }

    public List<UserRole> findByGroupAndRole(Group group, Role role) {
        return getRepository().findByGroupAndRole(group, role);
    }


}

package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.Group;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserRoleProvider extends ElementProvider<UserRole, Long, UserRoleRepository> {

    @Autowired
    public UserRoleProvider(UserRoleRepository repository) {
        super(repository);
    }

    public List<UserRole> findByUserAndGroup(User user, Group group) {
        return getRepository().findByUserAndGroup(user, group);
    }

    public List<UserRole> findByUserAndGroupIn(User user, Collection<Group> group) {
        return getRepository().findByUserAndGroupIn(user, group);
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

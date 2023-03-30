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

    public List<UserRole> findByUserAndOrganizationAndApplication(User user, Organization organization, Application application) {
        if (organization != null) {
            if (application != null) {
                return repository.findByUserAndOrganizationAndApplication(user, organization, application);
            } else {
                return repository.findByUserAndOrganization(user, organization);
            }
        } else {
            if (application != null) {
                return repository.findByUserAndApplication(user, application);
            } else {
                return repository.findByUser(user);
            }
        }
//        return repository.findByUserAndOrganizationAndApplication(user, organization, application);
    }

    public List<UserRole> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<UserRole> findByOrganization(Organization organization) {
        return repository.findByOrganization(organization);
    }

    public List<UserRole> findByOrganizationAndRole(Organization organization, Role role) {
        return repository.findByOrganizationAndRole(organization, role);
    }


}

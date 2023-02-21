package com.biit.usermanager.core.providers;

import com.biit.server.providers.CrudProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
                return Collections.singletonList(repository.findByUser(user));
            }
        }
    }

}

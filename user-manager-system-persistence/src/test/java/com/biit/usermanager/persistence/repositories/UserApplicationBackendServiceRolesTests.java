package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@Test(groups = {"userApplicationBackendServiceRoles"})
public class UserApplicationBackendServiceRolesTests extends AbstractTestNGSpringContextTests {

    private static final String APPLICATION_NAME = "TheApp";

    private static final String USER_NAME = "admin";
    private static final String USER_FIRST_NAME = "Test";
    private static final String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";

    private static final String[] APPLICATION_ROLES = new String[]{"ADMIN", "VIEWER"};
    private static final String[] BACKEND_ROLES = new String[]{"WRITER", "READER"};
    private static final String BACKEND_SERVICE = "DATABASE";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApplicationRoleRepository applicationRoleRepository;

    @Autowired
    private BackendServiceRepository backendServiceRepository;

    @Autowired
    private BackendServiceRoleRepository backendServiceRoleRepository;

    @Autowired
    private ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository;

    @Autowired
    private UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    private User admin;
    private Application application;

    @BeforeClass
    public void setDefaultData() {
        //Create the admin user
        admin = userRepository.save(new User(USER_NAME, USER_FIRST_NAME, USER_LAST_NAME, USER_PASSWORD));

        //Create the application
        application = applicationRepository.save(new Application(APPLICATION_NAME));

        //Set the application roles
        final List<Role> roles = new ArrayList<>();
        for (final String roleName : APPLICATION_ROLES) {
            roles.add(roleRepository.save(new Role(roleName)));
        }

        //Assign the application roles.
        final List<ApplicationRole> applicationRoles = new ArrayList<>();
        roles.forEach(role -> applicationRoles.add(applicationRoleRepository.save(new ApplicationRole(application, role))));

        //Set the backend roles.
        final BackendService backendService = backendServiceRepository.save(new BackendService(BACKEND_SERVICE));

        final List<BackendServiceRole> backendRoles = new ArrayList<>();
        for (final String roleName : BACKEND_ROLES) {
            backendRoles.add(backendServiceRoleRepository.save(new BackendServiceRole(backendService, roleName)));
        }

        //Assign the backend to an application.
        final Set<ApplicationBackendServiceRole> applicationBackendServiceRoleDTOs = new HashSet<>();
        for (ApplicationRole applicationRole : applicationRoles) {
            for (BackendServiceRole backendRole : backendRoles) {
                applicationBackendServiceRoleDTOs.add(applicationBackendServiceRoleRepository.save(new ApplicationBackendServiceRole(applicationRole, backendRole)));
            }
        }

        admin.setApplicationBackendServiceRoles(applicationBackendServiceRoleDTOs);
        userRepository.save(admin);
    }

    @Test
    public void loadApplicationBackendServiceRoles() {
        List<UserApplicationBackendServiceRole> userApplicationBackendServiceRoles = userApplicationBackendServiceRoleRepository.findAll();
        Assert.assertEquals(userApplicationBackendServiceRoles.size(), BACKEND_ROLES.length * APPLICATION_ROLES.length);
        Assert.assertEquals(userApplicationBackendServiceRoles.get(0).getId().getUserId(), admin.getId());
        Assert.assertEquals(userApplicationBackendServiceRoles.get(0).getId().getApplicationName(), application.getId());
    }

    @AfterClass
    public void wipeOut() {
        userApplicationBackendServiceRoleRepository.deleteAll();
        applicationBackendServiceRoleRepository.deleteAll();
        backendServiceRoleRepository.deleteAll();
        backendServiceRoleRepository.deleteAll();
        applicationRoleRepository.deleteAll();
        roleRepository.deleteAll();
        applicationRepository.deleteAll();
        userRepository.deleteAll();
    }

}

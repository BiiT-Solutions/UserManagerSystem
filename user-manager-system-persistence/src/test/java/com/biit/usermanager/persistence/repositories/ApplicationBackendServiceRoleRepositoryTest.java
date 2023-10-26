package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.*;
import com.biit.usermanager.persistence.entities.BackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Test(groups = {"applicationServiceRoleRepository"})
public class ApplicationBackendServiceRoleRepositoryTest extends AbstractTestNGSpringContextTests {

    private static final String ROLE_NAME = "AdminFrontend";
    private static final String APPLICATION_NAME = "Frontend";
    private static final String SERVICE_ROLE_NAME = "AdminBackend";
    private static final String SERVICE_NAME = "Backend";

    @Autowired
    private BackendServiceRepository backendServiceRepository;

    @Autowired
    private BackendServiceRoleRepository backendServiceRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationRoleRepository applicationRoleRepository;

    @Autowired
    private ApplicationBackendServiceRoleRepository applicationBackendServiceRoleRepository;

    private ApplicationRole applicationRole;
    private BackendServiceRole backendServiceRole;

    @BeforeClass
    public void prepareData() {
        Role role = new Role(ROLE_NAME);
        role = roleRepository.save(role);
        Assert.assertNotNull(role.getId());

        Application application = new Application(APPLICATION_NAME);
        application = applicationRepository.save(application);
        Assert.assertNotNull(application.getId());

        applicationRole = new ApplicationRole(application, role);
        applicationRole = applicationRoleRepository.save(applicationRole);
        Assert.assertNotNull(applicationRole.getId());

        BackendService backendService = new BackendService(SERVICE_NAME);
        backendService = backendServiceRepository.save(backendService);
        Assert.assertNotNull(backendService.getId());

        backendServiceRole = new BackendServiceRole(backendService, SERVICE_ROLE_NAME);
        backendServiceRole = backendServiceRoleRepository.save(backendServiceRole);
        Assert.assertNotNull(backendServiceRole.getId());
    }

    @Test
    public void saveApplicationServiceRole() {
        ApplicationBackendServiceRole applicationBackendServiceRole = new ApplicationBackendServiceRole(applicationRole, backendServiceRole);
        applicationBackendServiceRole = applicationBackendServiceRoleRepository.save(applicationBackendServiceRole);
        Assert.assertNotNull(applicationBackendServiceRole.getId());
    }

    @Test(dependsOnMethods = "saveApplicationServiceRole")
    public void findApplicationServiceRoleByServiceRole() {
        List<ApplicationBackendServiceRole> applicationBackendServiceRole = applicationBackendServiceRoleRepository.findByIdBackendServiceRole(backendServiceRole);
        Assert.assertEquals(applicationBackendServiceRole.size(), 1);
    }

    @Test(dependsOnMethods = "saveApplicationServiceRole")
    public void findApplicationServiceRoleByApplicationRole() {
        List<ApplicationBackendServiceRole> applicationBackendServiceRole = applicationBackendServiceRoleRepository.findByIdApplicationRole(applicationRole);
        Assert.assertEquals(applicationBackendServiceRole.size(), 1);
    }

    @Test(dependsOnMethods = "saveApplicationServiceRole")
    public void findApplicationServiceRole() {
        Optional<ApplicationBackendServiceRole> applicationServiceRole = applicationBackendServiceRoleRepository.findByIdApplicationRoleAndIdBackendServiceRole(applicationRole, backendServiceRole);
        Assert.assertTrue(applicationServiceRole.isPresent());
    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        applicationBackendServiceRoleRepository.deleteAll();
        applicationRoleRepository.deleteAll();
        applicationRepository.deleteAll();
        roleRepository.deleteAll();
        backendServiceRoleRepository.deleteAll();
        backendServiceRepository.deleteAll();
        Assert.assertEquals(backendServiceRoleRepository.count(), 0);
        Assert.assertEquals(backendServiceRepository.count(), 0);
    }

}

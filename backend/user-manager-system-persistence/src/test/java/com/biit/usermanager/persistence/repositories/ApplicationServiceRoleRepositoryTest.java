package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationServiceRole;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.Service;
import com.biit.usermanager.persistence.entities.ServiceRole;
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
public class ApplicationServiceRoleRepositoryTest extends AbstractTestNGSpringContextTests {

    private static final String ROLE_NAME = "AdminFrontend";
    private static final String APPLICATION_NAME = "Frontend";
    private static final String SERVICE_ROLE_NAME = "AdminBackend";
    private static final String SERVICE_NAME = "Backend";

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceRoleRepository serviceRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationRoleRepository applicationRoleRepository;

    @Autowired
    private ApplicationServiceRoleRepository applicationServiceRoleRepository;

    private ApplicationRole applicationRole;
    private ServiceRole serviceRole;

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

        Service service = new Service(SERVICE_NAME);
        Assert.assertNull(service.getId());
        service = serviceRepository.save(service);
        Assert.assertNotNull(service.getId());

        serviceRole = new ServiceRole(service, SERVICE_ROLE_NAME);
        serviceRole = serviceRoleRepository.save(serviceRole);
        Assert.assertNotNull(serviceRole.getId());
    }

    @Test
    public void saveApplicationServiceRole() {
        ApplicationServiceRole applicationServiceRole = new ApplicationServiceRole(applicationRole, serviceRole);
        applicationServiceRole = applicationServiceRoleRepository.save(applicationServiceRole);
        Assert.assertNotNull(applicationServiceRole.getId());
    }

    @Test(dependsOnMethods = "saveApplicationServiceRole")
    public void findApplicationServiceRoleByServiceRole() {
        List<ApplicationServiceRole> applicationServiceRole = applicationServiceRoleRepository.findByIdServiceRole(serviceRole);
        Assert.assertEquals(applicationServiceRole.size(), 1);
    }

    @Test(dependsOnMethods = "saveApplicationServiceRole")
    public void findApplicationServiceRoleByApplicationRole() {
        List<ApplicationServiceRole> applicationServiceRole = applicationServiceRoleRepository.findByIdApplicationRole(applicationRole);
        Assert.assertEquals(applicationServiceRole.size(), 1);
    }

    @Test(dependsOnMethods = "saveApplicationServiceRole")
    public void findApplicationServiceRole() {
        Optional<ApplicationServiceRole> applicationServiceRole = applicationServiceRoleRepository.findByIdApplicationRoleAndIdServiceRole(applicationRole, serviceRole);
        Assert.assertTrue(applicationServiceRole.isPresent());
    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        applicationServiceRoleRepository.deleteAll();
        applicationRoleRepository.deleteAll();
        applicationRepository.deleteAll();
        roleRepository.deleteAll();
        serviceRoleRepository.deleteAll();
        serviceRepository.deleteAll();
        Assert.assertEquals(serviceRoleRepository.count(), 0);
        Assert.assertEquals(serviceRepository.count(), 0);
    }

}

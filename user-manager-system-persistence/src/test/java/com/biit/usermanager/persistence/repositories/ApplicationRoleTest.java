package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

@SpringBootTest
@Test(groups = {"applicationRole"})
public class ApplicationRoleTest extends AbstractTestNGSpringContextTests {

    private static final String ROLE_NAME = "Admin";
    private static final String APPLICATION_NAME = "Frontend";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApplicationRoleRepository applicationRoleRepository;

    private Role role;
    private Application application;
    private ApplicationRole applicationRole;

    @BeforeClass
    public void prepareData() {
        role = new Role(ROLE_NAME);
        role = roleRepository.save(role);
        Assert.assertNotNull(role.getId());

        application = new Application(APPLICATION_NAME);
        application = applicationRepository.save(application);
        Assert.assertNotNull(application.getId());
    }


    @Test
    public void saveApplicationRoles() {
        applicationRole = new ApplicationRole(application, role);
        applicationRole = applicationRoleRepository.save(applicationRole);
        Assert.assertNotNull(applicationRole.getId());

        Role role2 = new Role(ROLE_NAME + "_2");
        role2 = roleRepository.save(role2);
        Assert.assertNotNull(role2.getId());

        ApplicationRole applicationRole2 = new ApplicationRole(application, role2);
        applicationRole = applicationRoleRepository.save(applicationRole);
        Assert.assertNotNull(applicationRole.getId());

        Application application2 = new Application(APPLICATION_NAME + "_2");
        application2 = applicationRepository.save(application2);
        Assert.assertNotNull(application2.getId());

        ApplicationRole applicationRole3 = new ApplicationRole(application2, role);
        applicationRole3 = applicationRoleRepository.save(applicationRole3);
        Assert.assertNotNull(applicationRole3.getId());
    }


    @Test(dependsOnMethods = "saveApplicationRoles")
    public void getApplicationRoleByApplication() {
        List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdApplication(application);
        Assert.assertFalse(applicationRoles.isEmpty());
        Assert.assertEquals(applicationRoles.get(0).getId().getRole().getName(), ROLE_NAME);
        Assert.assertEquals(applicationRoles.get(0).getId().getApplication().getName(), APPLICATION_NAME);
    }

    @Test(dependsOnMethods = "saveApplicationRoles")
    public void getApplicationRoleByApplicationId() {
        List<ApplicationRole> applicationRoles = applicationRoleRepository.findByIdApplicationId(application.getId());
        Assert.assertFalse(applicationRoles.isEmpty());
        Assert.assertEquals(applicationRoles.get(0).getId().getRole().getName(), ROLE_NAME);
        Assert.assertEquals(applicationRoles.get(0).getId().getApplication().getName(), APPLICATION_NAME);
    }


    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        applicationRoleRepository.deleteAll();
        applicationRepository.deleteAll();
        roleRepository.deleteAll();
        Assert.assertEquals(roleRepository.count(), 0);
        Assert.assertEquals(applicationRepository.count(), 0);
        Assert.assertEquals(applicationRoleRepository.count(), 0);
    }


}

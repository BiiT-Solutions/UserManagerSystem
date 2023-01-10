package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

@SpringBootTest
@Test(groups = {"userRepository"})
public class UserRoleTest extends AbstractTestNGSpringContextTests {

    private static final String ROLE_NAME = "Backend";

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    private Application application;

    @BeforeClass
    public void prepare() {
        Application roleApplication = new Application();
        roleApplication.setName("My Application");
        application = applicationRepository.save(roleApplication);
    }

    @Test
    public void saveRole() {
        Role role = new Role();
        role.setName(ROLE_NAME);
        role.setApplication(application);
        Assert.assertNull(role.getId());
        role = roleRepository.save(role);
        Assert.assertNotNull(role.getId());
    }

    @Test(dependsOnMethods = "saveRole")
    public void getRoleByName() {
        Optional<Role> role = roleRepository.findByName(ROLE_NAME);
        Assert.assertTrue(role.isPresent());
        Assert.assertEquals(role.get().getName(), ROLE_NAME);
    }

    @AfterClass
    public void cleanUpUsers() {
        roleRepository.deleteAll();
        Assert.assertEquals(roleRepository.count(), 0);
    }


}

package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Optional;

@SpringBootTest
@Test(groups = {"userRepository"})
public class UserRoleTest extends AbstractTestNGSpringContextTests {

    private static final String ROLE_NAME = "Backend";

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void saveRole() {
        Role role = new Role();
        role.setName(ROLE_NAME);
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


}

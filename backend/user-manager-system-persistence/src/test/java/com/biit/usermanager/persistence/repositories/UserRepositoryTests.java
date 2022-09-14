package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Test(groups = {"userRepository"})
public class UserRepositoryTests extends AbstractTestNGSpringContextTests {
    private static String USER_NAME = "TestUser";

    @Autowired
    private UserRepository userRepository;

    private UserRoleRepository userRoleRepository;

   @Test
    public void saveUser() {
        User user = new User();
        user.setUsername(USER_NAME);
        Assert.assertNull(user.getId());
        user = userRepository.save(user);
        Assert.assertNotNull(user.getId());

    }

   @Test(dependsOnMethods = "saveUser")
    public void getUserByUserName() {
        Optional<User> user = userRepository.findByUsername(USER_NAME);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getUsername(), USER_NAME);
    }


}


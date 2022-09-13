package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.User;
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
    private static String PHONE = "902202122";
    private static int NUM = 2;
    private static boolean ACCOUNT_EXPIRED = true;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUser() {
        User user = new User();

        user.setUsername(USER_NAME);
        user.setPhone(PHONE);
        user.setAccountExpired(ACCOUNT_EXPIRED);

        User user1 = new User();
        user1.setAccountExpired(ACCOUNT_EXPIRED);
        user1.setPhone(PHONE);
        User user2 = new User();
        user2.setAccountExpired(!ACCOUNT_EXPIRED);

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

    @Test(dependsOnMethods = "saveUser")
    public void getUserByPhone() {
        Optional<User> user = userRepository.findByPhone(PHONE);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getPhone(), PHONE);
    }


}

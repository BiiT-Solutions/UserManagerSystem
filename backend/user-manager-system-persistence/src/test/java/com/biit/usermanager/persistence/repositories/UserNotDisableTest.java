package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Test(groups = {"userRepository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserNotDisableTest extends AbstractTestNGSpringContextTests {
    private final static String USER_NAME_1 = "Ana";
    private final static String USER_NAME_2 = "Adri";
    private final static String USER_NAME_3 = "Mariano";

    @Autowired
    UserRepository userRepository;

    @Test
    public void saveUser() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.setName(USER_NAME_1);
        user2.setName(USER_NAME_2);
        user3.setName(USER_NAME_3);

        user1.setEnabled(true);
        user2.setEnabled(false);
        user3.setEnabled(true);

        Assert.assertNull(user1.getId());
        Assert.assertNull(user2.getId());
        Assert.assertNull(user3.getId());

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        Assert.assertNotNull(user1.getId());
        Assert.assertNotNull(user2.getId());
        Assert.assertNotNull(user3.getId());
    }

    @Test(dependsOnMethods = "saveUser")
    public void getUsersByNotDisable() {
        List<User> usersList = userRepository.findAllByEnabled(true);
        Assert.assertEquals(usersList.size(), 2);
        for (User user : usersList) {
            Assert.assertTrue(user.isEnabled());
        }
    }

    @AfterClass
    public void cleanUpUsers(){
        userRepository.deleteAll();
        Assert.assertEquals(userRepository.count(), 0);
    }
}

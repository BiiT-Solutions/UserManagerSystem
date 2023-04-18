package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.List;

@SpringBootTest
@Test(groups = {"userRepository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserNotDisableTest extends AbstractTestNGSpringContextTests {
    private final static String USER_NAME_1 = "Ana";
    private final static String USER_NAME_2 = "Adrian";
    private final static String USER_NAME_3 = "Mariano";

    private final static String id_Card1 = "21796308J";
    private final static String id_Card2 = "21796308S";
    private final static String id_Card3 = "21896308D";

    private final static String username1 = "amari";

    private final static String username2 = "adrian";

    private final static String username3 = "mariano";

    @Autowired
    UserRepository userRepository;

    @Test
    public void saveUser() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        user1.setIdCard(id_Card1);
        user2.setIdCard(id_Card2);
        user3.setIdCard(id_Card3);

        user1.setUsername(username1);
        user2.setUsername(username2);
        user3.setUsername(username3);

        user1.setName(USER_NAME_1);
        user2.setName(USER_NAME_2);
        user3.setName(USER_NAME_3);

        user1.setLastname("Mari");
        user2.setLastname("Coco");
        user3.setLastname("Cocolo");

        user1.setEmail("ani3537@gmail.com");
        user2.setEmail("adri3839@gmail.com");
        user3.setEmail("mariano1234@gmail.com");

        user1.setPhone("601295927");
        user2.setPhone("602405077");
        user3.setPhone("604503788");

        user1.setPassword("hola");
        user2.setPassword("adios");
        user3.setPassword("BonNadal");



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

    @AfterClass(alwaysRun = true)
    public void cleanUpUsers(){
        userRepository.deleteAll();
        Assert.assertEquals(userRepository.count(), 0);
    }
}

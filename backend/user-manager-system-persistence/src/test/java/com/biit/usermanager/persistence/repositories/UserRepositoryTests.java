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
import java.util.Optional;

@SpringBootTest
@Test(groups = {"userRepository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserRepositoryTests extends AbstractTestNGSpringContextTests {
    private static String USER_NAME = "TestUser";
    private static String USER_EMAIL = "TestUser@gmail.com";
    private static String NAME = "TestUserName";
    private static String LASTNAME = "TestUserLastname";
    private static String USER_IDCARD = "TestUserIdCard";
    private static String PHONE = "902202122";

    @Autowired
    private UserRepository userRepository;

    private UserRoleRepository userRoleRepository;

    @Test
    public void saveUser() {
        User user = new User();
        user.setUsername(USER_NAME);
        user.setName(NAME);
        user.setLastname(LASTNAME);
        user.setEmail(USER_EMAIL);
        user.setIdCard(USER_IDCARD);
        user.setPhone(PHONE);
        user.setAccountExpired(true);

        User user2 = new User();
        user2.setAccountExpired(false);
        userRepository.save(user2);

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
    public void getUserByNameAndLastname() {
        Optional<User> user = userRepository.findByNameAndLastname(NAME, LASTNAME);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getName(), NAME);
        Assert.assertEquals(user.get().getLastname(),LASTNAME);
    }
    @Test(dependsOnMethods = "saveUser")
    public void getUserByEmail() {
        Optional<User> user = userRepository.findByEmail(USER_EMAIL);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getEmail(), USER_EMAIL);
    }
    @Test(dependsOnMethods = "saveUser")
    public void getUserByIdCard() {
        Optional<User> user = userRepository.findByIdCard(USER_IDCARD);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getIdCard(), USER_IDCARD);
    }

    @Test(dependsOnMethods = "saveUser")
    public void getUserByPhone() {
        Optional<User> user = userRepository.findByPhone(PHONE);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getPhone(), PHONE);
    }

    @Test(dependsOnMethods = "saveUser")
    public void howManyUsersPhone() {
        List<User> users = userRepository.findAllByPhone(PHONE);
        for (User user : users) {
            Assert.assertNotNull(user);
            Assert.assertEquals(user.getPhone(), PHONE);
        }
    }

    @Test(dependsOnMethods = "saveUser")
    public void getUsersExpired() {
        List<User> users = userRepository.findByAccountExpired(true);
        for (User user : users) {
            Assert.assertNotNull(user);
            Assert.assertTrue(user.isAccountExpired());
        }
        Assert.assertEquals(users.size(), 1);
    }

    @AfterClass(alwaysRun = true)
    public void cleanUpUsers(){
        userRepository.deleteAll();
        Assert.assertEquals(userRepository.count(), 0);
    }
}

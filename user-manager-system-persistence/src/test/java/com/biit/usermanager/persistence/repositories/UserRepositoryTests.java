package com.biit.usermanager.persistence.repositories;

/*-
 * #%L
 * User Manager System (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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

    @Test
    public void saveUser() {
        User user = new User();
        user.setUsername(USER_NAME);
        user.setName(NAME);
        user.setLastname(LASTNAME);
        user.setEmail(USER_EMAIL);
        user.setIdCard(USER_IDCARD);
        user.setPhone(PHONE);
        user.expireAccount(true);

        User user2 = new User();
        user2.expireAccount(false);
        userRepository.save(user2);

        Assert.assertNull(user.getId());
        user = userRepository.save(user);
        Assert.assertNotNull(user.getId());
    }

    @Test(dependsOnMethods = "saveUser")
    public void getUserByUserName() {
        Optional<User> user = userRepository.findByUsernameHash(USER_NAME.toLowerCase());
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getUsername(), USER_NAME.toLowerCase());
    }

    @Test(dependsOnMethods = "saveUser")
    public void getUserByEmail() {
        List<User> users = userRepository.findByEmailHash(USER_EMAIL.toLowerCase());
        Assert.assertFalse(users.isEmpty());
        Assert.assertEquals(users.get(0).getEmail(), USER_EMAIL);
    }

    @AfterClass(alwaysRun = true)
    public void cleanUpUsers() {
        userRepository.deleteAll();
        Assert.assertEquals(userRepository.count(), 0);
    }
}

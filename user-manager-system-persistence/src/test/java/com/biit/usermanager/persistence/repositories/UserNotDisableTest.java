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

@SpringBootTest
@Test(groups = {"userRepository"})
public class UserNotDisableTest extends AbstractTestNGSpringContextTests {
    private static final String USER_NAME_1 = "Ana";
    private static final String USER_NAME_2 = "Adrian";
    private static final String USER_NAME_3 = "Mariano";

    private static final String id_Card1 = "21796308J";
    private static final String id_Card2 = "21796308S";
    private static final String id_Card3 = "21896308D";

    private static final String username1 = "amari";

    private static final String username2 = "adrian";

    private static final String username3 = "mariano";

    @Autowired
    private UserRepository userRepository;

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



        user1.setAccountBlocked(true);
        user2.setAccountBlocked(false);
        user3.setAccountBlocked(true);

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
    public void getUsersByNotBlocked() {
        List<User> usersList = userRepository.findAllByAccountBlocked(true);
        Assert.assertEquals(usersList.size(), 2);
        for (User user : usersList) {
            Assert.assertTrue(user.isAccountBlocked());
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanUpUsers(){
        userRepository.deleteAll();
        Assert.assertEquals(userRepository.count(), 0);
    }
}

package com.biit.usermanager.core;

/*-
 * #%L
 * User Manager System (core)
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

import com.biit.server.security.CreateUserRequest;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.exceptions.InvalidParameterException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "passwordTests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class PasswordTests extends AbstractTestNGSpringContextTests {
    private static final String USER_NAME = "user";
    private static final String USER_FIRST_NAME = "Test";
    private static final String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "password";

    private static final String USER_NEW_PASSWORD = "password2";

    @Autowired
    private UserController userController;

    @BeforeClass
    public void setUp() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setFirstname(USER_FIRST_NAME);
        createUserRequest.setLastname(USER_LAST_NAME);
        createUserRequest.setPassword(USER_PASSWORD);
        userController.create(createUserRequest, null);
    }

    @Test(expectedExceptions = UserNotFoundException.class)
    public void updatePasswordInvalidUser() {
        userController.updatePassword(USER_NAME + "_error", USER_PASSWORD, USER_NEW_PASSWORD, null);
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void updatePasswordInvalidOld() {
        userController.updatePassword(USER_NAME, USER_PASSWORD + "_error", USER_NEW_PASSWORD, null);
    }

    @Test
    public void updatePasswordCorrectOld() {
        userController.updatePassword(USER_NAME, USER_PASSWORD, USER_NEW_PASSWORD, null);
        userController.updatePassword(USER_NAME, USER_NEW_PASSWORD, USER_PASSWORD, null);
    }

    @AfterClass
    public void cleanUp() {
        userController.deleteAll(null);
    }
}

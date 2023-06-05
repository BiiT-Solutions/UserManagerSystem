package com.biit.usermanager.core;

import com.biit.server.security.CreateUserRequest;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.exceptions.InvalidParameterException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = "passwordTests")
@SpringBootTest
public class PasswordTests extends AbstractTestNGSpringContextTests {
    private static final String USER_NAME = "user";
    private final static String USER_FIRST_NAME = "Test";
    private final static String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "password";

    private static final String USER_NEW_PASSWORD = "password2";

    private static final String[] USER_ROLES = new String[] {"admin", "viewer"};

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
}

package com.biit.usermanager.client;

import com.biit.server.security.IAuthenticatedUser;
import com.biit.usermanager.client.provider.UserManagerClient;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.controller.UserRoleController;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.rest.UserManagerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = UserManagerServer.class)
@Test(groups = {"clientTests"})
public class ClientTests extends AbstractTestNGSpringContextTests {

    private static final String USER_NAME = "admin";
    private static final String USER_UNIQUE_ID = "00000000AA";
    private static final String USER_FIRST_NAME = "Test";
    private static final String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String[] USER_ROLES = new String[]{"ADMIN", "VIEWER"};

    @Autowired
    private UserController userController;

    @Autowired
    private RoleController roleController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private UserRoleController userRoleController;

    @Autowired
    private UserManagerClient userManagerClient;

    @Value("${spring.application.name}")
    private String applicationName;


    @BeforeClass
    public void setDefaultData() {
        //Create the admin user
        final UserDTO admin = (UserDTO) userController.createUser(USER_NAME, USER_UNIQUE_ID, USER_FIRST_NAME, USER_LAST_NAME, USER_PASSWORD);

        //Create the application
        final ApplicationDTO applicationDTO = applicationController.create(new ApplicationDTO(applicationName.toUpperCase(), ""));

        //Set the roles
        final List<RoleDTO> roles = new ArrayList<>();
        final List<RoleDTO> applicationRoles = new ArrayList<>();
        for (final String roleName : USER_ROLES) {
            roles.add(roleController.create(new RoleDTO(roleName, null)));
            applicationRoles.add(roleController.create(new RoleDTO(applicationName.toUpperCase() + "_" + roleName, null)));
        }

        //Assign the basic roles.
        roles.forEach(roleDTO -> userRoleController.create(new UserRoleDTO(admin, roleDTO, null, null)));

        //Assign application roles.
        applicationRoles.forEach(roleDTO -> userRoleController.create(new UserRoleDTO(admin, roleDTO, null, applicationDTO)));
    }

    @Test
    public void findUserByName() {
        final Optional<IAuthenticatedUser> user = userManagerClient.findByUsername(USER_NAME);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getUsername(), USER_NAME);
    }

    @Test
    public void findUserByNameAndApplication() {
        Optional<IAuthenticatedUser> user = userManagerClient.findByUsername(USER_NAME, applicationName.toUpperCase());
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getUsername(), USER_NAME);
        Assert.assertEquals(user.get().getAuthorities().size(), 2);

        user = userManagerClient.findByUsername(USER_NAME, applicationName + "_bad");
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getAuthorities().size(), 0);
    }
}

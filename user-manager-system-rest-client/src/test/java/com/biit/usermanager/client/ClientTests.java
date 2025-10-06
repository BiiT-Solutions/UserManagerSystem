package com.biit.usermanager.client;

import com.biit.usermanager.client.providers.UserManagerClient;
import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.ApplicationRoleController;
import com.biit.usermanager.core.controller.BackendServiceController;
import com.biit.usermanager.core.controller.BackendServiceRoleController;
import com.biit.usermanager.core.controller.OrganizationController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.TeamController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.core.exceptions.InvalidParameterException;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.rest.UserManagerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = UserManagerServer.class)
@Test(groups = {"clientTests"})
public class ClientTests extends AbstractTestNGSpringContextTests {

    private static final String USER_NAME = "admin";
    private static final String USER_UNIQUE_ID = "00000000AA";
    private static final String USER_FIRST_NAME = "Test";
    private static final String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String USER_EXTERNAL_REFERENCE = UUID.randomUUID().toString();
    private static final String[] APPLICATION_ROLES = new String[]{"WRITER", "READER"};
    private static final String[] BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};
    private static final String APPLICATION_NAME = "DASHBOARD";
    private static final String ORGANIZATION_NAME = "NHM";
    private static final String DEFAULT_GROUP = "Standard_Users";

    @Value("${bcrypt.salt:}")
    private String bcryptSalt;

    @Autowired
    private UserController userController;

    @Autowired
    private RoleController roleController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private OrganizationController organizationController;

    @Autowired
    private TeamController teamController;

    @Autowired
    private ApplicationRoleController applicationRoleController;

    @Autowired
    private BackendServiceRoleController backendServiceRoleController;
    @Autowired
    private BackendServiceController backendServiceController;

    @Autowired
    private ApplicationBackendServiceRoleController applicationBackendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleConverter applicationBackendServiceRoleConverter;

    @Autowired
    private UserManagerClient userManagerClient;

    @Value("${spring.application.name}")
    private String backendService;


    @BeforeClass
    public void setDefaultData() {
        //Create the admin user
        final UserDTO admin = userController.createUser(USER_NAME, USER_UNIQUE_ID, USER_FIRST_NAME, USER_LAST_NAME, USER_PASSWORD, USER_EXTERNAL_REFERENCE, null);

        //Create the application
        final ApplicationDTO applicationDTO = applicationController.create(new ApplicationDTO(APPLICATION_NAME, ""), null);

        //Create the organization
        final OrganizationDTO organizationDTO = organizationController.create(new OrganizationDTO(ORGANIZATION_NAME), null);

        //Create a group
        final TeamDTO teamDTO = teamController.create(new TeamDTO(DEFAULT_GROUP, organizationDTO), null);

        //Set the application roles
        final List<RoleDTO> roleDTOs = new ArrayList<>();
        for (final String roleName : APPLICATION_ROLES) {
            roleDTOs.add(roleController.create(new RoleDTO(roleName, null), null));
        }

        //Assign the application roles.
        final List<ApplicationRoleDTO> applicationRoles = new ArrayList<>();
        roleDTOs.forEach(roleDTO -> applicationRoles.add(applicationRoleController.create(new ApplicationRoleDTO(applicationDTO, roleDTO), null)));

        //Set the backend roles.
        final BackendServiceDTO backendServiceDTO = backendServiceController.create(new BackendServiceDTO(backendService), null);

        final List<BackendServiceRoleDTO> backendRoles = new ArrayList<>();
        for (final String roleName : BACKEND_ROLES) {
            backendRoles.add(backendServiceRoleController.create(new BackendServiceRoleDTO(backendServiceDTO, roleName), null));
        }

        //Assign the backend to an application.
        final List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOs = new ArrayList<>();
        for (ApplicationRoleDTO applicationRole : applicationRoles) {
            for (BackendServiceRoleDTO backendRole : backendRoles) {
                applicationBackendServiceRoleDTOs.add(applicationBackendServiceRoleController.create(new ApplicationBackendServiceRoleDTO(applicationRole, backendRole), null));
            }
        }

        userController.setApplicationBackendServiceRole(admin, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOs));
    }

    @Test
    public void checkPasswordCorrect() {
        userController.checkPassword(USER_NAME, USER_PASSWORD);
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void checkPasswordCorrectIncorrect() {
        userController.checkPassword(USER_NAME, USER_PASSWORD + "A");
    }

    @Test
    public void findUserByName() {
        final Optional<UserDTO> user = userManagerClient.findByUsername(USER_NAME);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getUsername(), USER_NAME);
    }

    @Test
    public void findUserByNameAndService() {
        //Application + default authorities.
        Optional<UserDTO> user = userManagerClient.findByUsername(USER_NAME, backendService);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getUsername(), USER_NAME);
        Assert.assertEquals(user.get().getAuthorities().size(), 2);

        //Only default authorities.
        user = userManagerClient.findByUsername(USER_NAME, null);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getUsername(), USER_NAME);
        Assert.assertEquals(user.get().getAuthorities().size(), 2);

        System.out.println(" #------------------------------------ Expected Exception ----------------------------");
        try {
            userManagerClient.findByUsername(USER_NAME, backendService + "_bad");
        } catch (Exception ignored) {

        }
        System.out.println(" #--------------------------------- End of Expected Exception -------------------------");
    }

    @Test
    public void findUserFromExternalReference() {
        Optional<UserDTO> user = userManagerClient.findByExternalReference(USER_EXTERNAL_REFERENCE);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(user.get().getUsername(), USER_NAME);
    }

    @Test
    public void getPassword() {
        Assert.assertTrue(BCrypt.checkpw(bcryptSalt + USER_PASSWORD, userManagerClient.getPassword(USER_NAME)));
    }

    @Test
    public void getPasswordByUID() {
        final Optional<UserDTO> user = userManagerClient.findByUsername(USER_NAME);
        Assert.assertTrue(user.isPresent());
        Assert.assertTrue(BCrypt.checkpw(bcryptSalt + USER_PASSWORD, userManagerClient.getPasswordByUid(user.get().getUID())));
    }

    @Test
    public void checkUsernameExists() {
        Assert.assertTrue(userManagerClient.usernameExists(USER_NAME));
        Assert.assertFalse(userManagerClient.usernameExists(USER_NAME + "_wrong"));
    }
}

package com.biit.usermanager.security;

import com.biit.usermanager.core.controller.*;
import com.biit.usermanager.dto.*;
import com.biit.usermanager.security.activities.ActivityManager;
import com.biit.usermanager.security.activities.RoleActivities;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = {"activitiesTests"})
public class RoleActivitiesTest extends AbstractTestNGSpringContextTests {

    private static final String ADMIN_USER_NAME = "admin";
    private static final String ADMIN_EMAIL = "admin@test.com";
    private final static String ADMIN_FIRST_NAME = "Admin";
    private final static String ADMIN_LAST_NAME = "User";
    private static final String ADMIN_PASSWORD = "zxc567";
    private static final String ADMIN_ID_CARD = "12345678A";
    private static final String[] ADMIN_ROLES = new String[]{"usermanagersystem_admin", "usermanagersystem_viewer"};

    private static final String USER_NAME = "test";
    private static final String USER_EMAIL = "test@test.com";
    private final static String USER_FIRST_NAME = "Test";
    private final static String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String USER_ID_CARD = "87654321B";
    private static final String[] USER_ROLES = new String[]{"test_receptionist"};

    private static final String GROUP_NAME = "Group1";


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @Autowired
    private RoleController roleController;

    @Autowired
    private UserRoleController userRoleController;

    @Autowired
    private GroupController groupController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private AuthorizationService authorizationService;

    @Value("${spring.application.name}")
    private String applicationName;

    private MockMvc mockMvc;

    private String jwtToken;

    private ApplicationDTO applicationDTO;

    private Map<String, RoleDTO> roles;

    private GroupDTO groupDTO;
    private GroupDTO groupDTO2;

    @Autowired
    private RoleActivities roleActivities;

    @Autowired
    private ActivityManager activityManager;

    private UserDTO user;


    @BeforeClass
    private void createApplication() {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setName(applicationName);
        this.applicationDTO = applicationController.create(applicationDTO);
    }

    @BeforeClass
    private void createGroups() {
        this.groupDTO = groupController.create(new GroupDTO(GROUP_NAME));
    }

    @BeforeClass
    private void createRoles() {
        roles = new HashMap<>();
        Set<String> roleNames = new HashSet<>(Arrays.asList(ADMIN_ROLES));
        roleNames.addAll(Arrays.asList(USER_ROLES));
        for (String roleName : roleNames) {
            roles.put(roleName, roleController.create(new RoleDTO(roleName, null)));
        }
    }

    @BeforeClass(dependsOnMethods = {"createApplication", "createRoles"})
    private void createAdminAccount() {
        //Create the admin user
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(ADMIN_USER_NAME);
        userDTO.setIdCard(ADMIN_ID_CARD);
        userDTO.setFirstName(ADMIN_FIRST_NAME);
        userDTO.setLastName(ADMIN_LAST_NAME);
        userDTO.setEmail(ADMIN_EMAIL);
        userDTO.setPassword(ADMIN_PASSWORD);
        final UserDTO adminUser = userController.create(userDTO);

        //Assign admin roles
        for (String adminRole : ADMIN_ROLES) {
            userRoleController.create(new UserRoleDTO(adminUser, roles.get(adminRole), null, applicationDTO));
        }
    }

    @BeforeClass(dependsOnMethods = {"createApplication", "createRoles", "createGroups"})
    private void createUserAccount() {
        //Create the test user
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(USER_NAME);
        userDTO.setIdCard(USER_ID_CARD);
        userDTO.setFirstName(USER_FIRST_NAME);
        userDTO.setLastName(USER_LAST_NAME);
        userDTO.setEmail(USER_EMAIL);
        userDTO.setPassword(USER_PASSWORD);
        user = userController.create(userDTO);

        //Assign user roles
        for (String userRoles : USER_ROLES) {
            userRoleController.create(new UserRoleDTO(user, roles.get(userRoles), groupDTO, applicationDTO));
            userRoleController.create(new UserRoleDTO(user, roles.get(userRoles), groupDTO2, applicationDTO));
        }
    }


    @BeforeClass
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void readActivities() {
        Assert.assertEquals(roleActivities.getRoleActivities("test_receptionist").size(), 32);
        Assert.assertTrue(roleActivities.getRoleActivities("test_receptionist").contains(roleActivities.getByTag("appointments_manager")));
        Assert.assertEquals(roleActivities.getRoleActivities("test_web-service-user").size(), 1);
    }

    @Test
    public void checkPermission() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        Assert.assertTrue(activityManager.isAuthorizedActivity(user, roleActivities.getByTag("appointments_manager")));
        Assert.assertFalse(activityManager.isAuthorizedActivity(user, roleActivities.getByTag("examine_patient")));
    }

    @AfterClass(alwaysRun = true)
    public void dropTables() {
        userRoleController.deleteAll();
        applicationController.deleteAll();
        groupController.deleteAll();
        roleController.deleteAll();
        userController.deleteAll();
    }

}

package com.biit.usermanager.security;

import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.ApplicationRoleController;
import com.biit.usermanager.core.controller.BackendServiceController;
import com.biit.usermanager.core.controller.BackendServiceRoleController;
import com.biit.usermanager.core.controller.GroupController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.security.activities.ActivityManager;
import com.biit.usermanager.security.activities.RoleActivities;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = {"activitiesTests"})
public class RoleActivitiesTest extends AbstractTestNGSpringContextTests {

    private static final String ADMIN_USER_NAME = "admin";
    private static final String ADMIN_EMAIL = "admin@test.com";
    private final static String ADMIN_FIRST_NAME = "Admin";
    private final static String ADMIN_LAST_NAME = "User";
    private static final String ADMIN_PASSWORD = "zxc567";
    private static final String ADMIN_ID_CARD = "12345678A";
    private static final List<String> ADMIN_ROLES = List.of("DOCTOR");

    private static final String[] BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};

    private static final String USER_NAME = "test";
    private static final String USER_EMAIL = "test@test.com";
    private final static String USER_FIRST_NAME = "Test";
    private final static String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String USER_ID_CARD = "87654321B";
    private static final List<String> USER_ROLES = List.of("RECEPTIONIST");

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
    private GroupController groupController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private BackendServiceRoleController backendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleController applicationBackendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleConverter applicationBackendServiceRoleConverter;

    @Autowired
    private RoleActivities roleActivities;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private ApplicationRoleController applicationRoleController;

    @Autowired
    private BackendServiceController backendServiceController;

    private static final String APPLICATION_NAME = "DASHBOARD";

    @Value("${spring.application.name}")
    private String backendService;

    private ApplicationDTO applicationDTO;

    private Map<String, RoleDTO> roles;

    private GroupDTO groupDTO;

    private UserDTO user;

    private List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOs;
    private List<ApplicationBackendServiceRoleDTO> adminRoles;
    private List<ApplicationBackendServiceRoleDTO> userRoles;

    @BeforeClass
    private void createApplication() {
        ApplicationDTO applicationDTO = new ApplicationDTO(APPLICATION_NAME);
        this.applicationDTO = applicationController.create(applicationDTO, null);
    }

    @BeforeClass(dependsOnMethods = "createApplication")
    private void createGroups() {
        this.groupDTO = groupController.create(new GroupDTO(GROUP_NAME, applicationDTO), null);
    }

    @BeforeClass
    private void createRoles() {
        roles = new HashMap<>();
        Set<String> roleNames = new HashSet<>(ADMIN_ROLES);
        roleNames.addAll(USER_ROLES);
        for (String roleName : roleNames) {
            roles.put(roleName, roleController.create(new RoleDTO(roleName, null), null));
        }

        //Assign the application roles.
        final List<ApplicationRoleDTO> applicationRoles = new ArrayList<>();
        roles.values().forEach(roleDTO -> applicationRoles.add(applicationRoleController.create(new ApplicationRoleDTO(applicationDTO, roleDTO), null)));

        //Set the backend roles.
        final BackendServiceDTO backendServiceDTO = backendServiceController.create(new BackendServiceDTO(backendService), null);

        final List<BackendServiceRoleDTO> backendRoles = new ArrayList<>();
        for (final String roleName : BACKEND_ROLES) {
            backendRoles.add(backendServiceRoleController.create(new BackendServiceRoleDTO(backendServiceDTO, roleName), null));
        }

        //Assign the backend to an application.
        applicationBackendServiceRoleDTOs = new ArrayList<>();
        adminRoles = new ArrayList<>();
        userRoles = new ArrayList<>();
        for (ApplicationRoleDTO applicationRole : applicationRoles) {
            for (BackendServiceRoleDTO backendRole : backendRoles) {
                final ApplicationBackendServiceRoleDTO applicationBackendServiceRoleDTO = applicationBackendServiceRoleController.create(new ApplicationBackendServiceRoleDTO(applicationRole, backendRole), null);
                applicationBackendServiceRoleDTOs.add(applicationBackendServiceRoleDTO);
                if (ADMIN_ROLES.contains(applicationRole.getId().getRole().getName())) {
                    adminRoles.add(applicationBackendServiceRoleDTO);
                }
                if (USER_ROLES.contains(applicationRole.getId().getRole().getName())) {
                    userRoles.add(applicationBackendServiceRoleDTO);
                }
            }
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
        final UserDTO adminUser = userController.create(userDTO, null);

        //Assign admin roles
        //Assign admin roles
        userController.setApplicationBackendServiceRole(adminUser,
                applicationBackendServiceRoleConverter.reverseAll(adminRoles));
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
        user = userController.create(userDTO, null);

        //Assign user roles
        userController.setApplicationBackendServiceRole(user,
                applicationBackendServiceRoleConverter.reverseAll(userRoles));
    }

    @Test(enabled = false)
    public void readActivities() {
        Assert.assertEquals(roleActivities.getRoleActivities("test_receptionist").size(), 32);
        Assert.assertTrue(roleActivities.getRoleActivities("test_receptionist").contains(roleActivities.getByTag("appointments_manager")));
        Assert.assertEquals(roleActivities.getRoleActivities("test_web-service-user").size(), 1);
    }

    @Test(enabled = false)
    public void checkPermission() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        Assert.assertTrue(activityManager.isAuthorizedActivity(user, roleActivities.getByTag("appointments_manager")));
        Assert.assertFalse(activityManager.isAuthorizedActivity(user, roleActivities.getByTag("examine_patient")));
    }

    @AfterClass(alwaysRun = true)
    public void dropTables() {
        userController.deleteAll();
        applicationBackendServiceRoleController.deleteAll();
        backendServiceRoleController.deleteAll();
        applicationRoleController.deleteAll();
        groupController.deleteAll();
        applicationController.deleteAll();
        backendServiceController.deleteAll();
        roleController.deleteAll();
    }

}

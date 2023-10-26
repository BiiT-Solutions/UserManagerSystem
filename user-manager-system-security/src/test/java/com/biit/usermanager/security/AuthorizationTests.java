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
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.OrganizationDoesNotExistException;
import com.biit.usermanager.security.exceptions.RoleDoesNotExistsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = {"authenticationTests"})
public class AuthorizationTests extends AbstractTransactionalTestNGSpringContextTests {
    private static final String ADMIN_USER_NAME = "admin";
    private static final String ADMIN_EMAIL = "admin@test.com";
    private final static String ADMIN_FIRST_NAME = "Admin";
    private final static String ADMIN_LAST_NAME = "User";
    private static final String ADMIN_PASSWORD = "zxc567";
    private static final String ADMIN_ID_CARD = "12345678A";
    private static final List<String> ADMIN_ROLES = List.of("ADMIN");

    private static final List<String> USER_ROLES = List.of("WRITER", "VIEWER");

    private static final List<String> OTHER_USER_ROLES = List.of("WRITER", "EDITOR");

    private static final String USER_NAME = "test";
    private static final String USER_EMAIL = "test@test.com";
    private final static String USER_FIRST_NAME = "Test";
    private final static String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String USER_ID_CARD = "87654321B";

    private static final String[] BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};

    private static final String OTHER_USER_NAME = "test2";
    private static final String OTHER_USER_EMAIL = "test2@test.com";
    private final static String OTHER_USER_FIRST_NAME = "Test2";
    private final static String OTHER_USER_LAST_NAME = "User2";
    private static final String OTHER_USER_PASSWORD = "456123";
    private static final String OTHER_USER_ID_CARD = "6667778P";

    private static final String GROUP_NAME = "Group1";

    private static final String EMPTY_GROUP_NAME = "Group2";
    private static final String OTHER_GROUP_NAME = "Group3";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @Autowired
    private RoleController roleController;

    @Autowired
    private ApplicationRoleController applicationRoleController;

    @Autowired
    private GroupController groupController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private BackendServiceController backendServiceController;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private BackendServiceRoleController backendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleController applicationBackendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleConverter applicationBackendServiceRoleConverter;

    private static final String APPLICATION_NAME = "DASHBOARD";

    @Value("${spring.application.name}")
    private String backendService;

    private ApplicationDTO applicationDTO;

    private Map<String, RoleDTO> roles;

    private GroupDTO groupDTO;
    private GroupDTO groupDTO2;

    private List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOs;
    private List<ApplicationBackendServiceRoleDTO> adminRoles;
    private List<ApplicationBackendServiceRoleDTO> userRoles;
    private List<ApplicationBackendServiceRoleDTO> otherRoles;


    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T fromJson(String payload, Class<T> clazz) throws IOException {
        return objectMapper.readValue(payload, clazz);
    }

    @BeforeClass
    private void createApplication() {
        ApplicationDTO applicationDTO = new ApplicationDTO(APPLICATION_NAME);
        this.applicationDTO = applicationController.create(applicationDTO, null);
    }

    @BeforeClass(dependsOnMethods = "createApplication")
    private void createGroups() {
        groupDTO = groupController.create(new GroupDTO(GROUP_NAME, applicationDTO), null);
        groupDTO2 = groupController.create(new GroupDTO(OTHER_GROUP_NAME, applicationDTO), null);
        groupController.create(new GroupDTO(EMPTY_GROUP_NAME, applicationDTO), null);
    }

    @BeforeClass
    private void createRoles() {
        roles = new HashMap<>();
        Set<String> roleNames = new HashSet<>(ADMIN_ROLES);
        roleNames.addAll(USER_ROLES);
        roleNames.addAll(OTHER_USER_ROLES);
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
        otherRoles = new ArrayList<>();
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
                if (OTHER_USER_ROLES.contains(applicationRole.getId().getRole().getName())) {
                    otherRoles.add(applicationBackendServiceRoleDTO);
                }
            }
        }
    }

    @BeforeClass(dependsOnMethods = {"createRoles"})
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
        userController.setApplicationBackendServiceRole(adminUser,
                applicationBackendServiceRoleConverter.reverseAll(adminRoles));
    }

    @BeforeClass(dependsOnMethods = {"createApplication", "createRoles"})
    private void createUserAccount() {
        //Create the test user
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(USER_NAME);
        userDTO.setIdCard(USER_ID_CARD);
        userDTO.setFirstName(USER_FIRST_NAME);
        userDTO.setLastName(USER_LAST_NAME);
        userDTO.setEmail(USER_EMAIL);
        userDTO.setPassword(USER_PASSWORD);
        final UserDTO testUser = userController.create(userDTO, null);

        //Assign user roles
        userController.setApplicationBackendServiceRole(testUser,
                applicationBackendServiceRoleConverter.reverseAll(userRoles));
    }

    @BeforeClass(dependsOnMethods = {"createApplication", "createRoles"})
    private void createOtherUserAccount() {
        //Create the test user
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(OTHER_USER_NAME);
        userDTO.setIdCard(OTHER_USER_ID_CARD);
        userDTO.setFirstName(OTHER_USER_FIRST_NAME);
        userDTO.setLastName(OTHER_USER_LAST_NAME);
        userDTO.setEmail(OTHER_USER_EMAIL);
        userDTO.setPassword(OTHER_USER_PASSWORD);
        final UserDTO testUser = userController.create(userDTO, null);

        //Assign user roles
        userController.setApplicationBackendServiceRole(testUser,
                applicationBackendServiceRoleConverter.reverseAll(otherRoles));
    }

    @Test
    public void getAllUsers() throws UserManagementException, InvalidCredentialsException {
        Assert.assertEquals(authorizationService.getAllUsers().size(), 3);
    }

    @Test(enabled = false)
    public void getAllUsersByGroup() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getAllUsers(groupDTO).size(), 2);
        GroupDTO groupDTO2 = groupController.getByName(OTHER_GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getAllUsers(groupDTO2).size(), 1);
        GroupDTO groupDTO3 = groupController.getByName(EMPTY_GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getAllUsers(groupDTO3).size(), 0);
    }

    @Test(enabled = false)
    public void getGroupById() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getOrganization(groupDTO.getUniqueId()).getUniqueName(), GROUP_NAME);
    }

    @Test(enabled = false)
    public void getGroupByName() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getOrganization(groupDTO.getUniqueName()).getUniqueName(), GROUP_NAME);
    }

    @Test(enabled = false)
    public void getAllGroups() throws UserManagementException, InvalidCredentialsException {
        Assert.assertEquals(authorizationService.getAllAvailableOrganizations().size(), 3);
    }

    @Test
    public void getRoleByName() throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException {
        RoleDTO roleDTO = roleController.getByName(USER_ROLES.get(0));
        Assert.assertEquals(authorizationService.getRole(roleDTO.getName()).getUniqueName(), USER_ROLES.get(0));
    }

    @Test
    public void getRoleById() throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException {
        RoleDTO roleDTO = roleController.getByName(USER_ROLES.get(0));
        Assert.assertEquals(authorizationService.getRole(roleDTO.getUniqueId()).getUniqueName(), USER_ROLES.get(0));
    }

    @Test(enabled = false)
    public void getUserGroups() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        Assert.assertEquals(authorizationService.getUserGroups(userController.getByUsername(USER_NAME)).size(), 2);
        Assert.assertEquals(authorizationService.getUserGroups(userController.getByUsername(ADMIN_USER_NAME)).size(), 0);
    }

    @Test(enabled = false)
    public void getUserRolesInGroupByUser() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO, groupDTO).size(), USER_ROLES.size());


        UserDTO adminDTO = userController.getByUsername(ADMIN_USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(adminDTO, groupDTO).size(), 0);
    }

    @Test(enabled = false)
    public void getUserRolesInGroup() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        Assert.assertEquals(authorizationService.getAllRoles(groupDTO).size(), OTHER_USER_ROLES.size());
    }

    @Test(enabled = false)
    public void getUserRoles() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO).size(), USER_ROLES.size());

        UserDTO adminDTO = userController.getByUsername(ADMIN_USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(adminDTO).size(), ADMIN_ROLES.size());
    }

    @Test(enabled = false)
    public void getUsersWithRoleOnGroup() throws UserManagementException, RoleDoesNotExistsException,
            InvalidCredentialsException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        RoleDTO roleDTO = roleController.getByName(USER_ROLES.get(0));

        Assert.assertEquals(authorizationService.getUsers(roleDTO, groupDTO).size(), 2);

        roleDTO = roleController.getByName(OTHER_USER_ROLES.get(1));
        Assert.assertEquals(authorizationService.getUsers(roleDTO, groupDTO).size(), 1);
    }

    //@Test(priority = 2) //Execute after any other test
    @Test(enabled = false, dependsOnMethods = {"getUsersWithRoleOnGroup", "getUserRoles", "getUserRolesInGroup",
            "getUserRolesInGroupByUser", "getUserGroups", "getRoleById", "getRoleByName", "getAllGroups", "getAllUsersByGroup"})
    public void addUserRole() throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException,
            UserDoesNotExistException, OrganizationDoesNotExistException {
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        RoleDTO roleDTO = roleController.getByName(OTHER_USER_ROLES.get(1));
        authorizationService.addUserRole(userDTO, roleDTO);

        Assert.assertEquals(authorizationService.getUserRoles(userDTO).size(), USER_ROLES.size() + 1);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO, groupDTO).size(), USER_ROLES.size());
    }

    @Test(enabled = false, dependsOnMethods = "addUserRole")
    public void addGroupUserRole() throws UserManagementException, InvalidCredentialsException,
            UserDoesNotExistException, OrganizationDoesNotExistException, RoleDoesNotExistsException {
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        RoleDTO roleDTO = roleController.getByName(ADMIN_ROLES.get(0));
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        authorizationService.addUserOrganizationRole(userDTO, groupDTO, roleDTO);

        Assert.assertEquals(authorizationService.getUserRoles(userDTO).size(), USER_ROLES.size() + 2);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO, groupDTO).size(), USER_ROLES.size() + 1);
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

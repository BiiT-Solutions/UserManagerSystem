package com.biit.usermanager.security;

import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.GroupController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.controller.UserRoleController;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserRoleDTO;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
    private static final String[] ADMIN_ROLES = new String[]{"usermanagersystem_admin", "usermanagersystem_viewer"};

    private static final String USER_NAME = "test";
    private static final String USER_EMAIL = "test@test.com";
    private final static String USER_FIRST_NAME = "Test";
    private final static String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String USER_ID_CARD = "87654321B";
    private static final String[] USER_ROLES = new String[]{"usermanagersystem_viewer"};

    private static final String OTHER_USER_NAME = "test2";
    private static final String OTHER_USER_EMAIL = "test2@test.com";
    private final static String OTHER_USER_FIRST_NAME = "Test2";
    private final static String OTHER_USER_LAST_NAME = "User2";
    private static final String OTHER_USER_PASSWORD = "456123";
    private static final String OTHER_USER_ID_CARD = "6667778P";
    private static final String[] OTHER_USER_ROLES = new String[]{"usermanagersystem_viewer", "usermanagersystem_editor"};

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
    private UserRoleController userRoleController;

    @Autowired
    private GroupController groupController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private AuthorizationService authorizationService;

    @Value("${spring.application.name}")
    private String applicationName;

    private ApplicationDTO applicationDTO;

    private Map<String, RoleDTO> roles;

    private GroupDTO groupDTO;
    private GroupDTO groupDTO2;

    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T fromJson(String payload, Class<T> clazz) throws IOException {
        return objectMapper.readValue(payload, clazz);
    }

    @BeforeClass
    private void createApplication() {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setName(applicationName);
        this.applicationDTO = applicationController.create(applicationDTO, null);
    }

    @BeforeClass(dependsOnMethods = "createApplication")
    private void createGroups() {
        this.groupDTO = groupController.create(new GroupDTO(GROUP_NAME, applicationDTO), null);
        this.groupDTO2 = groupController.create(new GroupDTO(OTHER_GROUP_NAME, applicationDTO), null);
        groupController.create(new GroupDTO(EMPTY_GROUP_NAME, applicationDTO), null);
    }

    @BeforeClass
    private void createRoles() {
        roles = new HashMap<>();
        Set<String> roleNames = new HashSet<>(Arrays.asList(ADMIN_ROLES));
        roleNames.addAll(Arrays.asList(USER_ROLES));
        roleNames.addAll(Arrays.asList(OTHER_USER_ROLES));
        for (String roleName : roleNames) {
            roles.put(roleName, roleController.create(new RoleDTO(roleName, null), null));
        }
    }

    @BeforeClass(dependsOnMethods = {"createGroups", "createRoles"})
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
        for (String adminRole : ADMIN_ROLES) {
            userRoleController.create(new UserRoleDTO(adminUser, roles.get(adminRole), null), null);
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
        final UserDTO testUser = userController.create(userDTO, null);

        //Assign user roles
        for (String userRoles : USER_ROLES) {
            userRoleController.create(new UserRoleDTO(testUser, roles.get(userRoles), groupDTO), null);
            userRoleController.create(new UserRoleDTO(testUser, roles.get(userRoles), groupDTO2), null);
        }
    }

    @BeforeClass(dependsOnMethods = {"createApplication", "createRoles", "createGroups"})
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
        for (String otherUserRoles : OTHER_USER_ROLES) {
            userRoleController.create(new UserRoleDTO(testUser, roles.get(otherUserRoles), groupDTO), null);
        }
    }

    @Test
    public void getAllUsers() throws UserManagementException, InvalidCredentialsException {
        Assert.assertEquals(authorizationService.getAllUsers().size(), 3);
    }

    @Test
    public void getAllUsersByGroup() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getAllUsers(groupDTO).size(), 2);
        GroupDTO groupDTO2 = groupController.getByName(OTHER_GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getAllUsers(groupDTO2).size(), 1);
        GroupDTO groupDTO3 = groupController.getByName(EMPTY_GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getAllUsers(groupDTO3).size(), 0);
    }

    @Test
    public void getGroupById() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getOrganization(groupDTO.getUniqueId()).getUniqueName(), GROUP_NAME);
    }

    @Test
    public void getGroupByName() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        Assert.assertEquals(authorizationService.getOrganization(groupDTO.getUniqueName()).getUniqueName(), GROUP_NAME);
    }

    @Test
    public void getAllGroups() throws UserManagementException, InvalidCredentialsException {
        Assert.assertEquals(authorizationService.getAllAvailableOrganizations().size(), 3);
    }

    @Test
    public void getRoleByName() throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException {
        RoleDTO roleDTO = roleController.getByName(USER_ROLES[0]);
        Assert.assertEquals(authorizationService.getRole(roleDTO.getName()).getUniqueName(), USER_ROLES[0]);
    }

    @Test
    public void getRoleById() throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException {
        RoleDTO roleDTO = roleController.getByName(USER_ROLES[0]);
        Assert.assertEquals(authorizationService.getRole(roleDTO.getUniqueId()).getUniqueName(), USER_ROLES[0]);
    }

    @Test
    public void getUserGroups() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        Assert.assertEquals(authorizationService.getUserGroups(userController.getByUsername(USER_NAME)).size(), 2);
        Assert.assertEquals(authorizationService.getUserGroups(userController.getByUsername(ADMIN_USER_NAME)).size(), 0);
    }

    @Test
    public void getUserRolesInGroupByUser() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO, groupDTO).size(), USER_ROLES.length);


        UserDTO adminDTO = userController.getByUsername(ADMIN_USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(adminDTO, groupDTO).size(), 0);
    }

    @Test
    public void getUserRolesInGroup() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        Assert.assertEquals(authorizationService.getAllRoles(groupDTO).size(), OTHER_USER_ROLES.length);
    }

    @Test
    public void getUserRoles() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO).size(), USER_ROLES.length);

        UserDTO adminDTO = userController.getByUsername(ADMIN_USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(adminDTO).size(), ADMIN_ROLES.length);
    }

    @Test
    public void getUsersWithRoleOnGroup() throws UserManagementException, RoleDoesNotExistsException,
            InvalidCredentialsException, OrganizationDoesNotExistException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        RoleDTO roleDTO = roleController.getByName(USER_ROLES[0]);

        Assert.assertEquals(authorizationService.getUsers(roleDTO, groupDTO).size(), 2);

        roleDTO = roleController.getByName(OTHER_USER_ROLES[1]);
        Assert.assertEquals(authorizationService.getUsers(roleDTO, groupDTO).size(), 1);
    }

    //@Test(priority = 2) //Execute after any other test
    @Test(dependsOnMethods = {"getUsersWithRoleOnGroup", "getUserRoles", "getUserRolesInGroup",
            "getUserRolesInGroupByUser", "getUserGroups", "getRoleById", "getRoleByName", "getAllGroups", "getAllUsersByGroup"})
    public void addUserRole() throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException,
            UserDoesNotExistException, OrganizationDoesNotExistException {
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        RoleDTO roleDTO = roleController.getByName(OTHER_USER_ROLES[1]);
        authorizationService.addUserRole(userDTO, roleDTO);

        Assert.assertEquals(authorizationService.getUserRoles(userDTO).size(), USER_ROLES.length + 1);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO, groupDTO).size(), USER_ROLES.length);
    }

    @Test(dependsOnMethods = "addUserRole")
    public void addGroupUserRole() throws UserManagementException, InvalidCredentialsException,
            UserDoesNotExistException, OrganizationDoesNotExistException, RoleDoesNotExistsException {
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        RoleDTO roleDTO = roleController.getByName(ADMIN_ROLES[0]);
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME, applicationDTO);
        authorizationService.addUserOrganizationRole(userDTO, groupDTO, roleDTO);

        Assert.assertEquals(authorizationService.getUserRoles(userDTO).size(), USER_ROLES.length + 2);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO, groupDTO).size(), USER_ROLES.length + 1);
    }

    @AfterClass(alwaysRun = true)
    public void dropTables() {
        userRoleController.deleteAll();
        groupController.deleteAll();
        applicationController.deleteAll();
        roleController.deleteAll();
        userController.deleteAll();
    }
}

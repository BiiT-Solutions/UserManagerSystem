package com.biit.usermanager.security;

import com.biit.usermanager.core.controller.*;
import com.biit.usermanager.dto.*;
import com.biit.usermanager.security.exceptions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

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

    private static final String ORGANIZATION_NAME = "Organization1";

    private static final String EMPTY_ORGANIZATION_NAME = "Organization2";
    private static final String OTHER_ORGANIZATION_NAME = "Organization3";

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
    private OrganizationController organizationController;

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

    private OrganizationDTO organizationDTO;
    private OrganizationDTO organizationDTO2;

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
        this.applicationDTO = applicationController.create(applicationDTO);
    }

    @BeforeClass
    private void createOrganizations() {
        this.organizationDTO = organizationController.create(new OrganizationDTO(ORGANIZATION_NAME));
        this.organizationDTO2 = organizationController.create(new OrganizationDTO(OTHER_ORGANIZATION_NAME));
        organizationController.create(new OrganizationDTO(EMPTY_ORGANIZATION_NAME));
    }

    @BeforeClass
    private void createRoles() {
        roles = new HashMap<>();
        Set<String> roleNames = new HashSet<>(Arrays.asList(ADMIN_ROLES));
        roleNames.addAll(Arrays.asList(USER_ROLES));
        roleNames.addAll(Arrays.asList(OTHER_USER_ROLES));
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

    @BeforeClass(dependsOnMethods = {"createApplication", "createRoles", "createOrganizations"})
    private void createUserAccount() {
        //Create the test user
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(USER_NAME);
        userDTO.setIdCard(USER_ID_CARD);
        userDTO.setFirstName(USER_FIRST_NAME);
        userDTO.setLastName(USER_LAST_NAME);
        userDTO.setEmail(USER_EMAIL);
        userDTO.setPassword(USER_PASSWORD);
        final UserDTO testUser = userController.create(userDTO);

        //Assign user roles
        for (String userRoles : USER_ROLES) {
            userRoleController.create(new UserRoleDTO(testUser, roles.get(userRoles), organizationDTO, applicationDTO));
            userRoleController.create(new UserRoleDTO(testUser, roles.get(userRoles), organizationDTO2, applicationDTO));
        }
    }

    @BeforeClass(dependsOnMethods = {"createApplication", "createRoles", "createOrganizations"})
    private void createOtherUserAccount() {
        //Create the test user
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(OTHER_USER_NAME);
        userDTO.setIdCard(OTHER_USER_ID_CARD);
        userDTO.setFirstName(OTHER_USER_FIRST_NAME);
        userDTO.setLastName(OTHER_USER_LAST_NAME);
        userDTO.setEmail(OTHER_USER_EMAIL);
        userDTO.setPassword(OTHER_USER_PASSWORD);
        final UserDTO testUser = userController.create(userDTO);

        //Assign user roles
        for (String otherUserRoles : OTHER_USER_ROLES) {
            userRoleController.create(new UserRoleDTO(testUser, roles.get(otherUserRoles), organizationDTO, applicationDTO));
        }
    }


    @BeforeClass
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void getAllUsers() throws UserManagementException, InvalidCredentialsException {
        Assert.assertEquals(authorizationService.getAllUsers().size(), 3);
    }

    @Test
    public void getAllUsersByOrganization() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        OrganizationDTO organizationDTO = organizationController.getByName(ORGANIZATION_NAME);
        Assert.assertEquals(authorizationService.getAllUsers(organizationDTO).size(), 2);
        OrganizationDTO organizationDTO2 = organizationController.getByName(OTHER_ORGANIZATION_NAME);
        Assert.assertEquals(authorizationService.getAllUsers(organizationDTO2).size(), 1);
        OrganizationDTO organizationDTO3 = organizationController.getByName(EMPTY_ORGANIZATION_NAME);
        Assert.assertEquals(authorizationService.getAllUsers(organizationDTO3).size(), 0);
    }

    @Test
    public void getOrganizationById() throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException {
        OrganizationDTO organizationDTO = organizationController.getByName(ORGANIZATION_NAME);
        Assert.assertEquals(authorizationService.getOrganization(organizationDTO.getUniqueId()).getUniqueName(), ORGANIZATION_NAME);
    }

    @Test
    public void getOrganizationByName() throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException {
        OrganizationDTO organizationDTO = organizationController.getByName(ORGANIZATION_NAME);
        Assert.assertEquals(authorizationService.getOrganization(organizationDTO.getUniqueName()).getUniqueName(), ORGANIZATION_NAME);
    }

    @Test
    public void getAllOrganizations() throws UserManagementException, InvalidCredentialsException {
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
    public void getUserOrganizations() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        Assert.assertEquals(authorizationService.getUserOrganizations(userController.getByUsername(USER_NAME)).size(), 2);
        Assert.assertEquals(authorizationService.getUserOrganizations(userController.getByUsername(ADMIN_USER_NAME)).size(), 0);
    }

    @Test
    public void getUserRolesInOrganizationByUser() throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException, UserDoesNotExistException {
        OrganizationDTO organizationDTO = organizationController.getByName(ORGANIZATION_NAME);
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO, organizationDTO).size(), USER_ROLES.length);


        UserDTO adminDTO = userController.getByUsername(ADMIN_USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(adminDTO, organizationDTO).size(), 0);
    }

    @Test
    public void getUserRolesInOrganization() throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException {
        Assert.assertEquals(authorizationService.getAllRoles(organizationDTO).size(), OTHER_USER_ROLES.length);
    }

    @Test
    public void getUserRoles() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO).size(), USER_ROLES.length);

        UserDTO adminDTO = userController.getByUsername(ADMIN_USER_NAME);
        Assert.assertEquals(authorizationService.getUserRoles(adminDTO).size(), ADMIN_ROLES.length);
    }

    @Test
    public void getUsersWithRoleOnOrganization() throws UserManagementException, OrganizationDoesNotExistException,
            RoleDoesNotExistsException, InvalidCredentialsException {
        OrganizationDTO organizationDTO = organizationController.getByName(ORGANIZATION_NAME);
        RoleDTO roleDTO = roleController.getByName(USER_ROLES[0]);

        Assert.assertEquals(authorizationService.getUsers(roleDTO, organizationDTO).size(), 2);

        roleDTO = roleController.getByName(OTHER_USER_ROLES[1]);
        Assert.assertEquals(authorizationService.getUsers(roleDTO, organizationDTO).size(), 1);
    }

    @Test(priority = 2) //Execute after any other test
    public void addUserRole() throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException,
            UserDoesNotExistException, OrganizationDoesNotExistException {
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        RoleDTO roleDTO = roleController.getByName(OTHER_USER_ROLES[1]);
        authorizationService.addUserRole(userDTO, roleDTO);

        Assert.assertEquals(authorizationService.getUserRoles(userDTO).size(), USER_ROLES.length + 1);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO, organizationDTO).size(), USER_ROLES.length);
    }

    @Test(dependsOnMethods = "addUserRole")
    public void addOrganizationUserRole() throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException,
            UserDoesNotExistException, OrganizationDoesNotExistException {
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        RoleDTO roleDTO = roleController.getByName(ADMIN_ROLES[0]);
        OrganizationDTO organizationDTO = organizationController.getByName(ORGANIZATION_NAME);
        authorizationService.addUserOrganizationRole(userDTO, organizationDTO, roleDTO);

        Assert.assertEquals(authorizationService.getUserRoles(userDTO).size(), USER_ROLES.length + 2);
        Assert.assertEquals(authorizationService.getUserRoles(userDTO, organizationDTO).size(), USER_ROLES.length + 1);
    }

    @AfterClass
    public void dropTables() {
        userRoleController.deleteAll();
        applicationController.deleteAll();
        organizationController.deleteAll();
        roleController.deleteAll();
        userController.deleteAll();
    }
}

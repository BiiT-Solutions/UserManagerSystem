package com.biit.usermanager.security;

import com.biit.usermanager.core.controller.*;
import com.biit.usermanager.dto.*;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.OrganizationDoesNotExistException;
import com.biit.usermanager.security.exceptions.RoleDoesNotExistsException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = {"authenticationTests"})
public class AuthorizationTests extends AbstractTestNGSpringContextTests {
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
    private static final String USER_NEW_PASSWORD = "asd12356";
    private static final String USER_ID_CARD = "87654321B";
    private static final String[] USER_ROLES = new String[]{"usermanagersystem_viewer"};

    private static final String NEW_USER_NAME = "NewUser";
    private static final String NEW_USER_NAME_UPDATED = "NewUser2";
    private static final String NEW_USER_EMAIL = "new@test.com";
    private final static String NEW_USER_FIRST_NAME = "New";
    private final static String NEW_USER_LAST_NAME = "User";
    private static final String NEW_USER_PASSWORD = "asd123";
    private static final String NEW_USER_ID_CARD = "1233123123P";
    private static final String[] NEW_USER_ROLES = new String[]{"usermanagersystem_viewer"};

    private static final String ORGANIZATION_NAME = "Organization1";

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

    @BeforeClass(dependsOnMethods = {"createApplication"})
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

        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setName(ORGANIZATION_NAME);
        organizationDTO = organizationController.create(organizationDTO);

        //Assign user roles
        for (String adminRole : USER_ROLES) {
            userRoleController.create(new UserRoleDTO(testUser, roles.get(adminRole), organizationDTO, applicationDTO));
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
        Assert.assertEquals(authorizationService.getAllUsers().size(), 2);
    }

    @Test
    public void getAllUsersByOrganization() throws UserManagementException, InvalidCredentialsException, OrganizationDoesNotExistException {
        OrganizationDTO organizationDTO = organizationController.getByName(ORGANIZATION_NAME);
        Assert.assertEquals(authorizationService.getAllUsers(organizationDTO).size(), 1);
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
        Assert.assertEquals(authorizationService.getAllAvailableOrganizations().size(), 1);
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
}

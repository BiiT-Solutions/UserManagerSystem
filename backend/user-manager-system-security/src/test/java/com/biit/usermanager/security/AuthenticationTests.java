package com.biit.usermanager.security;

import com.biit.server.security.model.AuthRequest;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.GroupController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.controller.UserRoleController;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = {"authenticationTests"})
public class AuthenticationTests extends AbstractTestNGSpringContextTests {

    private static final String ADMIN_USER_NAME = "admin";
    private static final String ADMIN_EMAIL = "admin@test.com";
    private final static String ADMIN_FIRST_NAME = "Admin";
    private final static String ADMIN_LAST_NAME = "User";
    private static final String ADMIN_PASSWORD = "zxc567";
    private static final String ADMIN_ID_CARD = "12345678A";
    private static final String[] ADMIN_ROLES = new String[] {"usermanagersystem_admin", "usermanagersystem_viewer"};

    private static final String USER_NAME = "test";

    private static final String USER_EMAIL = "test@test.com";
    private final static String USER_FIRST_NAME = "Test";
    private final static String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String USER_NEW_PASSWORD = "asd12356";
    private static final String USER_ID_CARD = "87654321B";
    private static final String[] USER_ROLES = new String[] {"usermanagersystem_viewer"};

    private static final String NEW_USER_NAME = "NewUser";
    private static final String NEW_USER_NAME_UPDATED = "NewUser2";
    private static final String NEW_USER_EMAIL = "new@test.com";
    private final static String NEW_USER_FIRST_NAME = "New";
    private final static String NEW_USER_LAST_NAME = "User";
    private static final String NEW_USER_PASSWORD = "asd123";
    private static final String NEW_USER_ID_CARD = "1233123123P";
    private static final String[] NEW_USER_ROLES = new String[] {"usermanagersystem_viewer"};

    private static final String GROUP_NAME = "Group1";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserProvider userProvider;

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
    private AuthenticationService authenticationService;

    @Value("${spring.application.name}")
    private String applicationName;

    private MockMvc mockMvc;

    private String jwtToken;

    private ApplicationDTO applicationDTO;

    private GroupDTO groupDTO;

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
        this.applicationDTO = applicationController.create(applicationDTO, null);
    }

    @BeforeClass
    private void createGroups() {
        this.groupDTO = groupController.create(new GroupDTO(GROUP_NAME), null);
    }

    @BeforeClass
    private void createRoles() {
        roles = new HashMap<>();
        Set<String> roleNames = new HashSet<>(Arrays.asList(ADMIN_ROLES));
        roleNames.addAll(Arrays.asList(USER_ROLES));
        for (String roleName : roleNames) {
            roles.put(roleName, roleController.create(new RoleDTO(roleName, null), null));
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
        for (String adminRole : ADMIN_ROLES) {
            userRoleController.create(new UserRoleDTO(adminUser, roles.get(adminRole), null, applicationDTO), null);
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
        final UserDTO testUser = userController.create(userDTO, null);

        //Assign user roles
        for (String userRoles : USER_ROLES) {
            userRoleController.create(new UserRoleDTO(testUser, roles.get(userRoles), groupDTO, applicationDTO), null);
        }
    }

    @BeforeClass
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void jwtGeneration() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername(ADMIN_USER_NAME);
        request.setPassword(ADMIN_PASSWORD);

        MvcResult createResult = this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        jwtToken = createResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        Assert.assertNotNull(jwtToken);
    }

    @Test(expectedExceptions = UserDoesNotExistException.class)
    public void checkWrongUserWithAdminCredentials() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        System.out.println(" ---------------------- EXPECTED EXCEPTIONS ---------------------------");
        authenticationService.authenticate(USER_EMAIL + "_wrong", USER_PASSWORD);
        System.out.println(" -------------------- END EXPECTED EXCEPTIONS -------------------------");
    }

    @Test
    public void checkUserWithAdminCredentials() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        authenticationService.authenticate(ADMIN_EMAIL, ADMIN_PASSWORD);
        authenticationService.authenticate(USER_EMAIL, USER_PASSWORD);
    }

    @Test
    public void getByEmail() throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
        Assert.assertEquals(authenticationService.getUserByEmail(ADMIN_EMAIL).getUniqueName(), ADMIN_USER_NAME);
        Assert.assertEquals(authenticationService.getUserByEmail(USER_EMAIL).getUniqueName(), USER_NAME);
    }

    @Test
    public void getById() throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException {
        final UserDTO userDTO = (UserDTO) userController.findByUsername(USER_NAME).orElseThrow(() -> new UserDoesNotExistException(""));
        Assert.assertEquals(authenticationService.getUserById(userDTO.getId()).getUniqueName(), USER_NAME);
    }

    @Test
    public void updatePassword() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        UserDTO userDTO = (UserDTO) userController.findByUsername(USER_NAME).orElseThrow(() -> new UserDoesNotExistException(""));
        User databaseUser = userProvider.findByUsername(USER_NAME).orElseThrow(() -> new UserDoesNotExistException(""));
        Assert.assertTrue(BCrypt.checkpw(USER_PASSWORD, databaseUser.getPassword()));
        Assert.assertFalse(BCrypt.checkpw(USER_NEW_PASSWORD, databaseUser.getPassword()));

        authenticationService.updatePassword(userDTO, USER_NEW_PASSWORD);
        databaseUser = userProvider.findByUsername(USER_NAME).orElseThrow(() -> new UserDoesNotExistException(""));

        userDTO = (UserDTO) authenticationService.getUserByEmail(USER_EMAIL);
        Assert.assertTrue(BCrypt.checkpw(USER_NEW_PASSWORD, databaseUser.getPassword()));
        Assert.assertFalse(BCrypt.checkpw(USER_PASSWORD, databaseUser.getPassword()));

        authenticationService.updatePassword(userDTO, USER_PASSWORD);
    }

    @Test
    public void addUser() throws UserManagementException, InvalidCredentialsException {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(NEW_USER_PASSWORD);
        userDTO.setUsername(NEW_USER_NAME);
        userDTO.setEmail(NEW_USER_EMAIL);
        userDTO.setFirstName(NEW_USER_FIRST_NAME);
        userDTO.setLastName(NEW_USER_LAST_NAME);
        userDTO.setIdCard(NEW_USER_ID_CARD);

        userDTO = (UserDTO) authenticationService.addUser(userDTO);
        Assert.assertNotNull(userDTO.getId());
        Assert.assertEquals(userDTO.getUsername(), NEW_USER_NAME);
    }

    @Test(dependsOnMethods = {"addUser"})
    public void updateUser() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        UserDTO userDTO = (UserDTO) userController.findByUsername(NEW_USER_NAME).orElseThrow(() -> new UserDoesNotExistException(""));
        userDTO.setUsername(NEW_USER_NAME_UPDATED);

        authenticationService.updateUser(userDTO);

        userDTO = (UserDTO) authenticationService.getUserByEmail(NEW_USER_EMAIL);
        Assert.assertEquals(userDTO.getUsername(), NEW_USER_NAME_UPDATED);

        userDTO.setUsername(NEW_USER_NAME);
        userDTO = (UserDTO) authenticationService.updateUser(userDTO);
        Assert.assertEquals(userDTO.getUsername(), NEW_USER_NAME);
    }

    @Test(dependsOnMethods = {"updateUser"})
    public void deleteUser() throws UserDoesNotExistException, UserManagementException, InvalidCredentialsException {
        UserDTO userDTO = (UserDTO) userController.findByUsername(NEW_USER_NAME).orElseThrow(() -> new UserDoesNotExistException(""));
        authenticationService.deleteUser(userDTO);
        Assert.assertNull(userController.findByUsername(NEW_USER_NAME).orElse(null));
    }

    @Test
    public void isInGroup() throws UserManagementException, InvalidCredentialsException {
        GroupDTO groupDTO = groupController.getByName(GROUP_NAME);
        UserDTO userDTO = userController.getByUsername(USER_NAME);
        Assert.assertTrue(authenticationService.isInGroup(groupDTO, userDTO));

        GroupDTO otherGroup = new GroupDTO();
        otherGroup.setName("Other Name");
        otherGroup = groupController.create(otherGroup, null);

        Assert.assertFalse(authenticationService.isInGroup(otherGroup, userDTO));
    }

    @Test
    public void getDefaultGroup() throws UserManagementException, InvalidCredentialsException, UserDoesNotExistException {
        UserDTO userDTO = (UserDTO) userController.findByUsername(USER_NAME).orElseThrow(() -> new UserDoesNotExistException(""));
        GroupDTO groupDTO = (GroupDTO) authenticationService.getDefaultGroup(userDTO);
        Assert.assertNotNull(groupDTO);
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

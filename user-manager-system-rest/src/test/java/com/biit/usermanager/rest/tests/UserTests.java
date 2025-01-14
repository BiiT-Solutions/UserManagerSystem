package com.biit.usermanager.rest.tests;

import com.biit.server.security.model.AuthRequest;
import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.ApplicationRoleController;
import com.biit.usermanager.core.controller.BackendServiceController;
import com.biit.usermanager.core.controller.BackendServiceRoleController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = "userTests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserTests extends AbstractTestNGSpringContextTests {

    private static final String USER_NAME = "admin";
    private static final String USER_UNIQUE_ID = "00000000AA";
    private static final String USER_FIRST_NAME = "Test";
    private static final String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String[] APPLICATION_ROLES = new String[]{"WRITER", "READER"};
    private static final String[] BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};
    private static final String APPLICATION_NAME = "DASHBOARD";

    private static final String USER2_NAME = "user2";
    private static final String USER2_UNIQUE_ID = "1111111BB";
    private final static String USER2_FIRST_NAME = "Test2";
    private final static String USER2_LAST_NAME = "User2";
    private static final String USER2_PASSWORD = "password";

    private final static String USER2_NEW_FIRST_NAME = "New Test2";
    private final static String USER2_NEW_LAST_NAME = "New  User2";

    private static final String USER3_NAME = "user3";
    private static final String USER3_UNIQUE_ID = "2222222CC";
    private final static String USER3_FIRST_NAME = "Test3";
    private final static String USER3_LAST_NAME = "User3";
    private static final String USER3_PASSWORD = "password";


    @Value("${bcrypt.salt:}")
    private String bcryptSalt;

    @Autowired
    private UserController userController;

    @Autowired
    private RoleController roleController;

    @Autowired
    private ApplicationController applicationController;

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
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.application.name}")
    private String backendService;

    private MockMvc mockMvc;

    private String jwtToken;

    private UserDTO admin;

    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T fromJson(String payload, Class<T> clazz) throws IOException {
        return objectMapper.readValue(payload, clazz);
    }


    @BeforeClass
    public void createAdminUser() {
        //Create the admin user
        admin = (UserDTO) userController.createUser(USER_NAME, USER_UNIQUE_ID, USER_FIRST_NAME, USER_LAST_NAME, USER_PASSWORD, null, null);

        //Create the application
        final ApplicationDTO applicationDTO = applicationController.create(new ApplicationDTO(APPLICATION_NAME, ""), null);

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

    @BeforeClass
    public void startServer() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void login() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername(USER_NAME);
        request.setPassword(USER_PASSWORD);

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

    @Test(dependsOnMethods = "login")
    public void updateUser() throws Exception {

        //Create the admin user
        final UserDTO user2 = (UserDTO) userController.createUser(USER2_NAME, USER2_UNIQUE_ID, USER2_FIRST_NAME, USER2_LAST_NAME, USER2_PASSWORD, null, null);

        //I can log in.
        AuthRequest request = new AuthRequest();
        request.setUsername(USER2_NAME);
        request.setPassword(USER2_PASSWORD);

        this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        //Change user data.
        user2.setFirstname(USER2_NEW_FIRST_NAME);
        user2.setLastname(USER2_NEW_LAST_NAME);

        this.mockMvc
                .perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(user2))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        //Ensure that the password is not updated.
        request = new AuthRequest();
        request.setUsername(USER2_NAME);
        request.setPassword(USER2_PASSWORD);

        MvcResult createResult = this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        UserDTO authenticatedUser = fromJson(createResult.getResponse().getContentAsString(), UserDTO.class);
        Assert.assertEquals(authenticatedUser.getName(), USER2_NEW_FIRST_NAME);

    }


    @Test(dependsOnMethods = "login")
    public void searchByUUID() throws Exception {
        final MvcResult userResult = this.mockMvc
                .perform(get("/users/uuids").param("uuids", admin.getUID())
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<UserDTO> users = Arrays.asList(fromJson(userResult.getResponse().getContentAsString(), UserDTO[].class));
        Assert.assertEquals(users.size(), 1);
    }


    @Test(dependsOnMethods = "login")
    public void accountExpired() throws Exception {

        //Create a new user
        final UserDTO user2 = (UserDTO) userController.createUser(USER3_NAME, USER3_UNIQUE_ID, USER3_FIRST_NAME, USER3_LAST_NAME, USER3_PASSWORD, null, null);

        //I can log in.
        AuthRequest request = new AuthRequest();
        request.setUsername(USER3_NAME);
        request.setPassword(USER3_PASSWORD);

        this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        //Change user data.
        final LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(1);
        user2.setAccountExpirationTime(expirationTime);

        this.mockMvc
                .perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(user2))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        //Ensure that I cannot use the token anymore.
        this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isLocked())
                .andExpect(MockMvcResultMatchers.header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andReturn();


        MvcResult createResult = this.mockMvc
                .perform(get("/users/" + user2.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        UserDTO authenticatedUser = fromJson(createResult.getResponse().getContentAsString(), UserDTO.class);
        Assert.assertEquals(authenticatedUser.getAccountExpirationTime().withNano(0), expirationTime.withNano(0));
    }

}

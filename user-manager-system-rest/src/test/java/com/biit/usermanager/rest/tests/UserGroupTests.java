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
import com.biit.usermanager.dto.UserGroupDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = "userGroupTests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserGroupTests extends AbstractTestNGSpringContextTests {
    private static final String USER_NAME = "admin";
    private static final String USER_UNIQUE_ID = "00000000AA";
    private static final String USER_FIRST_NAME = "Test";
    private static final String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String[] APPLICATION_ROLES = new String[]{"WRITER", "READER"};
    private static final String[] BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};
    private static final String APPLICATION_NAME = "DASHBOARD";

    private static final String USER1_NAME = "user1";
    private static final String USER1_UNIQUE_ID = "1111111BB";
    private final static String USER1_FIRST_NAME = "Test1";
    private final static String USER1_LAST_NAME = "User1";
    private static final String USER1_PASSWORD = "password";

    private static final String USER2_NAME = "user2";
    private static final String USER2_UNIQUE_ID = "22222222BB";
    private final static String USER2_FIRST_NAME = "Test2";
    private final static String USER2_LAST_NAME = "User2";
    private static final String USER2_PASSWORD = "password";

    private static final String USER3_NAME = "user3";
    private static final String USER3_UNIQUE_ID = "33333333BB";
    private final static String USER3_FIRST_NAME = "Test3";
    private final static String USER3_LAST_NAME = "User3";
    private static final String USER3_PASSWORD = "password";

    private static final String USER_GROUP1 = "group1";
    private static final String USER_GROUP2 = "group2";

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
    private UserDTO user1;
    private UserDTO user2;
    private UserDTO user3;

    private UserGroupDTO userGroup1;
    private UserGroupDTO userGroup2;

    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T fromJson(String payload, Class<T> clazz) throws IOException {
        return objectMapper.readValue(payload, clazz);
    }


    @BeforeClass
    public void createAdminUser() {
        //Create the admin user
        admin = userController.createUser(USER_NAME, USER_UNIQUE_ID, USER_FIRST_NAME, USER_LAST_NAME, USER_PASSWORD, null, null);

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
    public void createExtraUsers() {
        user1 = userController.createUser(USER1_NAME, USER1_UNIQUE_ID, USER1_FIRST_NAME, USER1_LAST_NAME, USER1_PASSWORD, null, null);
        user2 = userController.createUser(USER2_NAME, USER2_UNIQUE_ID, USER2_FIRST_NAME, USER2_LAST_NAME, USER2_PASSWORD, null, null);
        user3 = userController.createUser(USER3_NAME, USER3_UNIQUE_ID, USER3_FIRST_NAME, USER3_LAST_NAME, USER3_PASSWORD, null, null);
    }

    @BeforeClass
    public void startServer() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void checkPassword() {
        userController.checkPassword(USER_NAME, USER_PASSWORD);
    }


    @Test(dependsOnMethods = "checkPassword")
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
    public void createUserGroup() throws Exception {
        UserGroupDTO groupDTO1 = new UserGroupDTO(USER_GROUP1, "");

        MvcResult createResult1 = this.mockMvc
                .perform(put("/user-groups")
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(groupDTO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        userGroup1 = fromJson(createResult1.getResponse().getContentAsString(), UserGroupDTO.class);
        Assert.assertEquals(userGroup1.getName(), USER_GROUP1);

        UserGroupDTO groupDTO2 = new UserGroupDTO(USER_GROUP2, "");

        MvcResult createResult2 = this.mockMvc
                .perform(put("/user-groups")
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(groupDTO2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        userGroup2 = fromJson(createResult2.getResponse().getContentAsString(), UserGroupDTO.class);
        Assert.assertEquals(userGroup2.getName(), USER_GROUP2);
    }


    @Test(dependsOnMethods = "createUserGroup")
    public void assignUserGroupRoles() throws Exception {
        this.mockMvc
                .perform(post("/user-groups/id/" + userGroup1.getId() + "/applications/" + APPLICATION_NAME + "/application-roles/READER")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        this.mockMvc
                .perform(post("/user-groups/id/" + userGroup2.getId() + "/applications/" + APPLICATION_NAME + "/application-roles/WRITER")
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    @Test(dependsOnMethods = "createUserGroup")
    public void AssignToGroupByMultiplesUUID() throws Exception {
        final List<UUID> request = List.of(user3.getUUID(), admin.getUUID());

        this.mockMvc
                .perform(post("/user-groups/" + userGroup1.getId() + "/users/uuids")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request))
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    @Test(dependsOnMethods = "createUserGroup")
    public void AssignToGroupByMultiplesUsernames() throws Exception {
        final List<String> request = List.of(user1.getUsername(), user2.getUsername());

        this.mockMvc
                .perform(post("/user-groups/" + userGroup2.getId() + "/users/usernames")
                        .content(toJson(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    @Test(dependsOnMethods = {"AssignToGroupByMultiplesUUID", "AssignToGroupByMultiplesUsernames"})
    public void getRolesFromUserByApplication() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername(user1.getUsername());
        request.setPassword(user1.getPassword());

        MvcResult createResult = this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        final UserDTO storedUser =
                objectMapper.readValue(createResult.getResponse().getContentAsString(), UserDTO.class);
        Assert.assertEquals(storedUser.getApplicationRoles().size(), 1);
        Assert.assertEquals(storedUser.getApplicationRoles().iterator().next(), "DASHBOARD_WRITER");




        //Get User2
        MvcResult userResult = this.mockMvc
                .perform(get("/users/uuids/" + user3.getUUID())
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        final UserDTO storedUser3 =
                objectMapper.readValue(userResult.getResponse().getContentAsString(), UserDTO.class);

        Assert.assertEquals(storedUser3.getApplicationRoles().size(), 1);
        Assert.assertEquals(storedUser3.getApplicationRoles().iterator().next(), "DASHBOARD_READER");
    }
}


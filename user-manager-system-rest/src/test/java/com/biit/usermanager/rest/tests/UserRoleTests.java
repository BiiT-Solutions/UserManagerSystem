package com.biit.usermanager.rest.tests;

/*-
 * #%L
 * User Manager System (Rest)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = "userRoleTests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserRoleTests extends AbstractTestNGSpringContextTests {

    private static final String USER_NAME = "admin";
    private static final String USER_UNIQUE_ID = "00000000AA";
    private static final String USER_FIRST_NAME = "Test";
    private static final String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String[] APPLICATION_ROLES = new String[]{"WRITER", "READER"};
    private static final String[] BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};
    private static final String APPLICATION_NAME = "DASHBOARD";

    /* New DATA */

    private static final String NEW_APPLICATION_NAME = "Jarvis";
    private static final String NEW_ROLE_NAME = "IronMan";

    private static final String NEW_BACKEND_NAME = "Armour";
    private static final String NEW_BACKEND_ROLE_NAME = "Owner";

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

    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T fromJson(String payload, Class<T> clazz) throws IOException {
        return objectMapper.readValue(payload, clazz);
    }


    @BeforeClass
    public void createAdminUser() {
        //Create the admin user
        final UserDTO admin = userController.createUser(USER_NAME, USER_UNIQUE_ID, USER_FIRST_NAME, USER_LAST_NAME, USER_PASSWORD, null, null);

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
    public void checkPassword() {
        userController.checkPassword(USER_NAME, USER_PASSWORD);
    }

    @Test
    public void checkPublicRestService() throws Exception {
        //Info services are opened in rest-server library
        mockMvc.perform(get("/info/health-check")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
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
    public void createApplication() throws Exception {
        Assert.assertNotNull(jwtToken);

        MvcResult createResult = this.mockMvc
                .perform(post("/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(new ApplicationDTO(NEW_APPLICATION_NAME)))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ApplicationDTO applicationDTO = fromJson(createResult.getResponse().getContentAsString(), ApplicationDTO.class);
        Assert.assertEquals(applicationDTO.getCreatedBy(), USER_NAME);
        Assert.assertEquals(applicationDTO.getName(), NEW_APPLICATION_NAME);
    }

    @Test(dependsOnMethods = "createApplication")
    public void createRoles() throws Exception {
        Assert.assertNotNull(jwtToken);

        MvcResult createResult = this.mockMvc
                .perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(new RoleDTO(NEW_ROLE_NAME, "")))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        RoleDTO roleDTO = fromJson(createResult.getResponse().getContentAsString(), RoleDTO.class);
        Assert.assertEquals(roleDTO.getCreatedBy(), USER_NAME);
        Assert.assertEquals(roleDTO.getName(), NEW_ROLE_NAME);
    }

    @Test(dependsOnMethods = {"createApplication", "createRoles"})
    public void createApplicationRoles() throws Exception {
        Assert.assertNotNull(jwtToken);

        //Get Application
        MvcResult applicationResult = this.mockMvc
                .perform(get("/applications/" + NEW_APPLICATION_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        ApplicationDTO applicationDTO = fromJson(applicationResult.getResponse().getContentAsString(), ApplicationDTO.class);

        //Get Role
        MvcResult roleResult = this.mockMvc
                .perform(get("/roles/" + NEW_ROLE_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        RoleDTO roleDTO = fromJson(roleResult.getResponse().getContentAsString(), RoleDTO.class);

        //Assign Role
        MvcResult createResult = this.mockMvc
                .perform(post("/application-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(new ApplicationRoleDTO(applicationDTO, roleDTO)))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ApplicationRoleDTO applicationRoleDTO = fromJson(createResult.getResponse().getContentAsString(), ApplicationRoleDTO.class);
        Assert.assertEquals(applicationRoleDTO.getCreatedBy(), USER_NAME);
        Assert.assertEquals(applicationRoleDTO.getId().getApplication(), applicationDTO);
        Assert.assertEquals(applicationRoleDTO.getId().getRole(), roleDTO);
    }

    @Test(dependsOnMethods = "login")
    public void createBackend() throws Exception {
        Assert.assertNotNull(jwtToken);

        MvcResult createResult = this.mockMvc
                .perform(post("/backend-services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(new BackendServiceDTO(NEW_BACKEND_NAME)))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        BackendServiceDTO backendServiceDTO = fromJson(createResult.getResponse().getContentAsString(), BackendServiceDTO.class);
        Assert.assertEquals(backendServiceDTO.getCreatedBy(), USER_NAME);
        Assert.assertEquals(backendServiceDTO.getName(), NEW_BACKEND_NAME);
    }

    @Test(dependsOnMethods = "createBackend")
    public void createBackendRoles() throws Exception {
        Assert.assertNotNull(jwtToken);

        //Get Backend
        MvcResult backendResult = this.mockMvc
                .perform(get("/backend-services/" + NEW_BACKEND_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        BackendServiceDTO backendServiceDTO = fromJson(backendResult.getResponse().getContentAsString(), BackendServiceDTO.class);

        MvcResult createResult = this.mockMvc
                .perform(post("/backend-service-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(new BackendServiceRoleDTO(backendServiceDTO, NEW_BACKEND_ROLE_NAME)))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        BackendServiceRoleDTO backendServiceRoleDTO = fromJson(createResult.getResponse().getContentAsString(), BackendServiceRoleDTO.class);
        Assert.assertEquals(backendServiceRoleDTO.getCreatedBy(), USER_NAME);
        Assert.assertEquals(backendServiceRoleDTO.getId().getName(), NEW_BACKEND_ROLE_NAME);
    }

    @Test(dependsOnMethods = {"createBackendRoles", "createApplicationRoles"})
    public void assignApplicationRolesToBackend() throws Exception {
        //Get ApplicationRole
        MvcResult applicationRoleResult = this.mockMvc
                .perform(get("/application-roles/applications/" + NEW_APPLICATION_NAME + "/roles/" + NEW_ROLE_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        ApplicationRoleDTO applicationRoleDTO = fromJson(applicationRoleResult.getResponse().getContentAsString(), ApplicationRoleDTO.class);

        //Get BackendServiceRole
        MvcResult backendServiceRoleResult = this.mockMvc
                .perform(get("/backend-service-roles/backend-services/" + NEW_BACKEND_NAME + "/roles/" + NEW_BACKEND_ROLE_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        BackendServiceRoleDTO backendServiceRoleDTO = fromJson(backendServiceRoleResult.getResponse().getContentAsString(), BackendServiceRoleDTO.class);

        //Assign
        MvcResult createResult = this.mockMvc
                .perform(post("/application-backend-service-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(new ApplicationBackendServiceRoleDTO(applicationRoleDTO, backendServiceRoleDTO)))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ApplicationBackendServiceRoleDTO applicationBackendServiceRoleDTO = fromJson(createResult.getResponse().getContentAsString(), ApplicationBackendServiceRoleDTO.class);
        Assert.assertEquals(applicationBackendServiceRoleDTO.getCreatedBy(), USER_NAME);
        Assert.assertEquals(applicationBackendServiceRoleDTO.getId().getApplicationRole().getId().getApplication().getName(), NEW_APPLICATION_NAME);
        Assert.assertEquals(applicationBackendServiceRoleDTO.getId().getApplicationRole().getId().getRole().getName(), NEW_ROLE_NAME);
        Assert.assertEquals(applicationBackendServiceRoleDTO.getId().getBackendServiceRole().getId().getBackendService().getName(), NEW_BACKEND_NAME);
        Assert.assertEquals(applicationBackendServiceRoleDTO.getId().getBackendServiceRole().getId().getName(), NEW_BACKEND_ROLE_NAME);
    }

    @Test(dependsOnMethods = "assignApplicationRolesToBackend")
    public void assignRolesToUser() throws Exception {
        //Get User
        MvcResult userResult = this.mockMvc
                .perform(get("/users/usernames/" + USER_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        UserDTO user = fromJson(userResult.getResponse().getContentAsString(), UserDTO.class);

        //Assign permission
        MvcResult applicationBackendServiceRoleResult = this.mockMvc
                .perform(post("/users/usernames/" + USER_NAME
                        + "/applications/" + NEW_APPLICATION_NAME
                        + "/application-roles/" + NEW_ROLE_NAME
                        + "/backend-services/" + NEW_BACKEND_NAME
                        + "/backend-service-roles/" + NEW_BACKEND_ROLE_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        final UserDTO userDTO = fromJson(applicationBackendServiceRoleResult.getResponse().getContentAsString(), UserDTO.class);
        Assert.assertTrue(userDTO.getGrantedAuthorities().contains(NEW_BACKEND_NAME.toUpperCase() + "_" + NEW_BACKEND_ROLE_NAME.toUpperCase()));
        Assert.assertTrue(userDTO.getApplicationRoles().contains(NEW_APPLICATION_NAME.toUpperCase() + "_" + NEW_ROLE_NAME.toUpperCase()));
    }

    @Test(dependsOnMethods = "assignRolesToUser")
    public void deleteRolesFromUser() throws Exception {
        //Get User
        MvcResult userResult = this.mockMvc
                .perform(get("/users/usernames/" + USER_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        UserDTO user = fromJson(userResult.getResponse().getContentAsString(), UserDTO.class);

        //Delete permission
        MvcResult applicationBackendServiceRoleResult = this.mockMvc
                .perform(delete("/users/usernames/" + USER_NAME
                        + "/applications/" + NEW_APPLICATION_NAME
                        + "/application-roles/" + NEW_ROLE_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        final UserDTO userDTO = fromJson(applicationBackendServiceRoleResult.getResponse().getContentAsString(), UserDTO.class);
        Assert.assertFalse(userDTO.getGrantedAuthorities().contains(NEW_BACKEND_NAME.toUpperCase() + "_" + NEW_BACKEND_ROLE_NAME.toUpperCase()));
        Assert.assertFalse(userDTO.getApplicationRoles().contains(NEW_APPLICATION_NAME.toUpperCase() + "_" + NEW_ROLE_NAME.toUpperCase()));
    }

    @Test(dependsOnMethods = "deleteRolesFromUser")
    public void assignRolesFromUserByApplication() throws Exception {
        //Get User
        MvcResult userResult = this.mockMvc
                .perform(get("/users/usernames/" + USER_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        UserDTO user = fromJson(userResult.getResponse().getContentAsString(), UserDTO.class);

        //Assign permissions again, but only by application
        MvcResult applicationBackendServiceRoleResult = this.mockMvc
                .perform(post("/users/usernames/" + USER_NAME
                        + "/applications/" + NEW_APPLICATION_NAME
                        + "/application-roles/" + NEW_ROLE_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        final UserDTO userDTO = fromJson(applicationBackendServiceRoleResult.getResponse().getContentAsString(), UserDTO.class);
        Assert.assertTrue(userDTO.getGrantedAuthorities().contains(NEW_BACKEND_NAME.toUpperCase() + "_" + NEW_BACKEND_ROLE_NAME.toUpperCase()));
        Assert.assertTrue(userDTO.getApplicationRoles().contains(NEW_APPLICATION_NAME.toUpperCase() + "_" + NEW_ROLE_NAME.toUpperCase()));
    }

    @Test(dependsOnMethods = "deleteRolesFromUser")
    public void getRolesFromUserByApplication() throws Exception {
        //Get User
        MvcResult userResult = this.mockMvc
                .perform(get("/application-backend-service-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOS =
                Arrays.asList(objectMapper.readValue(userResult.getResponse().getContentAsString(), ApplicationBackendServiceRoleDTO[].class));

        Assert.assertEquals(applicationBackendServiceRoleDTOS.size(), 5);

        userResult = this.mockMvc
                .perform(get("/application-backend-service-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        applicationBackendServiceRoleDTOS =
                Arrays.asList(objectMapper.readValue(userResult.getResponse().getContentAsString(), ApplicationBackendServiceRoleDTO[].class));

        Assert.assertEquals(applicationBackendServiceRoleDTOS.size(), 5);
    }

    @Test(dependsOnMethods = "login")
    public void createInvalidRoles() throws Exception {
        Assert.assertNotNull(jwtToken);

        this.mockMvc
                .perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(toJson(new RoleDTO()))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }
}

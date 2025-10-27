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

import com.biit.server.security.exceptions.ActionForbiddenByConflictingData;
import com.biit.server.security.model.AuthRequest;
import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.ApplicationRoleController;
import com.biit.usermanager.core.controller.BackendServiceController;
import com.biit.usermanager.core.controller.BackendServiceRoleController;
import com.biit.usermanager.core.controller.OrganizationController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.TeamController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.TeamDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = "organizationAdminTests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrganizationAdminTests extends AbstractTestNGSpringContextTests {

    private static final String ADMIN_USER_NAME = "admin";
    private static final String ADMIN_USER_UNIQUE_ID = "00000000AA";
    private static final String ADMIN_USER_FIRST_NAME = "Test";
    private static final String ADMIN_USER_LAST_NAME = "User";
    private static final String ADMIN_USER_PASSWORD = "asd123";
    private static final String[] ADMIN_APPLICATION_ROLES = new String[]{"WRITER", "READER"};
    private static final String[] ADMIN_BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};

    private static final String APPLICATION_NAME = "DASHBOARD";


    private static final String ORG_USER_NAME = "orgAdmin";
    private static final String ORG_USER_UNIQUE_ID = "11111111BB";
    private static final String ORG_USER_FIRST_NAME = "OrgAdmin";
    private static final String ORG_USER_LAST_NAME = "User";
    private static final String ORG_USER_PASSWORD = "asd123";
    private static final String[] ORG_APPLICATION_ROLES = new String[]{"ORG_ADMIN"};
    private static final String[] ORG_BACKEND_ROLES = new String[]{"ORGANIZATION_ADMIN"};

    private static final String ORGANIZATION_NAME = "Umbrella";
    private static final String NEW_ORGANIZATION_NAME = "Academy";
    private static final String TEAM_NAME = "TeamA";
    private static final String NEW_TEAM_NAME = "TeamB";

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
    private OrganizationController organizationController;

    @Autowired
    private TeamController teamController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.application.name}")
    private String backendService;

    private MockMvc mockMvc;

    private String adminJwtToken;
    private String orgAdminJwtToken;

    private BackendServiceDTO backendServiceDTO;

    private ApplicationDTO applicationDTO;

    private TeamDTO teamDTO;

    private TeamDTO secondTeamDTO;

    private UserDTO orgAdmin;

    private UserDTO admin;

    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T fromJson(String payload, Class<T> clazz) throws IOException {
        return objectMapper.readValue(payload, clazz);
    }

    @BeforeClass
    public void createOrganization() {
        final OrganizationDTO organizationDTO = organizationController.create(new OrganizationDTO(ORGANIZATION_NAME), null);
        teamDTO = teamController.create(new TeamDTO(TEAM_NAME, organizationDTO), null);
    }


    @BeforeClass(dependsOnMethods = "createOrganization")
    public void createAdminUser() {
        //Create the admin user
        admin = userController.createUser(ADMIN_USER_NAME, ADMIN_USER_UNIQUE_ID, ADMIN_USER_FIRST_NAME, ADMIN_USER_LAST_NAME, ADMIN_USER_PASSWORD, null, null);

        //Create the application
        applicationDTO = applicationController.create(new ApplicationDTO(APPLICATION_NAME, ""), null);

        //Set the application roles
        final List<RoleDTO> roleDTOs = new ArrayList<>();
        for (final String roleName : ADMIN_APPLICATION_ROLES) {
            roleDTOs.add(roleController.create(new RoleDTO(roleName, null), null));
        }

        //Assign the application roles.
        List<ApplicationRoleDTO> applicationRoles = new ArrayList<>();
        roleDTOs.forEach(roleDTO -> applicationRoles.add(applicationRoleController.create(new ApplicationRoleDTO(applicationDTO, roleDTO), null)));

        //Set the backend roles.
        backendServiceDTO = backendServiceController.create(new BackendServiceDTO(backendService), null);

        final List<BackendServiceRoleDTO> backendRoles = new ArrayList<>();
        for (final String roleName : ADMIN_BACKEND_ROLES) {
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

    @BeforeClass(dependsOnMethods = "createAdminUser")
    public void createOrganizationAdminUser() {
        //Create the admin user
        orgAdmin = userController.createUser(ORG_USER_NAME, ORG_USER_UNIQUE_ID, ORG_USER_FIRST_NAME, ORG_USER_LAST_NAME, ORG_USER_PASSWORD, null, null);

        //Set the application roles
        final List<RoleDTO> roleDTOs = new ArrayList<>();
        for (final String roleName : ORG_APPLICATION_ROLES) {
            roleDTOs.add(roleController.create(new RoleDTO(roleName, null), null));
        }

        //Assign the application roles.
        List<ApplicationRoleDTO> applicationRoles = new ArrayList<>();
        roleDTOs.forEach(roleDTO -> applicationRoles.add(applicationRoleController.create(new ApplicationRoleDTO(applicationDTO, roleDTO), null)));


        final List<BackendServiceRoleDTO> backendRoles = new ArrayList<>();
        for (final String roleName : ORG_BACKEND_ROLES) {
            backendRoles.add(backendServiceRoleController.create(new BackendServiceRoleDTO(backendServiceDTO, roleName), null));
        }

        //Assign the backend to an application.
        final List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOs = new ArrayList<>();
        for (ApplicationRoleDTO applicationRole : applicationRoles) {
            for (BackendServiceRoleDTO backendRole : backendRoles) {
                applicationBackendServiceRoleDTOs.add(applicationBackendServiceRoleController.create(new ApplicationBackendServiceRoleDTO(applicationRole, backendRole), null));
            }
        }

        userController.setApplicationBackendServiceRole(orgAdmin, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOs));

        //Assign user to the organization by a team.
        teamController.assign(teamDTO.getId(), orgAdmin, ORG_USER_NAME);
    }

    @BeforeClass
    public void createSecondOrganization() {
        final OrganizationDTO organizationDTO = organizationController.create(new OrganizationDTO(NEW_ORGANIZATION_NAME), null);
        secondTeamDTO = teamController.create(new TeamDTO(NEW_TEAM_NAME, organizationDTO), null);
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
        request.setUsername(ADMIN_USER_NAME);
        request.setPassword(ADMIN_USER_PASSWORD);

        MvcResult createResult = this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        adminJwtToken = createResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        Assert.assertNotNull(adminJwtToken);

        request = new AuthRequest();
        request.setUsername(ORG_USER_NAME);
        request.setPassword(ORG_USER_PASSWORD);

        createResult = this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        orgAdminJwtToken = createResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        Assert.assertNotNull(orgAdminJwtToken);
    }

    private UserDTO createUser(String username) {
        final UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(ADMIN_USER_PASSWORD);
        userDTO.setFirstname(username);
        userDTO.setLastname(username);
        userDTO.setEmail(username + "@test.com");
        return userDTO;
    }


    @Test(dependsOnMethods = "login")
    public void createUsers() throws Exception {
        final UserDTO user1 = createUser("user1");

        this.mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(user1))
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        final UserDTO user2 = createUser("user2");

        this.mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(user2))
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        final UserDTO user3 = createUser("user3");

        this.mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(user3))
                        .header("Authorization", "Bearer " + orgAdminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
    }


    @Test(dependsOnMethods = "createUsers")
    public void checkUsers() throws Exception {

        MvcResult adminGetResult = this.mockMvc
                .perform(get("/users")
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult orgAdminGetResult = this.mockMvc
                .perform(get("/users")
                        .header("Authorization", "Bearer " + orgAdminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<UserDTO> usersSearchedByAdmin = Arrays.asList(fromJson(adminGetResult.getResponse().getContentAsString(), UserDTO[].class));
        final List<UserDTO> usersSearchedByOrgAdmin = Arrays.asList(fromJson(orgAdminGetResult.getResponse().getContentAsString(), UserDTO[].class));
        //Admin, Org Admin  + 3 new users.
        Assert.assertEquals(usersSearchedByAdmin.size(), 1 + 1 + 3);
        //Org Admin + 1 new user.
        Assert.assertEquals(usersSearchedByOrgAdmin.size(), 2);
    }


    @Test(dependsOnMethods = "createUsers")
    public void someEntitiesAreRestrictedToOrgAdmin() throws Exception {
        MvcResult adminGetResult = this.mockMvc
                .perform(get("/applications")
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult orgAdminGetResult = this.mockMvc
                .perform(get("/applications")
                        .header("Authorization", "Bearer " + orgAdminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<ApplicationDTO> adminApplicationDTOS = Arrays.asList(fromJson(adminGetResult.getResponse().getContentAsString(), ApplicationDTO[].class));
        final List<ApplicationDTO> orgApplicationDTOS = Arrays.asList(fromJson(orgAdminGetResult.getResponse().getContentAsString(), ApplicationDTO[].class));
        Assert.assertEquals(adminApplicationDTOS.size(), 1);
        Assert.assertEquals(orgApplicationDTOS.size(), 0);
    }

    @Test(dependsOnMethods = "createUsers")
    public void organizationEntitiesAreAvailableToOrgAdmin() throws Exception {
        MvcResult adminGetResult = this.mockMvc
                .perform(get("/organizations")
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult orgAdminGetResult = this.mockMvc
                .perform(get("/organizations")
                        .header("Authorization", "Bearer " + orgAdminJwtToken)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<OrganizationDTO> adminOrganizationsDTOS = Arrays.asList(fromJson(adminGetResult.getResponse().getContentAsString(), OrganizationDTO[].class));
        final List<OrganizationDTO> orgAdminOrganizationsDTOS = Arrays.asList(fromJson(orgAdminGetResult.getResponse().getContentAsString(), OrganizationDTO[].class));
        Assert.assertEquals(adminOrganizationsDTOS.size(), 2);
        Assert.assertEquals(orgAdminOrganizationsDTOS.size(), 1);
    }


    @Test(expectedExceptions = ActionForbiddenByConflictingData.class)
    public void anOrganizationAdminCanOnlyBePresentOnASingleOrganization() {
        //Assign user to a different organization by a team.
        teamController.assign(secondTeamDTO.getId(), orgAdmin, ORG_USER_NAME);
    }

    @Test
    public void anAdminCanBePresentOnMultipleOrganization() {
        //Assign user to a different organization by a team.
        teamController.assign(secondTeamDTO.getId(), admin, ORG_USER_NAME);
    }
}

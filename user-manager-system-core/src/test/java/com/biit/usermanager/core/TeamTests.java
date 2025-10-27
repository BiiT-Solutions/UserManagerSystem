package com.biit.usermanager.core;

/*-
 * #%L
 * User Manager System (core)
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

import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.ApplicationRoleController;
import com.biit.usermanager.core.controller.BackendServiceController;
import com.biit.usermanager.core.controller.BackendServiceRoleController;
import com.biit.usermanager.core.controller.OrganizationController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.TeamController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.providers.UserGroupApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Test(groups = "teamTests")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TeamTests extends AbstractTestNGSpringContextTests {

    private static final String USER_1_NAME = "JosephColton";
    private static final String USER_1_UNIQUE_ID = "00000000AA";
    private static final String USER_1_FIRST_NAME = "Joseph";
    private static final String USER_1_LAST_NAME = "Colton";
    private static final String USER_1_PASSWORD = "asd123";

    private static final String[] APPLICATION_ROLES = new String[]{"WRITER", "READER"};
    private static final String[] BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};
    private static final String APPLICATION_NAME = "DASHBOARD";
    private static final String ORGANIZATION_NAME = "HASBRO";
    private static final String PARENT_TEAM_NAME = "GIJOE";
    private static final String TEAM_NAME = "NINJA";


    @Autowired
    private UserController userController;

    @Autowired
    private TeamController teamController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private RoleController roleController;

    @Autowired
    private ApplicationRoleController applicationRoleController;

    @Autowired
    private OrganizationController organizationController;

    @Autowired
    private BackendServiceController backendServiceController;

    @Autowired
    private BackendServiceRoleController backendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleController applicationBackendServiceRoleController;

    @Autowired
    private UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider;

    @Value("${spring.application.name}")
    private String backendService;

    private TeamDTO parentTeamDTO;
    private TeamDTO teamDTO;

    private UserDTO user1;

    private ApplicationDTO applicationDTO;

    private final List<RoleDTO> roleDTOs = new ArrayList<>();

    private final List<ApplicationRoleDTO> applicationRoles = new ArrayList<>();

    private BackendServiceDTO backendServiceDTO;

    private final List<BackendServiceRoleDTO> backendRoles = new ArrayList<>();

    private OrganizationDTO organizationDTO;


    @BeforeClass
    public void createUsers() {
        //Create the admin user
        user1 = userController.createUser(USER_1_NAME, USER_1_UNIQUE_ID, USER_1_FIRST_NAME, USER_1_LAST_NAME, USER_1_PASSWORD, null, null);
    }


    @BeforeClass
    public void createOrganization() {
        organizationDTO = organizationController.create(new OrganizationDTO(ORGANIZATION_NAME), null);
    }


    @BeforeClass(dependsOnMethods = "createOrganization")
    public void createParentTeam() {
        parentTeamDTO = teamController.create(new TeamDTO(PARENT_TEAM_NAME, organizationDTO), null);
    }


    @BeforeClass(dependsOnMethods = "createParentTeam")
    public void createTeam() {
        teamDTO = teamController.create(new TeamDTO(TEAM_NAME, parentTeamDTO.getId(), organizationDTO), null);
    }


    @BeforeClass
    public void createApplication() {
        applicationDTO = applicationController.create(new ApplicationDTO(APPLICATION_NAME, ""), null);
    }


    @BeforeClass
    public void createRoles() {
        for (final String roleName : APPLICATION_ROLES) {
            roleDTOs.add(roleController.create(new RoleDTO(roleName, null), null));
        }
    }


    @BeforeClass(dependsOnMethods = {"createApplication", "createRoles"})
    public void createApplicationRoles() {
        roleDTOs.forEach(roleDTO -> applicationRoles.add(applicationRoleController.create(new ApplicationRoleDTO(applicationDTO, roleDTO), null)));
    }


    @BeforeClass
    public void createBackendService() {
        backendServiceDTO = backendServiceController.create(new BackendServiceDTO(backendService), null);
    }


    @BeforeClass(dependsOnMethods = "createBackendService")
    public void createBackendServiceRoles() {
        for (final String roleName : BACKEND_ROLES) {
            backendRoles.add(backendServiceRoleController.create(new BackendServiceRoleDTO(backendServiceDTO, roleName), null));
        }
    }


    @BeforeClass(dependsOnMethods = {"createApplicationRoles", "createBackendServiceRoles"})
    public void assignApplicationBackendServiceRoles() {
        Assert.assertTrue(userGroupApplicationBackendServiceRoleProvider.findAll().isEmpty());
        //Assign the backend to an application.
        for (ApplicationRoleDTO applicationRole : applicationRoles) {
            for (BackendServiceRoleDTO backendRole : backendRoles) {
                applicationBackendServiceRoleController.create(new ApplicationBackendServiceRoleDTO(applicationRole, backendRole), null);
            }
        }
        Assert.assertTrue(userGroupApplicationBackendServiceRoleProvider.findAll().isEmpty());
    }

    @Test
    public void getByParent() {
        Assert.assertEquals(teamController.getTeamsWithParent(parentTeamDTO).get(0).getName(), TEAM_NAME);
    }


    @Test
    public void assignUser() {
        teamController.assign(teamDTO.getId(), Collections.singleton(user1), null);
        Assert.assertNull(userController.getByUsername(user1.getUsername()).getGrantedAuthorities());
    }


    @Test(dependsOnMethods = {"assignUser"})
    public void getByTeamMembers() {
        Assert.assertEquals(userController.getByTeam(teamDTO.getId()).size(), 1, 0);
    }


    @Test(dependsOnMethods = {"getByTeamMembers"})
    public void unassignUser() {
        teamController.unAssign(teamDTO.getId(), Collections.singleton(user1), null);
    }


    @Test(dependsOnMethods = {"unassignUser"})
    public void getByTeamMembersEmpty() {
        Assert.assertEquals(userController.getByTeam(teamDTO.getId()).size(), 0);
    }

    @AfterClass
    public void deleteUsers() {
        userController.delete(user1, null);
    }

    @AfterClass(dependsOnMethods = "deleteUsers", alwaysRun = true)
    public void cleanUp() {
        userGroupApplicationBackendServiceRoleProvider.deleteAll();
        applicationBackendServiceRoleController.deleteAll(null);
        backendServiceRoleController.deleteAll(null);
        applicationRoleController.deleteAll(null);
        applicationController.deleteAll(null);
        teamController.deleteAll(null);
        userController.deleteAll(null);
        organizationController.deleteAll(null);
    }
}

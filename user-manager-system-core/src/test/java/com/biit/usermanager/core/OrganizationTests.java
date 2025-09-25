package com.biit.usermanager.core;

import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.ApplicationRoleController;
import com.biit.usermanager.core.controller.BackendServiceRoleController;
import com.biit.usermanager.core.controller.OrganizationController;
import com.biit.usermanager.core.controller.TeamController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.providers.UserGroupApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.TeamDTO;
import com.biit.usermanager.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

@Test(groups = "organizationTests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class OrganizationTests extends AbstractTestNGSpringContextTests {

    private static final String USER_1_NAME = "JosephColton";
    private static final String USER_1_UNIQUE_ID = "00000000AA";
    private static final String USER_1_FIRST_NAME = "Joseph";
    private static final String USER_1_LAST_NAME = "Colton";
    private static final String USER_1_PASSWORD = "asd123";

    private static final String USER_2_NAME = "KennethHandler";
    private static final String USER_2_UNIQUE_ID = "00000000BB";
    private static final String USER_2_FIRST_NAME = "Kenneth Sean Carson";
    private static final String USER_2_LAST_NAME = "Handler";
    private static final String USER_2_PASSWORD = "asd123";
    private static final String ORGANIZATION_1_NAME = "HASBRO";
    private static final String ORGANIZATION_2_NAME = "MATTEL";
    private static final String ORGANIZATION_3_NAME = "BANDAI";
    private static final String TEAM_1_NAME = "GIJOE";
    private static final String TEAM_2_NAME = "BARBIE";
    private static final String TEAM_3_NAME = "GOOJITZU";


    @Autowired
    private UserController userController;

    @Autowired
    private TeamController teamController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private ApplicationRoleController applicationRoleController;

    @Autowired
    private OrganizationController organizationController;

    @Autowired
    private BackendServiceRoleController backendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleController applicationBackendServiceRoleController;

    @Autowired
    private UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider;

    private TeamDTO teamDTO1;
    private TeamDTO teamDTO2;
    private TeamDTO teamDTO3;

    private UserDTO user1;
    private UserDTO user2;

    private OrganizationDTO organizationDTO1;
    private OrganizationDTO organizationDTO2;
    private OrganizationDTO organizationDTO3;


    @BeforeClass
    public void createUsers() {
        user1 = userController.createUser(USER_1_NAME, USER_1_UNIQUE_ID, USER_1_FIRST_NAME, USER_1_LAST_NAME, USER_1_PASSWORD, null, null);
        user2 = userController.createUser(USER_2_NAME, USER_2_UNIQUE_ID, USER_2_FIRST_NAME, USER_2_LAST_NAME, USER_2_PASSWORD, null, null);
    }


    @BeforeClass
    public void createOrganization1() {
        organizationDTO1 = organizationController.create(new OrganizationDTO(ORGANIZATION_1_NAME), null);
    }

    @BeforeClass
    public void createOrganization2() {
        organizationDTO2 = organizationController.create(new OrganizationDTO(ORGANIZATION_2_NAME), null);
    }

    @BeforeClass
    public void createOrganization3() {
        organizationDTO3 = organizationController.create(new OrganizationDTO(ORGANIZATION_3_NAME), null);
    }


    @BeforeClass(dependsOnMethods = "createOrganization1")
    public void createTeamOrganization1() {
        teamDTO1 = teamController.create(new TeamDTO(TEAM_1_NAME, organizationDTO1), null);
        Assert.assertEquals(teamController.getByOrganization(ORGANIZATION_1_NAME).size(), 1);
    }

    @BeforeClass(dependsOnMethods = "createOrganization2")
    public void createTeamOrganization2() {
        teamDTO2 = teamController.create(new TeamDTO(TEAM_2_NAME, organizationDTO2), null);
        Assert.assertEquals(teamController.getByOrganization(ORGANIZATION_2_NAME).size(), 1);
    }

    @BeforeClass(dependsOnMethods = "createOrganization3")
    public void createTeamOrganization3() {
        teamDTO3 = teamController.create(new TeamDTO(TEAM_3_NAME, organizationDTO3), null);
        Assert.assertEquals(teamController.getByOrganization(ORGANIZATION_3_NAME).size(), 1);
    }

    @Test
    public void assignUsers() {
        teamController.assign(teamDTO1.getId(), Collections.singleton(user1), null);
        teamController.assign(teamDTO2.getId(), Collections.singleton(user2), null);
        teamController.assign(teamDTO3.getId(), List.of(user2), null);
    }

    @Test
    public void getOrganizationByNameIgnoreCase() {
        Assert.assertNotNull(organizationController.findByName(ORGANIZATION_1_NAME.toLowerCase()));
        Assert.assertNotNull(organizationController.findByName(ORGANIZATION_2_NAME.toLowerCase()));
        Assert.assertNotNull(organizationController.findByName(ORGANIZATION_3_NAME.toLowerCase()));
    }

    @Test
    public void getTeamByNameIgnoreCase() {
        Assert.assertNotNull(teamController.getByName(TEAM_1_NAME.toLowerCase(), ORGANIZATION_1_NAME.toLowerCase()));
        Assert.assertNotNull(teamController.getByName(TEAM_2_NAME.toLowerCase(), ORGANIZATION_2_NAME.toLowerCase()));
        Assert.assertNotNull(teamController.getByName(TEAM_3_NAME.toLowerCase(), ORGANIZATION_3_NAME.toLowerCase()));
    }

    @Test(dependsOnMethods = {"assignUsers"})
    public void getOrganizationsByUsers() {
        Assert.assertEquals(organizationController.findByUserId(user1.getId()).size(), 1);
        Assert.assertEquals(organizationController.findByUserId(user2.getId()).size(), 2);
    }

    @Test(dependsOnMethods = {"assignUsers"})
    public void getUsersByOrganization() {
        Assert.assertEquals(userController.getByOrganization(organizationDTO1.getName()).size(), 1);
        Assert.assertEquals(userController.getByOrganization(organizationDTO2.getName()).size(), 1);
        Assert.assertEquals(userController.getByOrganization(organizationDTO3.getName()).size(), 1);
    }

    @AfterClass
    public void deleteUsers() {
        userController.delete(user1, null);
        userController.delete(user2, null);
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

package com.biit.usermanager.core;

import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.ApplicationRoleController;
import com.biit.usermanager.core.controller.BackendServiceController;
import com.biit.usermanager.core.controller.BackendServiceRoleController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.controller.UserGroupController;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Test(groups = "userGroupTests")
@SpringBootTest
public class UserGroupTests extends AbstractTestNGSpringContextTests {

    private static final String USER_1_NAME = "user1";
    private static final String USER_1_UNIQUE_ID = "00000000AA";
    private static final String USER_1_FIRST_NAME = "user1";
    private static final String USER_1_LAST_NAME = "user1";
    private static final String USER_1_PASSWORD = "asd123";

    private static final String USER_2_NAME = "user2";
    private static final String USER_2_UNIQUE_ID = "00000000BB";
    private static final String USER_2_FIRST_NAME = "user2";
    private static final String USER_2_LAST_NAME = "user2";
    private static final String USER_2_PASSWORD = "asd123";

    private static final String USER_GROUP_NAME = "Justice League";
    private static final String USER_GROUP_DESCRIPTION = "Justice League is a 2017 American superhero film based on the DC Comics superhero team of the same name.";

    private static final String[] APPLICATION_ROLES = new String[]{"WRITER", "READER"};
    private static final String[] BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};
    private static final String APPLICATION_NAME = "DASHBOARD";


    @Autowired
    private UserController userController;

    @Autowired
    private UserGroupController userGroupController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private RoleController roleController;

    @Autowired
    private ApplicationRoleController applicationRoleController;

    @Autowired
    private BackendServiceController backendServiceController;

    @Autowired
    private BackendServiceRoleController backendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleController applicationBackendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleConverter applicationBackendServiceRoleConverter;

    @Value("${spring.application.name}")
    private String backendService;

    private UserGroupDTO userGroup;

    private UserDTO user1;
    private UserDTO user2;

    private ApplicationDTO applicationDTO;

    private final List<RoleDTO> roleDTOs = new ArrayList<>();

    private final List<ApplicationRoleDTO> applicationRoles = new ArrayList<>();

    private BackendServiceDTO backendServiceDTO;

    private List<BackendServiceRoleDTO> backendRoles = new ArrayList<>();

    final List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOs = new ArrayList<>();

    @BeforeClass
    public void createUsers() {
        //Create the admin user
        user1 = (UserDTO) userController.createUser(USER_1_NAME, USER_1_UNIQUE_ID, USER_1_FIRST_NAME, USER_1_LAST_NAME, USER_1_PASSWORD, null);
        user2 = (UserDTO) userController.createUser(USER_2_NAME, USER_2_UNIQUE_ID, USER_2_FIRST_NAME, USER_2_LAST_NAME, USER_2_PASSWORD, null);
    }

    @BeforeClass
    public void createUserGroups() {
        userGroup = userGroupController.create(new UserGroupDTO(), null);
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
        //Assign the backend to an application.
        for (ApplicationRoleDTO applicationRole : applicationRoles) {
            for (BackendServiceRoleDTO backendRole : backendRoles) {
                applicationBackendServiceRoleDTOs.add(applicationBackendServiceRoleController.create(new ApplicationBackendServiceRoleDTO(applicationRole, backendRole), null));
            }
        }
    }

    @Test
    public void assignUser() {
        userGroupController.assign(userGroup.getId(), Collections.singleton(user1), null);
    }


    @Test
    public void assignRoles() {
        userGroupController.assign(userGroup, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOs));
    }

    @Test(dependsOnMethods = {"assignUser", "assignRoles"})
    public void getUserRoles() {
        Assert.fail();
    }
}

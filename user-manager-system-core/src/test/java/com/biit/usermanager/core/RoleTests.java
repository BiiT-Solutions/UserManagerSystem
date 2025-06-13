package com.biit.usermanager.core;

import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.controller.ApplicationController;
import com.biit.usermanager.core.controller.ApplicationRoleController;
import com.biit.usermanager.core.controller.BackendServiceController;
import com.biit.usermanager.core.controller.BackendServiceRoleController;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.core.exceptions.RoleAlreadyExistsException;
import com.biit.usermanager.core.providers.BackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
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

@Test(groups = "roleTests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class RoleTests extends AbstractTestNGSpringContextTests {

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
    private static final String OTHER_APPLICATION_NAME = "Cortana";

    private static final String NEW_BACKEND_NAME = "Armour";
    private static final String OTHER_BACKEND_NAME = "Core";

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
    private BackendServiceRoleProvider backendServiceRoleProvider;

    @Autowired
    private BackendServiceController backendServiceController;

    @Autowired
    private ApplicationBackendServiceRoleController applicationBackendServiceRoleController;

    @Autowired
    private ApplicationBackendServiceRoleConverter applicationBackendServiceRoleConverter;

    @Autowired
    private UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider;

    @Value("${spring.application.name}")
    private String backendService;

    private UserDTO admin;

    private ApplicationDTO applicationDTO;

    private final List<RoleDTO> roleDTOs = new ArrayList<>();

    private final List<ApplicationRoleDTO> applicationRoles = new ArrayList<>();

    private BackendServiceDTO backendServiceDTO;

    private final List<BackendServiceRoleDTO> backendRoles = new ArrayList<>();

    final List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOs = new ArrayList<>();

    @BeforeClass
    public void createUser() {
        //Create the admin user
        admin = (UserDTO) userController.createUser(USER_NAME, USER_UNIQUE_ID, USER_FIRST_NAME, USER_LAST_NAME, USER_PASSWORD, null, null);
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

    @Test(expectedExceptions = RoleAlreadyExistsException.class)
    public void addDuplicatedRole() {
        roleController.create(new RoleDTO(APPLICATION_ROLES[0], null), null);
    }


    @Test(expectedExceptions = RoleAlreadyExistsException.class)
    public void addDuplicatedBackendServiceRole() {
        backendServiceRoleController.create(new BackendServiceRoleDTO(backendServiceDTO, BACKEND_ROLES[0]), null);
    }

    @Test
    public void assignApplicationBackendServiceRoles() {
        //Assign the backend to an application.
        for (ApplicationRoleDTO applicationRole : applicationRoles) {
            for (BackendServiceRoleDTO backendRole : backendRoles) {
                applicationBackendServiceRoleDTOs.add(applicationBackendServiceRoleController.create(new ApplicationBackendServiceRoleDTO(applicationRole, backendRole), null));
            }
        }
    }

    @Test(dependsOnMethods = "assignApplicationBackendServiceRoles")
    public void assignApplicationBackendServiceRolesToUsers() {
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();
        userController.assign(admin, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOs));
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles + 4);
    }

    @Test
    public void generateNewApplicationWithRoles() {
        long existingApplicationBackendServiceRoles = applicationBackendServiceRoleController.count();
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();
        final ApplicationDTO applicationDTO = applicationController.create(new ApplicationDTO(NEW_APPLICATION_NAME, ""), null);
        final List<ApplicationRoleDTO> applicationRoles = new ArrayList<>();
        roleDTOs.forEach(roleDTO -> applicationRoles.add(applicationRoleController.create(new ApplicationRoleDTO(applicationDTO, roleDTO), null)));
        //Assign the backend to an application.
        final List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOs = new ArrayList<>();
        for (ApplicationRoleDTO applicationRole : applicationRoles) {
            for (BackendServiceRoleDTO backendRole : backendRoles) {
                applicationBackendServiceRoleDTOs.add(applicationBackendServiceRoleController.create(new ApplicationBackendServiceRoleDTO(applicationRole, backendRole), null));
            }
        }
        Assert.assertEquals(applicationBackendServiceRoleController.count(), existingApplicationBackendServiceRoles + 4);
        userController.assign(admin, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOs));
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles + 4);
    }

    @Test(dependsOnMethods = "generateNewApplicationWithRoles")
    public void updateApplicationRolesBackendServicesAndEnsureOldUsersGetThem() {
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();

        //New BackendService role.
        final BackendServiceRoleDTO newBackendServiceRoleDTO = backendServiceRoleController.create(new BackendServiceRoleDTO(backendServiceDTO, "Wizard"), null);

        //Assign the backend to an application.
        final ApplicationRoleDTO applicationRole = applicationRoleController.getByApplicationAndRole(APPLICATION_NAME, APPLICATION_ROLES[0]);
        final ApplicationBackendServiceRoleDTO applicationBackendServiceRole = applicationBackendServiceRoleController.create(new ApplicationBackendServiceRoleDTO(applicationRole, newBackendServiceRoleDTO), null);

        userController.assign(admin, Collections.singletonList(applicationBackendServiceRoleConverter.reverse(applicationBackendServiceRole)));

        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles + 1);

        //Delete the new role.
        applicationBackendServiceRoleController.delete(applicationBackendServiceRole, null);
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles);
    }

    @Test(dependsOnMethods = "generateNewApplicationWithRoles")
    public void searchBackendServiceRolesByApplicationAndApplicationRole() {
        List<BackendServiceRole> backendServiceRoles = backendServiceRoleProvider.findByApplicationAndRole(NEW_APPLICATION_NAME, APPLICATION_ROLES[0]);
        Assert.assertEquals(backendServiceRoles.size(), 2);
    }

    @Test(dependsOnMethods = {"generateNewApplicationWithRoles", "searchBackendServiceRolesByApplicationAndApplicationRole"})
    public void deleteNewApplication() {
        long existingApplicationBackendServiceRoles = applicationBackendServiceRoleController.count();
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();
        applicationController.deleteById(NEW_APPLICATION_NAME, null);
        //Must delete the Application Roles, the ApplicationBackendServiceRoles and the user assigned roles.
        Assert.assertEquals(applicationBackendServiceRoleController.count(), existingApplicationBackendServiceRoles - 4);
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles - 4);
    }

    @Test
    public void generateOtherApplicationWithRoles() {
        long existingApplicationBackendServiceRoles = applicationBackendServiceRoleController.count();
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();
        final ApplicationDTO applicationDTO = applicationController.create(new ApplicationDTO(OTHER_APPLICATION_NAME, ""), null);
        final List<ApplicationRoleDTO> applicationRoles = new ArrayList<>();
        roleDTOs.forEach(roleDTO -> applicationRoles.add(applicationRoleController.create(new ApplicationRoleDTO(applicationDTO, roleDTO), null)));
        //Assign the backend to an application.
        final List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOs = new ArrayList<>();
        for (ApplicationRoleDTO applicationRole : applicationRoles) {
            for (BackendServiceRoleDTO backendRole : backendRoles) {
                applicationBackendServiceRoleDTOs.add(applicationBackendServiceRoleController.create(new ApplicationBackendServiceRoleDTO(applicationRole, backendRole), null));
            }
        }
        Assert.assertEquals(applicationBackendServiceRoleController.count(), existingApplicationBackendServiceRoles + 4);
        userController.assign(admin, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOs));
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles + 4);
    }

    @Test(dependsOnMethods = "generateOtherApplicationWithRoles")
    public void deleteOtherApplicationRoles() {
        long existingApplicationBackendServiceRoles = applicationBackendServiceRoleController.count();
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();
        for (String roleName : APPLICATION_ROLES) {
            applicationRoleController.deleteByApplicationAndRole(OTHER_APPLICATION_NAME, roleName, null);
        }
        //Must delete the Application Roles, the ApplicationBackendServiceRoles and the user assigned roles.
        Assert.assertEquals(applicationBackendServiceRoleController.count(), existingApplicationBackendServiceRoles - 4);
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles - 4);
    }

    @Test
    public void generateNewBackendServiceWithRoles() {
        long existingApplicationBackendServiceRoles = applicationBackendServiceRoleController.count();
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();
        final BackendServiceDTO backendServiceDTO = backendServiceController.create(new BackendServiceDTO(NEW_BACKEND_NAME), null);
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
        Assert.assertEquals(applicationBackendServiceRoleController.count(), existingApplicationBackendServiceRoles + 4);
        userController.assign(admin, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOs));
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles + 4);
    }

    @Test(dependsOnMethods = "generateNewApplicationWithRoles")
    public void deleteNewBackendService() {
        long existingApplicationBackendServiceRoles = applicationBackendServiceRoleController.count();
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();
        backendServiceController.deleteById(NEW_BACKEND_NAME, null);
        //Must delete the Application Roles, the ApplicationBackendServiceRoles and the user assigned roles.
        Assert.assertEquals(applicationBackendServiceRoleController.count(), existingApplicationBackendServiceRoles - 4);
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles - 4);
    }

    @Test
    public void generateOtherNewBackendServiceWithRoles() {
        long existingApplicationBackendServiceRoles = applicationBackendServiceRoleController.count();
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();
        final BackendServiceDTO backendServiceDTO = backendServiceController.create(new BackendServiceDTO(OTHER_BACKEND_NAME), null);
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
        Assert.assertEquals(applicationBackendServiceRoleController.count(), existingApplicationBackendServiceRoles + 4);
        userController.assign(admin, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOs));
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles + 4);
    }

    @Test(dependsOnMethods = "generateOtherNewBackendServiceWithRoles")
    public void deleteOtherBackendServiceRole() {
        long existingApplicationBackendServiceRoles = applicationBackendServiceRoleController.count();
        long existingUserRoles = userApplicationBackendServiceRoleProvider.count();
        for (final String roleName : BACKEND_ROLES) {
            backendServiceRoleController.delete(OTHER_BACKEND_NAME, roleName, null);
        }
        //Must delete the Application Roles, the ApplicationBackendServiceRoles and the user assigned roles.
        Assert.assertEquals(applicationBackendServiceRoleController.count(), existingApplicationBackendServiceRoles - 4);
        Assert.assertEquals(userApplicationBackendServiceRoleProvider.count(), existingUserRoles - 4);
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        userApplicationBackendServiceRoleProvider.deleteAll();
        applicationBackendServiceRoleController.deleteAll(null);
        backendServiceRoleController.deleteAll(null);
        applicationRoleController.deleteAll(null);
        applicationController.deleteAll(null);
        userController.deleteAll(null);
    }
}

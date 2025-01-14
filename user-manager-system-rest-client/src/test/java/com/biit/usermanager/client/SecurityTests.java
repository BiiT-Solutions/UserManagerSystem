package com.biit.usermanager.client;


import com.biit.server.security.exceptions.ActionNotAllowedException;
import com.biit.usermanager.client.security.UserClientSecurityController;
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
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.dto.BackendServiceDTO;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.rest.UserManagerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = UserManagerServer.class)
@Test(groups = {"securityTests"})
public class SecurityTests extends AbstractTestNGSpringContextTests {

    private static final String USER1_NAME = "admin";
    private static final String USER1_UNIQUE_ID = "00000000AA";
    private static final String USER1_FIRST_NAME = "Test";
    private static final String USER1_LAST_NAME = "User";
    private static final String USER1_PASSWORD = "asd123";
    private static final String[] USER1_BACKEND_ROLES = new String[]{"ADMIN", "VIEWER"};

    private static final String USER2_NAME = "guest";
    private static final String USER2_UNIQUE_ID = "00000000BB";
    private static final String USER2_FIRST_NAME = "Guest";
    private static final String USER2_LAST_NAME = "User";
    private static final String USER2_PASSWORD = "asd123";
    private static final String[] USER2_BACKEND_ROLES = new String[]{"VIEWER"};

    private static final String APPLICATION_NAME = "DASHBOARD";
    private static final String[] APPLICATION_ROLES = new String[]{"WRITER", "READER"};
    private static final String ORGANIZATION_NAME = "NHM";

    @Autowired
    private UserController userController;

    @Autowired
    private RoleController roleController;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private OrganizationController organizationController;

    @Autowired
    private TeamController teamController;

    @Autowired
    private ApplicationRoleController applicationRoleController;

    @Autowired
    private BackendServiceRoleController backendServiceRoleController;
    @Autowired
    private BackendServiceController backendServiceController;

    @Autowired
    private ApplicationBackendServiceRoleController applicationBackendServiceRoleController;

    @Autowired
    private UserApplicationBackendServiceRoleProvider applicationBackendServiceRoleProvider;

    @Autowired
    private ApplicationBackendServiceRoleConverter applicationBackendServiceRoleConverter;

    @Autowired
    private UserClientSecurityController userClientSecurityController;

    @Value("${spring.application.name}")
    private String backendService;

    private UserDTO admin;
    private UserDTO guest;


    @BeforeClass
    public void setDefaultData() {
        //Create the admin user
        admin = (UserDTO) userController.createUser(USER1_NAME, USER1_UNIQUE_ID, USER1_FIRST_NAME, USER1_LAST_NAME, USER1_PASSWORD, null, null);
        guest = (UserDTO) userController.createUser(USER2_NAME, USER2_UNIQUE_ID, USER2_FIRST_NAME, USER2_LAST_NAME, USER2_PASSWORD, null, null);

        //Create the application
        final ApplicationDTO applicationDTO = applicationController.create(new ApplicationDTO(APPLICATION_NAME, ""), null);

        //Create the organization
        final OrganizationDTO organizationDTO = organizationController.create(new OrganizationDTO(ORGANIZATION_NAME), null);

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
        final List<BackendServiceRoleDTO> backendRolesUser2 = new ArrayList<>();
        for (final String roleName : USER1_BACKEND_ROLES) {
            final BackendServiceRoleDTO backendServiceRoleDTO = backendServiceRoleController.create(new BackendServiceRoleDTO(backendServiceDTO, roleName), null);
            backendRoles.add(backendServiceRoleDTO);
            if (Arrays.asList(USER2_BACKEND_ROLES).contains(roleName)) {
                backendRolesUser2.add(backendServiceRoleDTO);
            }
        }

        //Assign the backend to an application.
        final List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOs = new ArrayList<>();
        final List<ApplicationBackendServiceRoleDTO> applicationBackendServiceRoleDTOsUser2 = new ArrayList<>();
        for (ApplicationRoleDTO applicationRole : applicationRoles) {
            for (BackendServiceRoleDTO backendRole : backendRoles) {
                final ApplicationBackendServiceRoleDTO applicationBackendServiceRoleDTO = new ApplicationBackendServiceRoleDTO(applicationRole, backendRole);
                if (backendRolesUser2.contains(backendRole)) {
                    applicationBackendServiceRoleDTOsUser2.add(applicationBackendServiceRoleDTO);
                }
                applicationBackendServiceRoleDTOs.add(applicationBackendServiceRoleController.create(applicationBackendServiceRoleDTO, null));
            }
        }

        userController.setApplicationBackendServiceRole(admin, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOs));
        userController.setApplicationBackendServiceRole(guest, applicationBackendServiceRoleConverter.reverseAll(applicationBackendServiceRoleDTOsUser2));
    }

    @Test
    public void checkAdminCanSeeOtherUserData() {
        userClientSecurityController.checkIfCanSeeUserData(APPLICATION_NAME, admin.getUsername(), guest.getUUID(), "USERMANAGERSYSTEM_ADMIN");
    }

    @Test
    public void checkAdminCanSeeOtherUserDataWithoutApplication() {
        userClientSecurityController.checkIfCanSeeUserData(APPLICATION_NAME, admin.getUsername(), guest.getUUID(), "ADMIN");
    }

    @Test(expectedExceptions = ActionNotAllowedException.class)
    public void checkGuestCannotSeeOtherUserData() {
        userClientSecurityController.checkIfCanSeeUserData(APPLICATION_NAME, guest.getUsername(), admin.getUUID(), "USERMANAGERSYSTEM_ADMIN");
    }

    @Test
    public void checkGuestCanSeeOwnUserData() {
        userClientSecurityController.checkIfCanSeeUserData(APPLICATION_NAME, guest.getUsername(), guest.getUUID(), "USERMANAGERSYSTEM_ADMIN");
    }

    @Test
    public void checkGuestCanSeeOwnUserDataWithoutApplication() {
        userClientSecurityController.checkIfCanSeeUserData(APPLICATION_NAME, guest.getUsername(), guest.getUUID(), "ADMIN");
    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        applicationBackendServiceRoleProvider.deleteAll();
        applicationBackendServiceRoleController.deleteAll(null);
        applicationRoleController.deleteAll(null);
        roleController.deleteAll(null);
        applicationController.deleteAll(null);
        backendServiceRoleController.deleteAll(null);
        backendServiceController.deleteAll(null);
        teamController.deleteAll(null);
        userController.deleteAll(null);
    }
}

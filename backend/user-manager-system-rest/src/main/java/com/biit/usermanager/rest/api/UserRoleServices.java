package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.usermanager.core.controller.UserRoleController;
import com.biit.usermanager.core.converters.UserRoleConverter;
import com.biit.usermanager.core.converters.models.UserRoleConverterRequest;
import com.biit.usermanager.core.providers.UserRoleProvider;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles/users")
public class UserRoleServices extends BasicServices<UserRole, UserRoleDTO, UserRoleRepository,
        UserRoleProvider, UserRoleConverterRequest, UserRoleConverter, UserRoleController> {

    public UserRoleServices(UserRoleController userRoleController) {
        super(userRoleController);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get roles by username", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/usernames/{username}/groups/{groupName}/applications/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRoleDTO> getRolesFromUserGroupAndApplication(@Parameter(description = "Username of an existing user", required = true)
                                                                 @PathVariable("username") String username,
                                                                 @Parameter(description = "Group name")
                                                                 @PathVariable("groupName") String groupName,
                                                                 @Parameter(description = "Application name")
                                                                 @PathVariable("applicationName") String applicationName,
                                                                 HttpServletRequest request) {
        return getController().getByUserAndGroupAndApplication(username, groupName, applicationName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get roles by username", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/usernames/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRoleDTO> getRolesFromUser(@Parameter(description = "Username of an existing user", required = true)
                                              @PathVariable("username") String username,
                                              HttpServletRequest request) {
        return getController().getByUser(username);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get roles by group name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/groups/{groupName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRoleDTO> getRolesFromGroup(@Parameter(description = "Name of an existing group", required = true)
                                               @PathVariable("groupName") String groupName,
                                               HttpServletRequest request) {
        return getController().getByGroup(groupName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get roles by username", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/groups/{groupName}/roles/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRoleDTO> getUsersWithRoleOnGroup(@Parameter(description = "Name of an existing group", required = true)
                                                     @PathVariable("groupName") String groupName,
                                                     @Parameter(description = "Role name", required = true)
                                                     @PathVariable("roleName") String roleName,
                                                     HttpServletRequest request) {
        return getController().getByUserAndRole(groupName, roleName);
    }
}

package com.biit.usermanager.rest.api;

import com.biit.server.rest.ElementServices;
import com.biit.usermanager.core.controller.UserGroupController;
import com.biit.usermanager.core.converters.UserGroupConverter;
import com.biit.usermanager.core.converters.models.UserGroupConverterRequest;
import com.biit.usermanager.core.providers.UserGroupProvider;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.dto.UserGroupDTO;
import com.biit.usermanager.persistence.entities.UserGroup;
import com.biit.usermanager.persistence.repositories.UserGroupRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-groups")
public class UserGroupServices extends ElementServices<UserGroup, Long, UserGroupDTO, UserGroupRepository,
        UserGroupProvider, UserGroupConverterRequest, UserGroupConverter, UserGroupController> {

    public UserGroupServices(UserGroupController userGroupController) {
        super(userGroupController);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get UserGroup by name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO getByGroupName(@Parameter(description = "Name of an existing group", required = true) @PathVariable("name") String name,
                                       HttpServletRequest request) {
        return getController().getByName(name);
    }


    @Operation(summary = "Checks if a group name is already taken or not.")
    @GetMapping(path = "/public/{name}/check")
    @ResponseStatus(value = HttpStatus.OK)
    public void checkGroupNameExists(@Parameter(description = "name", required = true)
                                     @PathVariable("name") String name,
                                     HttpServletRequest httpRequest) {
        getController().checkNameExists(name);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes a UserGroup by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(path = "/name/{name}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteGroup(@Parameter(description = "name", required = true)
                            @PathVariable("name") String name, Authentication authentication, HttpServletRequest httpRequest) {
        getController().delete(name, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes a UserGroup by id.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteGroup(@Parameter(description = "id", required = true)
                            @PathVariable("id") Long id, Authentication authentication, HttpServletRequest httpRequest) {
        getController().delete(id, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Assign roles to a group. Generates the intermediate structure if needed.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/name/{name}/applications/{applicationName}/application-roles/{applicationRoleName}"
            + "/backend-services/{backendServiceName}/backend-service-roles/{backendServiceRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO getRolesFromApplicationAndBackendServices(
            @Parameter(description = "Name of an existing UserGroup", required = true)
            @PathVariable("name") String name,
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            @Parameter(description = "Backend Service name", required = true)
            @PathVariable("backendServiceName") String backendServiceName,
            @Parameter(description = "Backend Role name", required = true)
            @PathVariable("backendServiceRoleName") String backendServiceRoleName,
            HttpServletRequest request) {
        return getController().assign(name, applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Assign roles to a group. Generates the intermediate structure if needed.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/id/{id}/applications/{applicationName}/application-roles/{applicationRoleName}"
            + "/backend-services/{backendServiceName}/backend-service-roles/{backendServiceRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO getRolesFromApplicationAndBackendServices(
            @Parameter(description = "Id of an existing UserGroup", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            @Parameter(description = "Backend Service name", required = true)
            @PathVariable("backendServiceName") String backendServiceName,
            @Parameter(description = "Backend Role name", required = true)
            @PathVariable("backendServiceRoleName") String backendServiceRoleName,
            HttpServletRequest request) {
        return getController().assign(id, applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Assign application roles to a UserGroup. Assigns all related backend services that are defined.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/name/{name}/applications/{applicationName}/application-roles/{applicationRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO setRolesFromApplicationAndRoles(
            @Parameter(description = "Name of an existing User Group", required = true)
            @PathVariable("name") String name,
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            HttpServletRequest request) {
        return getController().assign(name, applicationName, applicationRoleName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Assign application roles to a UserGroup. Assigns all related backend services that are defined.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/id/{id}/applications/{applicationName}/application-roles/{applicationRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO setRolesFromApplicationAndRoles(
            @Parameter(description = "Id of an existing User Group", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            HttpServletRequest request) {
        return getController().assign(id, applicationName, applicationRoleName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes application roles from a User Group.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/name/{name}/applications/{applicationName}/application-roles/{applicationRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO deleteRolesFromApplicationAndRoles(
            @Parameter(description = "Name of an existing User Group", required = true)
            @PathVariable("name") String name,
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().unAssign(name, applicationName, applicationRoleName, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes application roles from a User Group.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/id/{id}/applications/{applicationName}/application-roles/{applicationRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO deleteRolesFromApplicationAndRoles(
            @Parameter(description = "Id of an existing User Group", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().unAssign(id, applicationName, applicationRoleName, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Adds Users to the UserGroup.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/{id}/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO addUsers(
            @Parameter(description = "Id of an existing User Group", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> users,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assign(id, users, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Adds Users to the UserGroup by group name.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/name/{name}/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO addUsers(
            @Parameter(description = "Name of an existing UserGroup", required = true)
            @PathVariable("name") String name,
            @RequestBody Collection<UserDTO> users,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assign(name, users, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Adds Users to the UserGroup.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/{id}/users/uuids",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO addUsersByUuids(
            @Parameter(description = "Id of an existing User Group", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UUID> usersUUIDs,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assignByUUID(id, usersUUIDs, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Adds Users to the UserGroup.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/name/{name}/users/uuids",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO addUsersByUuids(
            @Parameter(description = "Name of an existing UserGroup", required = true)
            @PathVariable("name") String name,
            @RequestBody Collection<UUID> usersUUIDs,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assignByUUID(name, usersUUIDs, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Adds Users to the UserGroup.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/{id}/users/usernames",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO addUsersByUsernames(
            @Parameter(description = "Id of an existing User Group", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<String> usernames,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assignByUsernames(id, usernames, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Adds Users to the UserGroup.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/name/{name}/users/usernames",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO addUsersByUsernamesAndGroupName(
            @Parameter(description = "Name an existing User Group", required = true)
            @PathVariable("name") String name,
            @RequestBody Collection<String> usernames,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().assignByUsernames(name, usernames, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Removes Users from the UserGroup.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/{id}/users/remove",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserGroupDTO removeUsers(
            @Parameter(description = "Id of an existing User Group", required = true)
            @PathVariable("id") Long id,
            @RequestBody Collection<UserDTO> users,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().unAssign(id, users, authentication.getName());
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get all groups from a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserGroupDTO> getByUsername(@Parameter(description = "Name of an existing user", required = true) @PathVariable("username") String username,
                                            HttpServletRequest request) {
        return getController().getFromUser(username);
    }
}

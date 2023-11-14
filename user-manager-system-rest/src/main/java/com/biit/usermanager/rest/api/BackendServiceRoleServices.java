package com.biit.usermanager.rest.api;

import com.biit.server.rest.CreatedElementServices;
import com.biit.usermanager.core.controller.BackendServiceRoleController;
import com.biit.usermanager.core.converters.BackendServiceRoleConverter;
import com.biit.usermanager.core.converters.models.BackendServiceRoleConverterRequest;
import com.biit.usermanager.core.providers.BackendServiceRoleProvider;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.BackendServiceRoleRepository;
import io.swagger.v3.oas.annotations.Hidden;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend-service-roles")
public class BackendServiceRoleServices extends CreatedElementServices<
        BackendServiceRole,
        BackendServiceRoleId,
        BackendServiceRoleDTO,
        BackendServiceRoleRepository,
        BackendServiceRoleProvider,
        BackendServiceRoleConverterRequest,
        BackendServiceRoleConverter,
        BackendServiceRoleController
        > {

    public BackendServiceRoleServices(BackendServiceRoleController backendServiceRoleController) {
        super(backendServiceRoleController);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get backend's roles by service name and role name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/backend-services/{backendServiceName}/roles/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BackendServiceRoleDTO getRolesFromServiceWithName(
            @Parameter(description = "Name of an existing service", required = true)
            @PathVariable("backendServiceName") String backendServiceName,
            @Parameter(description = "Name of the role", required = true)
            @PathVariable("roleName") String roleName,
            HttpServletRequest request) {
        return getController().findByServiceAndRole(backendServiceName, roleName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get backend's roles by service name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/backend-services/{backendServiceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BackendServiceRoleDTO> getRolesFromServiceWithName(
            @Parameter(description = "Name of an existing service", required = true)
            @PathVariable("backendServiceName") String backendServiceName,
            HttpServletRequest request) {
        return getController().findByService(backendServiceName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get backend's roles by user name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/users/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BackendServiceRoleDTO> getRolesFromUser(
            @Parameter(description = "Name of an existing user", required = true)
            @PathVariable("userName") String userName,
            HttpServletRequest request) {
        return getController().findBy(userName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get backend's roles by user name, group and application", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/users/{userName}/groups/{groupName}/applications/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BackendServiceRoleDTO> getRolesFromUserGroupAndApplication(
            @Parameter(description = "Name of an existing user", required = true)
            @PathVariable("userName") String userName,
            @Parameter(description = "Name of an existing group", required = false)
            @PathVariable("groupName") String groupName,
            @Parameter(description = "Name of an existing application", required = false)
            @PathVariable("applicationName") String applicationName,
            HttpServletRequest request) {
        return getController().findBy(userName, groupName, applicationName);
    }

    @Hidden
    @Override
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets an entity.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BackendServiceRoleDTO get(@Parameter(description = "Id of an existing application", required = true) @PathVariable("id") BackendServiceRoleId id,
                                     Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Endpoint not allowed!");
    }

    @Hidden
    @Override
    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Deletes an entity.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Id of an existing application", required = true) @PathVariable("id") BackendServiceRoleId id,
                       HttpServletRequest request) {
        throw new UnsupportedOperationException("Endpoint not allowed!");
    }

    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Deletes an entity.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/backend-services/{backendServiceName}/roles/{roleName}")
    public void delete(@Parameter(description = "Name of an existing service", required = true)
                       @PathVariable("backendServiceName") String backendServiceName,
                       @Parameter(description = "Name of the role", required = true)
                       @PathVariable("roleName") String roleName,
                       Authentication authentication, HttpServletRequest request) {
        getController().delete(backendServiceName, roleName, authentication.getName());
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get backend's roles by application and application's role.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/applications/{applicationName}/role/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BackendServiceRoleDTO> getRolesFromApplicationAndRole(
            @Parameter(description = "Name of an existing application", required = false)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Name of the application's role", required = false)
            @PathVariable("roleName") String roleName,
            HttpServletRequest request) {
        return getController().findByApplicationAndRole(applicationName, roleName);
    }

}

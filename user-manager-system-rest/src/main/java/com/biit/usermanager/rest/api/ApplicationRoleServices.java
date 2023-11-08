package com.biit.usermanager.rest.api;

import com.biit.server.rest.CreatedElementServices;
import com.biit.usermanager.core.controller.ApplicationRoleController;
import com.biit.usermanager.core.converters.ApplicationRoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationRoleConverterRequest;
import com.biit.usermanager.core.providers.ApplicationRoleProvider;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.ApplicationRoleId;
import com.biit.usermanager.persistence.repositories.ApplicationRoleRepository;
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
@RequestMapping("/application-roles")
public class ApplicationRoleServices extends CreatedElementServices<
        ApplicationRole,
        ApplicationRoleId,
        ApplicationRoleDTO,
        ApplicationRoleRepository,
        ApplicationRoleProvider,
        ApplicationRoleConverterRequest,
        ApplicationRoleConverter,
        ApplicationRoleController
        > {

    public ApplicationRoleServices(ApplicationRoleController applicationRoleController) {
        super(applicationRoleController);
    }

    @Hidden
    @Override
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets an entity.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApplicationRoleDTO get(@Parameter(description = "Id of an existing application", required = true)
                                  @PathVariable("id") ApplicationRoleId id,
                                  Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Endpoint not allowed!");
    }

    @Hidden
    @Override
    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Deletes an entity.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Id of an existing application", required = true)
                       @PathVariable("id") ApplicationRoleId id,
                       HttpServletRequest request) {
        throw new UnsupportedOperationException("Endpoint not allowed!");
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get application's roles by application name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/applications/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ApplicationRoleDTO> getRolesFromApplication(
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            HttpServletRequest request) {
        return getController().getByApplication(applicationName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get application's roles by role name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/roles/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ApplicationRoleDTO> getRolesFromUser(@Parameter(description = "Role name", required = true)
                                                     @PathVariable("roleName") String roleName,
                                                     HttpServletRequest request) {
        return getController().getByRole(roleName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get application's roles by application name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/applications/{applicationName}/roles/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApplicationRoleDTO getRolesFromApplicationAndRoles(
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Role name", required = true)
            @PathVariable("roleName") String roleName,
            HttpServletRequest request) {
        return getController().getByApplicationAndRole(applicationName, roleName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Deletes an entity.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/applications/{applicationName}/roles/{roleName}")
    public void delete(@Parameter(description = "Application name", required = true)
                       @PathVariable("applicationName") String applicationName,
                       @Parameter(description = "Role name", required = true)
                       @PathVariable("roleName") String roleName,
                       Authentication authentication, HttpServletRequest request) {
        getController().deleteByApplicationAndRole(applicationName, roleName, authentication.getName());
    }
}

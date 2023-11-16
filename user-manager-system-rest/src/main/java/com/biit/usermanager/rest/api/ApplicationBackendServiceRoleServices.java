package com.biit.usermanager.rest.api;

import com.biit.server.rest.CreatedElementServices;
import com.biit.usermanager.core.controller.ApplicationBackendServiceRoleController;
import com.biit.usermanager.core.converters.ApplicationBackendServiceRoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationBackendServiceRoleConverterRequest;
import com.biit.usermanager.core.providers.ApplicationBackendServiceRoleProvider;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRoleId;
import com.biit.usermanager.persistence.repositories.ApplicationBackendServiceRoleRepository;
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

@RestController
@RequestMapping("/application-backend-service-roles")
public class ApplicationBackendServiceRoleServices extends CreatedElementServices<
        ApplicationBackendServiceRole,
        ApplicationBackendServiceRoleId,
        ApplicationBackendServiceRoleDTO,
        ApplicationBackendServiceRoleRepository,
        ApplicationBackendServiceRoleProvider,
        ApplicationBackendServiceRoleConverterRequest,
        ApplicationBackendServiceRoleConverter,
        ApplicationBackendServiceRoleController
        > {

    public ApplicationBackendServiceRoleServices(ApplicationBackendServiceRoleController applicationBackendServiceRoleController) {
        super(applicationBackendServiceRoleController);
    }

    @Hidden
    @Override
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets an entity.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApplicationBackendServiceRoleDTO get(@Parameter(description = "Id of an existing application", required = true)
                                                @PathVariable("id") ApplicationBackendServiceRoleId id,
                                                Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Endpoint not allowed!");
    }

    @Hidden
    @Override
    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Deletes an entity.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Id of an existing application", required = true) @PathVariable("id") ApplicationBackendServiceRoleId id,
                       Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Endpoint not allowed!");
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get application's roles.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/applications/{applicationName}/application-roles/{applicationRoleName}"
            + "/backend-services/{backendServiceName}/backend-service-roles/{backendServiceRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApplicationBackendServiceRoleDTO getRolesFromApplicationAndRoles(
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            @Parameter(description = "Backend Service name", required = true)
            @PathVariable("backendServiceName") String backendServiceName,
            @Parameter(description = "Backend Role name", required = true)
            @PathVariable("backendServiceRoleName") String backendServiceRoleName,
            HttpServletRequest request) {
        return getController().findByApplicationRoleAndServiceRole(applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Deletes an entity.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/applications/{applicationName}/application-roles/{applicationRoleName}"
            + "/backend-services/{backendServiceName}/backend-service-roles/{backendServiceRoleName}")
    public void delete(@Parameter(description = "Application name", required = true)
                       @PathVariable("applicationName") String applicationName,
                       @Parameter(description = "Application Role name", required = true)
                       @PathVariable("applicationRoleName") String applicationRoleName,
                       @Parameter(description = "Backend Service name", required = true)
                       @PathVariable("backendServiceName") String backendServiceName,
                       @Parameter(description = "Backend Role name", required = true)
                       @PathVariable("backendServiceRoleName") String backendServiceRoleName,
                       Authentication authentication, HttpServletRequest request) {
        getController().deleteByApplicationRoleAndServiceRole(applicationName,
                applicationRoleName, backendServiceName, backendServiceRoleName, authentication.getName());
    }


}

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
    @Operation(summary = "Get backend's roles by user name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/users/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BackendServiceRoleDTO> getRolesFromUser(
            @Parameter(description = "Name of an existing user", required = true)
            @PathVariable("userName") String userName,
            HttpServletRequest request) {
        return getController().findBy(userName);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get backend's roles by user name", security = @SecurityRequirement(name = "bearerAuth"))
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


}

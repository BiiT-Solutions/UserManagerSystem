package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.usermanager.core.controller.RoleController;
import com.biit.usermanager.core.converters.RoleConverter;
import com.biit.usermanager.core.converters.models.RoleConverterRequest;
import com.biit.usermanager.core.providers.RoleProvider;
import com.biit.usermanager.dto.RoleDTO;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.repositories.RoleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/roles")
public class RoleServices extends BasicServices<Role, RoleDTO, RoleRepository,
        RoleProvider, RoleConverterRequest, RoleConverter, RoleController> {

    public RoleServices(RoleController roleController) {
        super(roleController);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets a role by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleDTO getByName(@Parameter(description = "Name of an existing role", required = true) @PathVariable("name") String name,
                       HttpServletRequest request) {
        return controller.getByName(name);
    }

}

package com.biit.usermanager.rest.api;

import com.biit.usermanager.core.controller.OrganizationController;
import com.biit.usermanager.core.controller.models.OrganizationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationServices {
    private final OrganizationController organizationController;

    public OrganizationServices(OrganizationController organizationController) {
        this.organizationController = organizationController;
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_VIEWER')")
    @Operation(summary = "Gets all organizations.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrganizationDTO> getAll(HttpServletRequest request) {
        return organizationController.get();
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_VIEWER')")
    @Operation(summary = "Counts all organizations.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public long count(HttpServletRequest request) {
        return organizationController.count();
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_VIEWER')")
    @Operation(summary = "Gets a organization.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrganizationDTO get(@Parameter(description = "Id of an existing organization", required = true) @PathVariable("id") Long id,
                               HttpServletRequest request) {
        return organizationController.get(id);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Creates a organization.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrganizationDTO add(@RequestBody OrganizationDTO organizationDTO, HttpServletRequest request) {
        return organizationController.create(organizationDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Deletes a organization.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Id of an existing organization", required = true) @PathVariable("id") Long id,
                       HttpServletRequest request) {
        organizationController.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Deletes a organization.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody OrganizationDTO organizationDTO, HttpServletRequest request) {
        organizationController.delete(organizationDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Updates a organization.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrganizationDTO update(@RequestBody OrganizationDTO organizationDTO, HttpServletRequest request) {
        return organizationController.update(organizationDTO);
    }
}

package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.usermanager.core.controller.OrganizationController;
import com.biit.usermanager.core.converters.OrganizationConverter;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.repositories.OrganizationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/organizations")
public class OrganizationServices extends BasicServices<Organization, OrganizationDTO, OrganizationRepository,
        OrganizationProvider, OrganizationConverterRequest, OrganizationConverter, OrganizationController> {

    public OrganizationServices(OrganizationController organizationController) {
        super(organizationController);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Gets an organization by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrganizationDTO get(@Parameter(description = "Name of an existing organization", required = true) @PathVariable("name") String name,
                               HttpServletRequest request) {
        return controller.getByName(name);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Deletes an organization by name.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Name of an existing organization", required = true) @PathVariable("name") String name,
                       HttpServletRequest request) {
        controller.deleteByName(name);
    }
}

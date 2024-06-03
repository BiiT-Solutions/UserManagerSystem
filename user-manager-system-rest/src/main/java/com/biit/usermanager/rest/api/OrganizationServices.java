package com.biit.usermanager.rest.api;

import com.biit.server.rest.ElementServices;
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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/organizations")
public class OrganizationServices extends ElementServices<Organization, String, OrganizationDTO, OrganizationRepository,
        OrganizationProvider, OrganizationConverterRequest, OrganizationConverter, OrganizationController> {
    protected OrganizationServices(OrganizationController controller) {
        super(controller);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege)")
    @Operation(summary = "Get all user's organizations.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OrganizationDTO> getOrganizationsByUser(@Parameter(description = "User Id", required = true)
                                                              @PathVariable("userId") Long userId,
                                                              HttpServletRequest request) {
        return getController().getByUser(userId);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get current user's organizations.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OrganizationDTO> getOrganizationsByUser(Authentication authentication,
                                                              HttpServletRequest request) {
        return getController().getByUser(authentication.getName());
    }
}

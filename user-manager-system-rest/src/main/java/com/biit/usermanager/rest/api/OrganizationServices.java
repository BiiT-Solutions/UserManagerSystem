package com.biit.usermanager.rest.api;

/*-
 * #%L
 * User Manager System (Rest)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
import java.util.UUID;

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
        return getController().findByUserId(userId);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege)")
    @Operation(summary = "Get all user's organizations.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/users/uuids/{userUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OrganizationDTO> getOrganizationsByUser(@Parameter(description = "User Id", required = true)
                                                              @PathVariable("userUUID") UUID userUUID,
                                                              HttpServletRequest request) {
        return getController().findByUserUID(userUUID.toString());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege)")
    @Operation(summary = "Get all user's organizations.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/users/names/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OrganizationDTO> getOrganizationsByUser(@Parameter(description = "User Name", required = true)
                                                              @PathVariable("userName") String userName,
                                                              HttpServletRequest request) {
        return getController().findByUsername(userName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get current user's organizations.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OrganizationDTO> getOrganizationsByUser(Authentication authentication,
                                                              HttpServletRequest request) {
        return getController().findByUsername(authentication.getName());
    }
}

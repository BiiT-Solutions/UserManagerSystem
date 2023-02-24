package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.usermanager.core.controller.UserRoleController;
import com.biit.usermanager.core.converters.UserRoleConverter;
import com.biit.usermanager.core.converters.models.UserRoleConverterRequest;
import com.biit.usermanager.core.providers.UserRoleProvider;
import com.biit.usermanager.dto.UserRoleDTO;
import com.biit.usermanager.persistence.entities.UserRole;
import com.biit.usermanager.persistence.repositories.UserRoleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/roles/users")
public class UserRoleServices extends BasicServices<UserRole, UserRoleDTO, UserRoleRepository,
        UserRoleProvider, UserRoleConverterRequest, UserRoleConverter, UserRoleController> {

    public UserRoleServices(UserRoleController userRoleController) {
        super(userRoleController);
    }


    @PreAuthorize("hasRole(@securityService.viewerPrivilege)")
    @Operation(summary = "Get roles by username", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/username/{username}/organization/{organizationName}/application/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRoleDTO> getRolesFromUser(@Parameter(description = "Username of an existing user", required = true)
                                              @PathVariable("username") String username,
                                              @Parameter(description = "Organization name", required = true)
                                              @PathVariable("organizationName") String organizationName,
                                              @Parameter(description = "Application name", required = true)
                                              @PathVariable("applicationName") String applicationName,
                                              HttpServletRequest request) {
        return controller.getByUserAndOrganizationAndApplication(username, organizationName, applicationName);
    }
}

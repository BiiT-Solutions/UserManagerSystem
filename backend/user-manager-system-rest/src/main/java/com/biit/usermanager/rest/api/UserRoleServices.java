package com.biit.usermanager.rest.api;

import com.biit.usermanager.core.controller.UserRoleController;
import com.biit.usermanager.core.controller.models.UserRoleDTO;
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
@RequestMapping("/roles/users")
public class UserRoleServices {
    private final UserRoleController userRoleController;

    public UserRoleServices(UserRoleController userRoleController) {
        this.userRoleController = userRoleController;
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_VIEWER')")
    @Operation(summary = "Gets all UserRoles.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRoleDTO> getAll(HttpServletRequest request) {
        return userRoleController.get();
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_VIEWER')")
    @Operation(summary = "Counts all UserRoles.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public long count(HttpServletRequest request) {
        return userRoleController.count();
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_VIEWER')")
    @Operation(summary = "Gets a userRole.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRoleDTO get(@Parameter(description = "Id of an existing UserRole", required = true) @PathVariable("id") Long id,
                           HttpServletRequest request) {
        return userRoleController.get(id);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Creates a UserRole.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRoleDTO add(@RequestBody UserRoleDTO userRoleDTO, HttpServletRequest request) {
        return userRoleController.create(userRoleDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Deletes a UserRole.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Id of an existing userRole", required = true) @PathVariable("id") Long id,
                       HttpServletRequest request) {
        userRoleController.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Deletes a UserRole.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody UserRoleDTO userRoleDTO, HttpServletRequest request) {
        userRoleController.delete(userRoleDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Updates a UserRole.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRoleDTO update(@RequestBody UserRoleDTO userRoleDTO, HttpServletRequest request) {
        return userRoleController.update(userRoleDTO);
    }
}

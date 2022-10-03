package com.biit.usermanager.rest.api;

import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.controller.models.UserDTO;
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
@RequestMapping("/users")
public class UserServices {
    private final UserController userController;

    public UserServices(UserController userController) {
        this.userController = userController;
    }

    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @Operation(summary = "Gets all users.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getAll(HttpServletRequest request) {
        return userController.get();
    }

    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @Operation(summary = "Counts all users.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public long count(HttpServletRequest request) {
        return userController.count();
    }

    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @Operation(summary = "Gets a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO get(@Parameter(description = "Id of an existing user", required = true) @PathVariable("id") Long id,
                       HttpServletRequest request) {
        return userController.get(id);
    }


    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @Operation(summary = "Get user by name", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO get(@Parameter(description = "Name of an existing user", required = true) @PathVariable("name") String name,
                       HttpServletRequest request) {
        return userController.getByUserName(name);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Creates a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes =  MediaType.APPLICATION_JSON_VALUE)
    public UserDTO add(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        return userController.create(userDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Deletes a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Parameter(description = "Id of an existing user", required = true) @PathVariable("id") Long id,
                       HttpServletRequest request) {
        userController.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get user by phone number", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByPhone(@Parameter(description = "Phone of an existing user", required = true) @PathVariable("phone") String phone,
                       HttpServletRequest request) {
        return userController.getByPhone(phone);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Gets a list of expired users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{account_expired}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getByAccExpired(@Parameter(description = "Account is expired", required = true)
                                             @PathVariable("account_expired") boolean accountExpired,
                              HttpServletRequest request) {
        return userController.getAllByExpired(accountExpired);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Deletes a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        userController.delete(userDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Updates a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO update(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        return userController.update(userDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER_MANAGER_ADMIN')")
    @Operation(summary = "Gets all enable/disable users .", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/enable/{enable}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getEnable( @Parameter(description = "enable/disable", required = true)
                                        @PathVariable("enable") boolean enable, HttpServletRequest request) {
        return userController.getByEnable(enable);
    }
}

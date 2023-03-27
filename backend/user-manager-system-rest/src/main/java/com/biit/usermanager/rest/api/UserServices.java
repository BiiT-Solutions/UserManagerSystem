package com.biit.usermanager.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.server.security.model.UpdatePasswordRequest;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import com.biit.usermanager.rest.api.models.CheckCredentialsRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserServices extends BasicServices<User, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter, UserController> {

    public UserServices(UserController userController) {
        super(userController);
    }

    //@PreAuthorize("hasAuthority('USERMANAGERSYSTEM_VIEWER')")
    @PreAuthorize("hasAuthority(@securityService.viewerPrivilege)")
    @Operation(summary = "Get user by username", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUsername(@Parameter(description = "Username of an existing user", required = true) @PathVariable("username") String username,
                                 HttpServletRequest request) {
        return controller.getByUsername(username);
    }

    @PreAuthorize("hasAuthority(@securityService.viewerPrivilege)")
    @Operation(summary = "Get user by email", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByEmail(@Parameter(description = "Email of an existing user", required = true) @PathVariable("email") String email,
                              HttpServletRequest request) {
        return controller.getByEmail(email);
    }

    @PreAuthorize("hasAuthority(@securityService.viewerPrivilege)")
    @Operation(summary = "Check user and password", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/credentials", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO checkCredentials(@RequestBody CheckCredentialsRequest credentialsRequest,
                                    HttpServletRequest request) {
        return controller.checkCredentials(credentialsRequest.getUsername(), credentialsRequest.getEmail(), credentialsRequest.getPassword());
    }

    @PreAuthorize("hasAuthority(@securityService.viewerPrivilege)")
    @Operation(summary = "Get user by username and application", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/username/{username}/application/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUsernameAndApplication(@Parameter(description = "Username of an existing user", required = true)
                                               @PathVariable("username") String username,
                                               @Parameter(description = "Name of an existing application", required = true)
                                               @PathVariable("applicationName") String applicationName,
                                               HttpServletRequest request) {
        return (UserDTO) controller.findByUsername(username, applicationName).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
    }

    @PreAuthorize("hasAuthority(@securityService.viewerPrivilege)")
    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUUID(@Parameter(description = "Name of an existing user", required = true) @PathVariable("id") String id,
                             HttpServletRequest request) {
        return controller.getByUserId(id);
    }

    @PreAuthorize("hasAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Get user by phone number", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/phone/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByPhone(@Parameter(description = "Phone of an existing user", required = true) @PathVariable("phone") String phone,
                              HttpServletRequest request) {
        return controller.getByPhone(phone);
    }

    @PreAuthorize("hasAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets a list of expired users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/account_expired/{account_expired}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getByAccountExpired(@Parameter(description = "Account is expired", required = true)
                                             @PathVariable("account_expired") boolean accountExpired,
                                             HttpServletRequest request) {
        return controller.getAllByExpired(accountExpired);
    }

    @PreAuthorize("hasAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all enable/disable users .", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/enable/{enable}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getEnabled(@Parameter(description = "enable/disable", required = true)
                                    @PathVariable("enable") boolean enable, HttpServletRequest request) {
        return controller.getByEnable(enable);
    }

    @Operation(summary = "Updates the password of the current logged in user.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(path = "/password")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void updatePassword(@RequestBody UpdatePasswordRequest request, Authentication authentication, HttpServletRequest httpRequest) {
        try {
            controller.updatePassword(authentication.getName(), request.getOldPassword(), request.getNewPassword());
        } catch (Exception e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
        }
    }

    @PreAuthorize("hasAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Updates a password.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(path = "/{username}/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public UserDTO updateUserPassword(@Parameter(description = "username", required = true)
                                      @PathVariable("username") String username,
                                      @RequestBody UpdatePasswordRequest request, Authentication authentication, HttpServletRequest httpRequest) {
        try {
            return (UserDTO) controller.updatePassword(username, request.getOldPassword(), request.getNewPassword());
        } catch (Exception e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
        }
        return null;
    }

    @PreAuthorize("hasAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(path = "/{username}/")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public UserDTO deleteUser(@Parameter(description = "username", required = true)
                              @PathVariable("username") String username, Authentication authentication, HttpServletRequest httpRequest) {
        try {
            return controller.delete(username);
        } catch (Exception e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
        }
        throw new UserNotFoundException(this.getClass(), "No user found with username '" + username + "'");
    }
}

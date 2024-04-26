package com.biit.usermanager.rest.api;

import com.biit.server.exceptions.BadRequestException;
import com.biit.server.rest.ElementServices;
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
import com.biit.usermanager.rest.api.models.PasswordChangeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserServices extends ElementServices<User, Long, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter, UserController> {

    public UserServices(UserController userController) {
        super(userController);
    }

    //@PreAuthorize("hasAuthority('USERMANAGERSYSTEM_ADMIN')")
    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get user by username", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/usernames/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUsername(@Parameter(description = "Username of an existing user", required = true) @PathVariable("username") String username,
                                 HttpServletRequest request) {
        return getController().getByUsername(username);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get user by email", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/emails/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByEmail(@Parameter(description = "Email of an existing user", required = true) @PathVariable("email") String email,
                              HttpServletRequest request) {
        return (UserDTO) getController().findByEmailAddress(email).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with email '" + email + "' found on the system."));
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Check user and password", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/credentials", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO checkCredentials(@RequestBody CheckCredentialsRequest credentialsRequest,
                                    HttpServletRequest request) {
        return getController().checkCredentials(credentialsRequest.getUsername(), credentialsRequest.getEmail(), credentialsRequest.getPassword());
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get user by username and application. The granted authorities are filtered by the application name.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/usernames/{username}/applications/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUsernameAndApplication(@Parameter(description = "Username of an existing user", required = true)
                                               @PathVariable("username") String username,
                                               @Parameter(description = "Name of an existing application", required = true)
                                               @PathVariable("applicationName") String applicationName,
                                               HttpServletRequest request) {
        return (UserDTO) getController().findByUsernameAndApplication(username, applicationName).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get user by username and backend service. The granted authorities are filtered by the selected service name.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/usernames/{username}/service/{backendServiceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUsernameAndBackendService(@Parameter(description = "Username of an existing user", required = true)
                                                  @PathVariable("username") String username,
                                                  @Parameter(description = "Name of an existing service", required = true)
                                                  @PathVariable("backendServiceName") String backendServiceName,
                                                  HttpServletRequest request) {
        return (UserDTO) getController().findByUsername(username, backendServiceName).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get user by email and application", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/emails/{email}/applications/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByEmailAndApplication(@Parameter(description = "Email of an existing user", required = true)
                                            @PathVariable("email") String email,
                                            @Parameter(description = "Name of an existing application", required = true)
                                            @PathVariable("applicationName") String applicationName,
                                            HttpServletRequest request) {
        return (UserDTO) getController().findByEmailAddress(email, applicationName).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with email '" + email + "' found on the system."));
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/uuids/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUUID(@Parameter(description = "Name of an existing user", required = true) @PathVariable("uuid") String uuid,
                             HttpServletRequest request) {
        return (UserDTO) getController().findByUID(uuid).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with uuid '" + uuid + "' found on the system."));
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Get user by phone number", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/phones/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByPhone(@Parameter(description = "Phone of an existing user", required = true) @PathVariable("phone") String phone,
                              HttpServletRequest request) {
        return getController().getByPhone(phone);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets a list of expired users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/accounts-expired/{account_expired}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getByAccountExpired(@Parameter(description = "Account is expired", required = true)
                                             @PathVariable("account_expired") boolean accountExpired,
                                             HttpServletRequest request) {
        return getController().getAllByExpired(accountExpired);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all enable/disable users .", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/enabled/{enabled}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getEnabled(@Parameter(description = "enabled/disabled", required = true)
                                    @PathVariable("enabled") boolean enable, HttpServletRequest request) {
        return getController().getByAccountBlocked(enable);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Updates the password of the current logged in user.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = "/passwords")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void updatePassword(@RequestBody UpdatePasswordRequest request, Authentication authentication, HttpServletRequest httpRequest) {
        try {
            getController().updatePassword(authentication.getName(), request.getOldPassword(), request.getNewPassword(), authentication.getName());
        } catch (Exception e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
        }
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Updates a password by an admin user. Does not require to know the old password.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = "/{username}/passwords", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public UserDTO updateUserPassword(@Parameter(description = "username", required = true)
                                      @PathVariable("username") String username,
                                      @RequestBody UpdatePasswordRequest request, Authentication authentication, HttpServletRequest httpRequest) {
        try {
            return (UserDTO) getController().updatePassword(username, request.getNewPassword(), authentication.getName());
        } catch (Exception e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
        }
        return null;
    }


    @Operation(summary = "Checks if a username is already taken or not.")
    @GetMapping(path = "/public/{username}/check")
    @ResponseStatus(value = HttpStatus.OK)
    public void checkUsernameExists(@Parameter(description = "username", required = true)
                                    @PathVariable("username") String username,
                                    HttpServletRequest httpRequest) {
        getController().checkUsernameExists(username);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets an encrypted password hash.", security = @SecurityRequirement(name = "bearerAuth"), hidden = true)
    @GetMapping(path = "/{username}/passwords", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public String getsUserPassword(@Parameter(description = "username", required = true) @PathVariable("username") String username,
                                   Authentication authentication, HttpServletRequest httpRequest) {
        try {
            return getController().getPassword(username);
        } catch (Exception e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
        }
        return null;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets an encrypted password hash.", security = @SecurityRequirement(name = "bearerAuth"), hidden = true)
    @GetMapping(path = "/uids/{uids}/passwords", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public String getsUserPasswordById(@Parameter(description = "The UID from the user", required = true) @PathVariable("uids") String uids,
                                       Authentication authentication, HttpServletRequest httpRequest) {
        try {
            return getController().getPasswordByUid(uids);
        } catch (Exception e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
        }
        return null;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes a user by username.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(path = "/usernames/{username}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteUser(@Parameter(description = "username", required = true)
                           @PathVariable("username") String username, Authentication authentication, HttpServletRequest httpRequest) {
        getController().delete(username, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Assign roles to a user. Generates the intermediate structure if needed.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/usernames/{username}/applications/{applicationName}/application-roles/{applicationRoleName}"
            + "/backend-services/{backendServiceName}/backend-service-roles/{backendServiceRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getRolesFromApplicationAndBackendServices(
            @Parameter(description = "Username of an existing user", required = true)
            @PathVariable("username") String username,
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            @Parameter(description = "Backend Service name", required = true)
            @PathVariable("backendServiceName") String backendServiceName,
            @Parameter(description = "Backend Role name", required = true)
            @PathVariable("backendServiceRoleName") String backendServiceRoleName,
            HttpServletRequest request) {
        return getController().assign(username, applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Assign application roles to a user. Assigns all related backend services that are defined.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/usernames/{username}/applications/{applicationName}/application-roles/{applicationRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO setRolesFromApplicationAndRoles(
            @Parameter(description = "Username of an existing user", required = true)
            @PathVariable("username") String username,
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            HttpServletRequest request) {
        return getController().assign(username, applicationName, applicationRoleName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Deletes application roles from a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/usernames/{username}/applications/{applicationName}/application-roles/{applicationRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO deleteRolesFromApplicationAndRoles(
            @Parameter(description = "Username of an existing user", required = true)
            @PathVariable("username") String username,
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            Authentication authentication,
            HttpServletRequest request) {
        return getController().unAssign(username, applicationName, applicationRoleName, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get UserGroup's users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/user-group/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsers(@Parameter(description = "Id of an existing user group", required = true) @PathVariable("id") Long id,
                                  HttpServletRequest request) {
        return getController().getByUserGroup(id);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get Team's users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsersByTeam(@Parameter(description = "Id of an existing team", required = true) @PathVariable("id") Long id,
                                        HttpServletRequest request) {
        return getController().getByTeam(id);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege)")
    @Operation(summary = "Get Organization's users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/organanizations/{organizationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsersByOrganization(@Parameter(description = "Name of an existing organization", required = true) @PathVariable("organizationName")
                                                String organizationName,
                                                HttpServletRequest request) {
        return getController().getByOrganization(organizationName);
    }


    @Operation(summary = "Generates a token for reseting the password", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/public/emails/{email}/reset-password")
    public void createResetPasswordToken(@Parameter(description = "Email from an existing user", required = true) @PathVariable("email") String email,
                                         HttpServletRequest request) {
        getController().resetPassword(email);
    }


    @Operation(summary = "Changes the password from a user, using a token", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/public/change-password")
    public void resetPasswordFromToken(@Parameter(description = "Token obtained by mail", required = true) @RequestParam("token") String token,
                                       @RequestBody(required = true) PasswordChangeRequest passwordChangeRequest,
                                       HttpServletRequest request) {
        if (passwordChangeRequest == null || passwordChangeRequest.getNewPassword() == null) {
            throw new BadRequestException(this.getClass(), "Password is not set correctly.");
        }
        getController().updatePassword(token, passwordChangeRequest.getNewPassword());
    }
}

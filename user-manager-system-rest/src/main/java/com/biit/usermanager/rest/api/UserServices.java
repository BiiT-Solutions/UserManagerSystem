package com.biit.usermanager.rest.api;

import com.biit.database.encryption.SHA512HashGenerator;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.server.controllers.models.ElementDTO;
import com.biit.server.exceptions.BadRequestException;
import com.biit.server.logger.RestServerLogger;
import com.biit.server.providers.StorableObjectProvider;
import com.biit.server.rest.ElementServices;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IAuthenticatedUser;
import com.biit.server.security.model.IUserOrganization;
import com.biit.server.security.model.UpdatePasswordRequest;
import com.biit.server.security.rest.BruteForceService;
import com.biit.server.security.rest.NetworkController;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.exceptions.UserAlreadyExistsException;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
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

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/users")
public class UserServices extends ElementServices<User, Long, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter, UserController> {

    private static final int MAX_WAITING_SECONDS = 10;
    private static final long MILLIS = 1000L;

    private final SecureRandom random = new SecureRandom();

    private final NetworkController networkController;
    private final BruteForceService bruteForceService;
    private final UserManagerSecurityService securityService;
    private final List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider;

    public UserServices(UserController userController, NetworkController networkController, BruteForceService bruteForceService,
                        UserManagerSecurityService securityService, List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(userController);
        this.networkController = networkController;
        this.bruteForceService = bruteForceService;
        this.securityService = securityService;
        this.userOrganizationProvider = userOrganizationProvider;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get user by username", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/usernames/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUsername(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
            @Parameter(description = "Username of an existing user", required = true)
            @PathVariable("username") String username,
            HttpServletRequest request, Authentication authentication) {
        canBeDoneByDifferentUsers(username, authentication);
        return getController().getByUsername(username);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege)")
    @Operation(summary = "Get user by email", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/emails/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByEmail(@Parameter(description = "Email of an existing user", required = true) @Email @PathVariable("email") String email,
                              HttpServletRequest request, Authentication authentication) {

        final UserDTO user = getController().findByEmailAddress(email).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with email '" + email + "' found on the system."));
        canBeDoneByDifferentUsers(user.getUsername(), authentication);
        return user;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege, @securityService.viewerPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Check user and password", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/credentials", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO checkCredentials(@Valid @RequestBody CheckCredentialsRequest credentialsRequest,
                                    HttpServletRequest request) {
        return getController().checkCredentials(credentialsRequest.getUsername(), credentialsRequest.getEmail(), credentialsRequest.getPassword());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get user by username and application. The granted authorities are filtered by the application name.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/usernames/{username}/applications/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUsernameAndApplication(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
            @Parameter(description = "Username of an existing user", required = true)
            @PathVariable("username") String username,
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Name of an existing application", required = true)
            @PathVariable("applicationName") String applicationName,
            HttpServletRequest request, Authentication authentication) {
        final UserDTO user = getController().findByUsernameAndApplication(username, applicationName).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
        canBeDoneByDifferentUsers(user.getUsername(), authentication);
        return user;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get user by username and backend service. The granted authorities are filtered by the selected service name.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/usernames/{username}/service/{backendServiceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUsernameAndBackendService(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
            @Parameter(description = "Username of an existing user", required = true)
            @PathVariable("username") String username,
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Name of an existing service", required = true)
            @PathVariable("backendServiceName") String backendServiceName,
            HttpServletRequest request, Authentication authentication) {
        final UserDTO user = getController().findByUsername(username, backendServiceName).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
        canBeDoneByDifferentUsers(user.getUsername(), authentication);
        return user;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get user by email and application", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/emails/{email}/applications/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByEmailAndApplication(@Parameter(description = "Email of an existing user", required = true)
                                            @Email @PathVariable("email") String email,
                                            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
                                            @Parameter(description = "Name of an existing application", required = true)
                                            @PathVariable("applicationName") String applicationName,
                                            HttpServletRequest request, Authentication authentication) {
        final UserDTO user = getController().findByEmailAddress(email, applicationName).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with email '" + email + "' found on the system."));
        canBeDoneByDifferentUsers(user.getUsername(), authentication);
        return user;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/uuids/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getByUUID(@Parameter(description = "Name of an existing user", required = true) @PathVariable("uuid") String uuid,
                             HttpServletRequest request, Authentication authentication) {
        final UserDTO user = getController().findByUID(uuid).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with uuid '" + uuid + "' found on the system."));
        canBeDoneByDifferentUsers(user.getUsername(), authentication);
        return user;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Gets all entities that have these uuids", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/uuids", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<UserDTO> getAllByUUIDs(@Parameter(description = "List of users' uuids.")
                                             @RequestParam Collection<UUID> uuids,
                                             Authentication authentication, HttpServletRequest request) {
        final List<String> grantedAuthorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (!grantedAuthorities.contains(securityService.getAdminPrivilege()) && !grantedAuthorities.contains(securityService.getEditorPrivilege())) {
            return getController().findByUIDsInUserOrganizations(uuids, authentication.getName());
        }
        return getController().findByUIDs(uuids);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Gets all entities that have these uuids", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/uuids", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Collection<UserDTO> getAllByUUIDsInBody(@Parameter(description = "List of users' uuids.")
                                                   @RequestBody Collection<UUID> uuids,
                                                   Authentication authentication, HttpServletRequest request) {
        final List<String> grantedAuthorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (!grantedAuthorities.contains(securityService.getAdminPrivilege()) && !grantedAuthorities.contains(securityService.getEditorPrivilege())) {
            return getController().findByUIDsInUserOrganizations(uuids, authentication.getName());
        }
        return getController().findByUIDs(uuids);
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


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Updates a password by an admin user. Does not require to know the old password.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = "/{username}/passwords", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public UserDTO updateUserPassword(@Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
                                      @Parameter(description = "username", required = true)
                                      @PathVariable("username") String username,
                                      @RequestBody UpdatePasswordRequest request, Authentication authentication, HttpServletRequest httpRequest) {
        canBeDoneByDifferentUsers(username, authentication);
        try {
            return (UserDTO) getController().updatePassword(username, request.getNewPassword(), authentication.getName());
        } catch (Exception e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
        }
        return null;
    }


    @Operation(summary = "Adds a new user into the system. For Sign Up")
    @PostMapping(path = "/public/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public IAuthenticatedUser register(@RequestBody CreateUserRequest request, Authentication authentication, HttpServletRequest httpRequest) {
        UserManagerLogger.warning(this.getClass(), "Creating a new user from ip '" + networkController.getClientIP(httpRequest) + "'.");
        return getController().createPublicUser(request, request.getUsername());
    }


    @Operation(summary = "Checks if a username is available or not.")
    @GetMapping(path = "/public/{username}/available")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Void> checkUsernameExists(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
            @Parameter(description = "username", required = true)
            @PathVariable("username") String username,
            HttpServletRequest httpRequest) {
        final String ip = networkController.getClientIP(httpRequest);
        if (bruteForceService.isBlocked(ip)) {
            try {
                Thread.sleep(random.nextInt(MAX_WAITING_SECONDS) * MILLIS);
            } catch (InterruptedException e) {
                try {
                    RestServerLogger.warning(this.getClass(), "Too many attempts from IP '" + ip + "'.");
                    final HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.RETRY_AFTER, String.valueOf(bruteForceService.getElementsTime().get(ip)
                            + bruteForceService.getExpirationTime()));
                    return new ResponseEntity<>(headers, HttpStatus.LOCKED);
                } finally {
                    Thread.currentThread().interrupt();
                }
            }
        }
        UserManagerLogger.warning(this.getClass(), "Checking if a user exists from ip '" + ip + "'.");
        try {
            getController().checkUsernameExists(username);
            bruteForceService.loginSucceeded(ip);
            throw new UserAlreadyExistsException(this.getClass(), "User already exists.");
        } catch (UserNotFoundException ignored) {
            bruteForceService.loginFailed(ip);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets an encrypted password hash.", security = @SecurityRequirement(name = "bearerAuth"), hidden = true)
    @GetMapping(path = "/{username}/passwords", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public String getsUserPassword(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
            @Parameter(description = "username", required = true)
            @PathVariable("username") String username,
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


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Deletes a user by username.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(path = "/usernames/{username}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void deleteUser(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
            @Parameter(description = "username", required = true)
            @PathVariable("username") String username,
            Authentication authentication, HttpServletRequest httpRequest) {
        canBeDoneByDifferentUsers(username, authentication);
        getController().getByUsername(username);
        getController().delete(username, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
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
            Authentication authentication, HttpServletRequest request) {
        canBeDoneByDifferentUsers(username, authentication);
        return getController().assign(username, applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Assign application roles to a user. Assigns all related backend services that are defined.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/usernames/{username}/applications/{applicationName}/application-roles/{applicationRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO setRolesFromApplicationAndRoles(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
            @Parameter(description = "Username of an existing user", required = true)
            @PathVariable("username") String username,
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            Authentication authentication, HttpServletRequest request) {
        canBeDoneByDifferentUsers(username, authentication);
        return getController().assign(username, applicationName, applicationRoleName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Deletes application roles from a user.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/usernames/{username}/applications/{applicationName}/application-roles/{applicationRoleName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO deleteRolesFromApplicationAndRoles(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_SMALL_FIELD_LENGTH)
            @Parameter(description = "Username of an existing user", required = true)
            @PathVariable("username") String username,
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Application name", required = true)
            @PathVariable("applicationName") String applicationName,
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Application Role name", required = true)
            @PathVariable("applicationRoleName") String applicationRoleName,
            Authentication authentication, HttpServletRequest request) {
        canBeDoneByDifferentUsers(username, authentication);
        return getController().unAssign(username, applicationName, applicationRoleName, authentication.getName());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get UserGroup's users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/user-groups/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsers(@Parameter(description = "Id of an existing user group", required = true) @PathVariable("id") Long id,
                                  Authentication authentication, HttpServletRequest request) {
        final List<UserDTO> users = getController().getByUserGroup(id);
        final List<String> grantedAuthorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (!grantedAuthorities.contains(securityService.getAdminPrivilege()) && !grantedAuthorities.contains(securityService.getEditorPrivilege())) {
            final Collection<? extends IUserOrganization> editorOrganizations = userOrganizationProvider.get(0).findByUsername(authentication.getName());
            final Collection<String> organizationHashes = editorOrganizations.stream().map(o ->
                    SHA512HashGenerator.createHash(o.getName())).toList();
            return users.stream().filter(user -> organizationHashes.contains(user.getCreatedOn())).toList();
        }
        return users;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get UserGroup's users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/user-groups/names/{groupName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsersByUserGroup(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Name of an existing user group", required = true)
            @PathVariable("groupName") String groupName,
            HttpServletRequest request) {
        return getController().getByUserGroup(groupName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get users from team", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/teams/{teamId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsersByTeam(
            @Parameter(description = "Id of an existing team", required = true) @PathVariable("teamId") Long teamId,
            @RequestParam(name = "page", defaultValue = "0") Optional<Integer> page,
            @RequestParam(name = "size", defaultValue = StorableObjectProvider.MAX_PAGE_SIZE + "") Optional<Integer> size,
            HttpServletRequest request) {
        if (size.isPresent()) {
            return getController().getByTeam(teamId, page.orElse(0), size.get());
        }
        return getController().getByTeam(teamId);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get users from team", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/organizations/{organizationName}/teams/{teamName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsersByTeam(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Name of an existing organization", required = true)
            @PathVariable("organizationName") String organizationName,
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Name of an existing team", required = true)
            @PathVariable("teamName") String teamName,
            @RequestParam(name = "page", defaultValue = "0") Optional<Integer> page,
            @RequestParam(name = "size", defaultValue = StorableObjectProvider.MAX_PAGE_SIZE + "") Optional<Integer> size,
            Authentication authentication, HttpServletRequest request) {
        checkHasOrganizationAdminAccess(organizationName, authentication,
                securityService.getAdminPrivilege(), securityService.getEditorPrivilege());
        if (size.isPresent()) {
            return getController().getByTeam(organizationName, teamName, page.orElse(0), size.get());
        }
        return getController().getByTeam(organizationName, teamName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Count users from team", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/organizations/{organizationName}/teams/{teamName}/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public long countUsersByTeam(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Name of an existing organization", required = true) @PathVariable("organizationName") String organizationName,
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Name of an existing team", required = true) @PathVariable("teamName") String teamName,
            Authentication authentication, HttpServletRequest request) {
        checkHasOrganizationAdminAccess(organizationName, authentication,
                securityService.getAdminPrivilege(), securityService.getEditorPrivilege());
        return getController().countByTeam(organizationName, teamName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Get Organization's users", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/organizations/{organizationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getUsersByOrganization(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Name of an existing organization", required = true)
            @PathVariable("organizationName")
            String organizationName,
            Authentication authentication, HttpServletRequest request) {
        checkHasOrganizationAdminAccess(organizationName, authentication,
                securityService.getAdminPrivilege(), securityService.getEditorPrivilege());
        return getController().getByOrganization(organizationName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.adminPrivilege, @securityService.editorPrivilege)")
    @Operation(summary = "Count users from team", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/organizations/{organizationName}/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public long countUsersByOrganization(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Name of an existing organization", required = true)
            @PathVariable("organizationName") String organizationName,
            Authentication authentication, HttpServletRequest request) {
        checkHasOrganizationAdminAccess(organizationName, authentication,
                securityService.getAdminPrivilege(), securityService.getEditorPrivilege());
        return getController().countByOrganization(organizationName);
    }


    @Operation(summary = "Generates a token for reseting the password")
    @GetMapping(value = "/public/emails/{email}/reset-password")
    public void createResetPasswordToken(@Parameter(description = "Email from an existing user", required = true)
                                         @Email @PathVariable("email") String email,
                                         HttpServletRequest request) throws EmailNotSentException {
        UserManagerLogger.warning(this.getClass(), "Requesting to rest a password from ip '" + networkController.getClientIP(request) + "'.");
        getController().resetPassword(email);
    }


    @Operation(summary = "Checks the validity of a token")
    @GetMapping(value = "/public/tokens")
    public void checkToken(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Token to check", required = true) @RequestParam("token") String token,
            HttpServletRequest request) {
        UserManagerLogger.warning(this.getClass(), "Checking the validity of a token from ip '" + networkController.getClientIP(request) + "'.");
        getController().checkToken(token);
    }


    @Operation(summary = "Changes the password from a user, using a token")
    @PostMapping(value = "/public/change-password")
    public void resetPasswordFromToken(@Parameter(description = "Token obtained by mail", required = true) @RequestParam("token") String token,
                                       @RequestBody(required = true) @Valid PasswordChangeRequest passwordChangeRequest,
                                       HttpServletRequest request) {
        UserManagerLogger.warning(this.getClass(), "Changing a password from ip '" + networkController.getClientIP(request) + "'.");
        if (passwordChangeRequest == null || passwordChangeRequest.getNewPassword() == null) {
            throw new BadRequestException(this.getClass(), "Password request is not set correctly.");
        }
        getController().updatePassword(token, passwordChangeRequest.getNewPassword());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets a user by its external reference.", security = @SecurityRequirement(name = "bearerAuth"), hidden = true)
    @GetMapping(path = "/references/{externalReference}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public UserDTO getsUserByExternalReference(
            @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
            @Parameter(description = "Reference from a 3rd party application.", required = true)
            @PathVariable("externalReference") String externalReference,
            Authentication authentication, HttpServletRequest httpRequest) {
        return getController().getByExternalReference(externalReference);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets multiple users by their external references.", security = @SecurityRequirement(name = "bearerAuth"), hidden = true)
    @GetMapping(path = "/references", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<UserDTO> getsUserByExternalReference(@Parameter(description = "List of references", required = true) @RequestParam("references")
                                                           List<String> externalReferences, Authentication authentication, HttpServletRequest httpRequest) {
        return getController().getByExternalReference(externalReferences);
    }
}

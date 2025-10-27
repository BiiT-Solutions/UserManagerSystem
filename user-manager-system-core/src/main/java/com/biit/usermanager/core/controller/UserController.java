package com.biit.usermanager.core.controller;

/*-
 * #%L
 * User Manager System (core)
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

import com.biit.kafka.controllers.KafkaElementController;
import com.biit.kafka.events.EventSubject;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.exceptions.ActionNotAllowedException;
import com.biit.server.security.model.IAuthenticatedUser;
import com.biit.server.security.model.IUserOrganization;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationNotFoundException;
import com.biit.usermanager.core.exceptions.ApplicationRoleNotFoundException;
import com.biit.usermanager.core.exceptions.BackendServiceNotFoundException;
import com.biit.usermanager.core.exceptions.BackendServiceRoleNotFoundException;
import com.biit.usermanager.core.exceptions.EmailAlreadyExistsException;
import com.biit.usermanager.core.exceptions.EmailNotFoundException;
import com.biit.usermanager.core.exceptions.ExternalReferenceAlreadyExistsException;
import com.biit.usermanager.core.exceptions.InvalidParameterException;
import com.biit.usermanager.core.exceptions.InvalidPasswordException;
import com.biit.usermanager.core.exceptions.OrganizationNotFoundException;
import com.biit.usermanager.core.exceptions.RoleWithoutBackendServiceRoleException;
import com.biit.usermanager.core.exceptions.TeamNotFoundException;
import com.biit.usermanager.core.exceptions.TokenExpiredException;
import com.biit.usermanager.core.exceptions.UserAlreadyExistsException;
import com.biit.usermanager.core.exceptions.UserDoesNotExistsException;
import com.biit.usermanager.core.exceptions.UserGroupNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.kafka.UserEventSender;
import com.biit.usermanager.core.providers.ApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.ApplicationRoleProvider;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.core.providers.BackendServiceRoleProvider;
import com.biit.usermanager.core.providers.EmailService;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.core.providers.PasswordResetTokenProvider;
import com.biit.usermanager.core.providers.RoleProvider;
import com.biit.usermanager.core.providers.TeamMemberProvider;
import com.biit.usermanager.core.providers.TeamProvider;
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserGroupApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserGroupProvider;
import com.biit.usermanager.core.providers.UserGroupUserProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.core.utils.RoleNameGenerator;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.ApplicationRole;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.PasswordResetToken;
import com.biit.usermanager.persistence.entities.Team;
import com.biit.usermanager.persistence.entities.TeamMember;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserGroup;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.UserGroupUser;
import com.biit.usermanager.persistence.repositories.UserRepository;
import com.biit.usermanager.security.exceptions.OrganizationDoesNotExistException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.biit.server.providers.StorableObjectProvider.DEFAULT_PAGE_SIZE;

@Controller
@Order(1)
@Qualifier("userController")
public class UserController extends KafkaElementController<User, Long, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter> implements IAuthenticatedUserProvider<UserDTO> {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ADMIN_AUTHORITY = "USERMANAGERSYSTEM_ADMIN";

    @Value("${bcrypt.salt:}")
    private String bcryptSalt;

    @Value("#{new Boolean('${user.public.register.enabled:false}')}")
    private boolean allowPublicRegistration = false;

    @Value("${user.public.register.duration.hours:1}")
    private int publicUserExpirationHours = 1;


    private final ApplicationProvider applicationProvider;
    private final BackendServiceProvider backendServiceProvider;

    private final UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider;
    private final UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider;

    private final ApplicationBackendServiceRoleProvider applicationBackendServiceRoleProvider;

    private final ApplicationRoleProvider applicationRoleProvider;
    private final BackendServiceRoleProvider backendServiceRoleProvider;

    private final UserGroupProvider userGroupProvider;

    private final TeamProvider teamProvider;

    private final OrganizationProvider organizationProvider;

    private final UserGroupUserProvider userGroupUserProvider;

    private final TeamMemberProvider teamMemberProvider;

    private final PasswordResetTokenProvider passwordResetTokenProvider;

    private final EmailService emailService;

    private final RoleProvider roleProvider;

    private final List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider;

    @Autowired
    protected UserController(UserProvider provider, UserConverter converter,
                             ApplicationProvider applicationProvider, BackendServiceProvider backendServiceProvider,
                             UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider,
                             UserGroupApplicationBackendServiceRoleProvider userGroupApplicationBackendServiceRoleProvider,
                             ApplicationBackendServiceRoleProvider applicationBackendServiceRoleProvider,
                             ApplicationRoleProvider applicationRoleProvider, BackendServiceRoleProvider backendServiceRoleProvider,
                             UserEventSender userEventSender, UserGroupProvider userGroupProvider,
                             TeamProvider teamProvider, OrganizationProvider organizationProvider, UserGroupUserProvider userGroupUserRepository,
                             TeamMemberProvider teamMemberProvider, PasswordResetTokenProvider passwordResetTokenProvider,
                             EmailService emailService, RoleProvider roleProvider,
                             List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(provider, converter, userEventSender, userOrganizationProvider);
        this.applicationProvider = applicationProvider;
        this.backendServiceProvider = backendServiceProvider;
        this.userApplicationBackendServiceRoleProvider = userApplicationBackendServiceRoleProvider;
        this.userGroupApplicationBackendServiceRoleProvider = userGroupApplicationBackendServiceRoleProvider;
        this.applicationBackendServiceRoleProvider = applicationBackendServiceRoleProvider;
        this.applicationRoleProvider = applicationRoleProvider;
        this.backendServiceRoleProvider = backendServiceRoleProvider;
        this.userGroupProvider = userGroupProvider;
        this.teamProvider = teamProvider;
        this.organizationProvider = organizationProvider;
        this.userGroupUserProvider = userGroupUserRepository;
        this.teamMemberProvider = teamMemberProvider;
        this.passwordResetTokenProvider = passwordResetTokenProvider;
        this.emailService = emailService;
        this.roleProvider = roleProvider;
        this.userOrganizationProvider = userOrganizationProvider;
    }

    private Collection<? extends IUserOrganization> sharedOrganizations(String username1, String username2) {
        final Collection<? extends IUserOrganization> userOrganizations = userOrganizationProvider.get(0).findByUsername(username1);
        final Collection<? extends IUserOrganization> editorOrganizations = userOrganizationProvider.get(0).findByUsername(username2);
        userOrganizations.retainAll(editorOrganizations);
        return userOrganizations;
    }


    @Override
    protected UserConverterRequest createConverterRequest(User entity) {
        return new UserConverterRequest(entity);
    }

    public UserDTO getByUsername(String username) {
        if (username == null) {
            return null;
        }
        final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                new UserDoesNotExistsException(this.getClass(), "No user with username '" + username + "' found on the system."))));
        return setGrantedAuthorities(userDTO, null, null);
    }


    public UserDTO checkCredentials(String username, String email, String password) {
        final User user;
        if (username != null) {
            try {
                user = getProvider().findByUsername(username).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                        "No user with username '" + username + "' found on the system."));
            } catch (Exception e) {
                UserManagerLogger.warning(this.getClass(), "No User with username '" + username + "' found on the system.");
                throw e;
            }
        } else {
            try {
                user = getProvider().findByEmail(email).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                        "No User with email '" + email + "' found on the system."));
            } catch (Exception e) {
                UserManagerLogger.warning(this.getClass(), "No User with email '" + email + "' found on the system.");
                throw e;
            }
        }
        if (!BCrypt.checkpw(bcryptSalt + password, user.getPassword())) {
            try {
                throw new InvalidPasswordException(this.getClass(), "Password '" + password + "' does not match the correct one!");
            } catch (Exception e) {
                UserManagerLogger.debug(this.getClass(), "Password '" + password + "' does not match the correct one!");
                throw e;
            }
        }
        final UserDTO userDTO = setGrantedAuthorities(getConverter().convert(new UserConverterRequest(user)), null, null);
        UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", userDTO != null ? userDTO.getGrantedAuthorities() : "");
        return userDTO;
    }


    public UserDTO getByEmail(String email) {
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByEmail(email).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(),
                            "No User with email '" + email + "' found on the system."))));
            final UserDTO grantedUserDTO = setGrantedAuthorities(userDTO, null, null);
            UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUserDTO != null ? grantedUserDTO.getGrantedAuthorities() : "");
            return grantedUserDTO;
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with email '" + email + "' found on the system.");
            throw e;
        }
    }


    public UserDTO getByUserId(String id) {
        return getConverter().convert(new UserConverterRequest(getProvider().getById(id).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with id '" + id + "' found on the system."))));
    }


    @Override
    public Optional<UserDTO> findByUsername(String username) {
        try {
            return Optional.of(getByUsername(username));
        } catch (UserNotFoundException e) {
            UserManagerLogger.warning(this.getClass(), "No User with username '" + username + "' found on the system.");
            return Optional.empty();
        }
    }


    public UserDTO findByUUID(UUID uuid) {
        return convert(getProvider().findByUuid(uuid).orElseThrow(() ->
                new UserDoesNotExistsException(this.getClass(), "No user with uuid '" + uuid + "' found on the system.")));
    }


    @Override
    public Optional<UserDTO> findByUsername(String username, String backendServiceName) {
        if (backendServiceName == null) {
            return findByUsername(username);
        }
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
            final BackendService backendService = backendServiceProvider.findByName(backendServiceName).orElseThrow(() ->
                    new BackendServiceNotFoundException(this.getClass(), "Service with name '" + backendServiceName + "' not found."));
            final UserDTO grantedUser = setGrantedAuthorities(userDTO, null, backendService);
            UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUser != null ? grantedUser.getGrantedAuthorities() : "");
            return Optional.of(grantedUser);
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with username '" + username + "' found on the system.");
            throw e;
        }
    }


    public Optional<UserDTO> findByUsernameAndApplication(String username, String applicationName) {
        if (applicationName == null) {
            return findByUsername(username);
        }
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
            final Application application = applicationProvider.findByName(applicationName).orElseThrow(() ->
                    new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found."));
            final UserDTO grantedUser = setGrantedAuthorities(userDTO, application, null);
            UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUser != null ? grantedUser.getGrantedAuthorities() : "");
            return Optional.of(grantedUser);
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with username '" + username + "' found on the system.");
            throw e;
        }
    }


    @Override
    public Optional<UserDTO> findByEmailAddress(String email) {
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByEmail(email).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with email '" + email + "' found on the system."))));
            return Optional.of(setGrantedAuthorities(userDTO, null, null));
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with email '" + email + "' found on the system.");
            throw e;
        }
    }


    @Override
    public Optional<UserDTO> findByEmailAddress(String email, String applicationName) {
        if (applicationName == null) {
            return findByEmailAddress(email);
        }
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByEmail(email).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with email '" + email + "' found on the system."))));
            final Application application = applicationProvider.findByName(applicationName).orElseThrow(() ->
                    new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found."));
            try {
                final UserDTO grantedUserDTO = setGrantedAuthorities(userDTO, application, null);
                UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUserDTO != null ? grantedUserDTO.getGrantedAuthorities() : "");
                if (grantedUserDTO != null) {
                    return Optional.of(grantedUserDTO);
                }
                return Optional.empty();
            } catch (ApplicationNotFoundException e) {
                UserManagerLogger.warning(this.getClass(), "No application '" + applicationName + "' exists.");
                throw e;
            }
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with email '" + email + "' found on the system.");
            throw e;
        }
    }


    @Override
    public Optional<UserDTO> findByUID(String uid) {
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUuid(UUID.fromString(uid)).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with uid '" + uid + "' found on the system."))));
            final UserDTO grantedUserDTO = setGrantedAuthorities(userDTO, null, null);
            UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUserDTO != null ? grantedUserDTO.getGrantedAuthorities() : "");
            return Optional.of(grantedUserDTO);
        } catch (IllegalArgumentException e) {
            UserManagerLogger.warning(this.getClass(), "Invalid uid '" + uid + "'!.");
            throw e;
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with id '" + uid + "' found on the system.");
            throw e;
        }
    }


    @Override
    @Transactional
    public UserDTO create(UserDTO dto, String creatorName) {
        //Check username does not exist. Ignore cases.
        if (getProvider().findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(this.getClass(), "Username '" + dto.getUsername() + "' already exists!");
        }
        if (getProvider().findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(this.getClass(), "Email '" + dto.getEmail() + "' already exists!");
        }
        if (dto.getExternalReference() != null && getProvider().findByExternalReference(dto.getExternalReference()).isPresent()) {
            throw new ExternalReferenceAlreadyExistsException(this.getClass(),
                    "External reference '" + dto.getExternalReference() + "' already in use!");
        }
        //If username is not set, is the email.
        if ((dto.getUsername() == null || dto.getUsername().isBlank()) && (dto.getEmail() != null && !dto.getEmail().isBlank())) {
            dto.setUsername(dto.getEmail());
        }
        final UserDTO userDTO = super.create(dto, creatorName);
        if (userDTO != null) {
            final User user = reverse(userDTO);
            userGroupUserProvider.assignToDefaultGroup(user);
            try {
                emailService.sendUserCreationEmail(user);
            } catch (EmailNotSentException | InvalidEmailAddressException | FileNotFoundException e) {
                UserManagerLogger.severe(this.getClass(), e.getMessage());
            }
        }
        return userDTO;
    }


    public IAuthenticatedUser createPublicUser(CreateUserRequest createUserRequest, String createdBy) {
        if (allowPublicRegistration) {
            if (findByUsername(createUserRequest.getUsername()).isPresent()) {
                UserManagerLogger.warning(this.getClass(), "Username '" + createUserRequest.getUsername() + "' already exists!.");
                throw new UserAlreadyExistsException(this.getClass(), "Username exists!");
            }
            final UserDTO userDTO = create(createUserRequest, createUserRequest.getUsername());
            userDTO.setAccountExpirationTime(LocalDateTime.now().plusHours(publicUserExpirationHours));
            getProvider().save(reverse(userDTO));
            userGroupUserProvider.assignToDefaultGroup(reverse(userDTO));
            //Assign to organization and team if needed.
            try {
                if (createUserRequest.getOrganization() != null && !createUserRequest.getOrganization().isBlank()) {
                    final Organization organization = organizationProvider.findByName(createUserRequest.getOrganization()).orElseThrow(() ->
                            new OrganizationDoesNotExistException("No organization exists with name '" + createUserRequest.getOrganization() + "'."));
                    final Team team;
                    if (createUserRequest.getTeam() != null && !createUserRequest.getTeam().isBlank()) {
                        team = teamProvider.findByNameAndOrganization(createUserRequest.getTeam(), organization).orElseThrow(() ->
                                new TeamNotFoundException(this.getClass(), "No team exists with name '" + createUserRequest.getTeam() + "'."));
                    } else {
                        //No team selected, assign to default.
                        final List<Team> teamsFromOrganization = teamProvider.findByOrganization(organization);
                        if (teamsFromOrganization.size() != 1) {
                            throw new TeamNotFoundException(this.getClass(), "No default team exists for organization '"
                                    + createUserRequest.getOrganization() + "'. "
                                    + "Expecting only one group to be present on the organization to be selected as default one.");
                        }
                        UserManagerLogger.debug(this.getClass(), "Using default team '" + createUserRequest.getOrganization() + "'.");
                        team = teamsFromOrganization.get(0);
                    }
                    //Assign user to team.
                    teamMemberProvider.assign(userDTO.getId(), team.getId(), team.getOrganization().getId(), createdBy);
                    UserManagerLogger.info(this.getClass(), "Assigning user '{}' to team '{}'.", userDTO.getUsername(), team.getName());
                } else {
                    UserManagerLogger.debug(this.getClass(), "No organization provided meanwhile creating a user.");
                }
            } catch (Exception e) {
                UserManagerLogger.warning(this.getClass(), "User '" + createUserRequest.getUsername() + "' cannot be assigned to an organization '"
                        + createUserRequest.getOrganization() + "'. Error message is: " + e.getMessage());
            }
            return userDTO;
        } else {
            throw new ActionNotAllowedException(this.getClass(), "Feature disabled.");
        }
    }


    @Override
    public UserDTO create(CreateUserRequest createUserRequest, String createdBy) {
        final UserDTO authenticatedUser = createUser(createUserRequest.getUsername(), createUserRequest.getUniqueId(),
                createUserRequest.getEmail(), createUserRequest.getFirstname(), createUserRequest.getLastname(), createUserRequest.getPassword(),
                createUserRequest.getExternalReference(), createdBy);
        //Set roles if is the first user on a database.
        if (createUserRequest.getAuthorities() != null) {
            createUserRequest.getAuthorities().forEach(authority -> roleProvider
                    .createDefaultRoleAdmin(authenticatedUser.getId(), authority.replaceAll(ROLE_PREFIX, "")));
        } else {
            //Set default role group
            userGroupUserProvider.assignToDefaultGroup(reverse(authenticatedUser));
        }
        //Assign 3rd party reference.
        return authenticatedUser;
    }

    public UserDTO createUser(String username, String uniqueId, String name, String lastName, String password, String externalReference,
                              String createdBy) {
        return createUser(username, uniqueId, null, name, lastName, password, externalReference, createdBy);
    }

    public UserDTO createUser(String username, String uniqueId, String email, String name, String lastName, String password,
                              String externalReference, String createdBy) {
        if (findByUsername(username).isPresent()) {
            UserManagerLogger.warning(this.getClass(), "Username '" + username + "' already exists!.");
            throw new UserAlreadyExistsException(this.getClass(), "Username exists!");
        }
        final UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setUUID(UUID.randomUUID());
        userDTO.setFirstName(name != null ? name : "");
        userDTO.setLastname(lastName != null ? lastName : "");
        userDTO.setIdCard(uniqueId);
        userDTO.setPassword(password);
        userDTO.setCreatedBy(createdBy);
        userDTO.setEmail(Objects.requireNonNullElseGet(email, () -> "email" + getProvider().count() + "@email.com"));
        userDTO.setExternalReference(externalReference);
        //Check the email account.
        final Optional<User> existingUserByEmail = getProvider().findByEmail(userDTO.getEmail());
        if (existingUserByEmail.isPresent()
                && !Objects.equals(userDTO.getUUID(), existingUserByEmail.get().getUuid())) {
            throw new EmailAlreadyExistsException(this.getClass(), "Email '" + userDTO.getEmail() + "' already exists!");
        }
        final User user = getProvider().save(getConverter().reverse(userDTO));
        UserManagerLogger.info(this.getClass(), "User '" + username + "' created on the system.");
        try {
            emailService.sendUserCreationEmail(user);
        } catch (EmailNotSentException | InvalidEmailAddressException | FileNotFoundException e) {
            UserManagerLogger.severe(this.getClass(), e.getMessage());
        }
        return getConverter().convert(new UserConverterRequest(user));
    }

    public void updatePassword(String token, String newPassword) {
        final PasswordResetToken passwordResetToken = checkToken(token);
        UserManagerLogger.info(this.getClass(), "Updating password for user '{}'.", passwordResetToken.getUser().getUsername());
        updatePassword(passwordResetToken.getUser().getUsername(), newPassword, passwordResetToken.getUser().getUsername());
    }


    @Override
    public UserDTO updatePassword(String username, String oldPassword, String newPassword, String updatedBy) {
        final User user = checkPassword(username, oldPassword);

        user.setPassword(bcryptSalt + newPassword);
        user.setUpdatedBy(updatedBy);
        UserManagerLogger.info(this.getClass(), "Password updated!.");
        return getConverter().convert(new UserConverterRequest(getProvider().save(user)));
    }

    /**
     * Updates a user password, but does not check the old password. Only for using by Admins or first log in.
     *
     * @param username    user to be changed
     * @param newPassword the new password
     * @param updatedBy   the admin who is updating it.
     * @return the updated user.
     */
    public IAuthenticatedUser updatePassword(String username, String newPassword, String updatedBy) {
        final User user = getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
        final UserDTO updater = getByUsername(updatedBy);
        final UserDTO userDTO = setGrantedAuthorities(convert(user), null, null);
        if (updater != null && userDTO != null && userDTO.getGrantedAuthorities().contains(ADMIN_AUTHORITY)
                && !updater.getGrantedAuthorities().contains(ADMIN_AUTHORITY)) {
            throw new ActionNotAllowedException(this.getClass(), "You cannot change another admin's password.");
        }


        user.setPassword(bcryptSalt + newPassword);
        user.setUpdatedBy(updatedBy);
        UserManagerLogger.info(this.getClass(), "Password updated!.");
        return getConverter().convert(new UserConverterRequest(getProvider().save(user)));
    }


    public User checkPassword(String username, String password) {
        final User user = getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
        //Check the password.
        if (password != null && !BCrypt.checkpw(bcryptSalt + password, user.getPassword())) {
            UserManagerLogger.warning(this.getClass(), "Provided password is incorrect!.");
            throw new InvalidParameterException(this.getClass(), "Provided password is incorrect!");
        }
        return user;
    }


    public String getPassword(String username) {
        final User user = getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
        return user.getPassword();
    }


    /**
     * Gets the password using a user UID.
     *
     * @param uid is the database id.
     * @return the password.
     */
    public String getPasswordByUid(String uid) {
        if (uid == null) {
            return null;
        }
        final User user = getProvider().findByUuid(UUID.fromString(uid)).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with id '" + uid + "' found on the system."));
        return user.getPassword();
    }


    /**
     * Restricted service that only update some fields from the user.
     *
     * @param userDTO   data to be updated.
     * @param updatedBy who has asked by this service.
     * @return the new updated user.
     */
    public UserDTO updateOwnUser(UserDTO userDTO, String updatedBy) {
        userDTO.setUsername(updatedBy);
        final User user = getProvider().findByUsername(updatedBy).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + updatedBy + "' found on the system."));
        user.setUpdatedBy(updatedBy);
        user.setIdCard(userDTO.getIdCard());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setPostalCode(userDTO.getPostalCode());
        user.setCity(userDTO.getCity());
        user.setCountry(userDTO.getCountry());
        if (userDTO.getLocale() != null) {
            user.setLocale(userDTO.getLocale());
        }
        return getConverter().convert(new UserConverterRequest(getProvider().update(user)));
    }


    @Override
    public UserDTO updateUser(CreateUserRequest createUserRequest, String updatedBy) {
        final User user = getProvider().findByUsername(createUserRequest.getUsername()).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + createUserRequest.getUsername() + "' found on the system."));

        user.setUsername(createUserRequest.getUsername());
        user.setName(createUserRequest.getFirstname());
        user.setLastname(createUserRequest.getLastname());
        user.setIdCard(createUserRequest.getUniqueId());
        user.setUpdatedBy(updatedBy);
        UserManagerLogger.info(this.getClass(), "User '" + createUserRequest.getUsername() + "' updated!.");
        return getConverter().convert(new UserConverterRequest(getProvider().update(user)));
    }


    public List<UserDTO> getByAccountBlocked(boolean accountBlocked) {
        return getProvider().findAllByAccountBlocked(accountBlocked).parallelStream().map(this::createConverterRequest).map(getConverter()::convert)
                .collect(Collectors.toList());
    }


    @Override
    public Collection<UserDTO> findAll() {
        return getProvider().findAll().parallelStream().map(this::createConverterRequest).map(getConverter()::convert).collect(Collectors.toList());
    }

    @Override
    public Collection<UserDTO> findAll(int page, int size) {
        return getProvider().findAll(page, size).parallelStream().map(this::createConverterRequest).map(getConverter()::convert).collect(Collectors.toList());
    }


    @Transactional
    @Override
    public boolean deleteUser(String deletedBy, String username) {
        try {
            delete(username, deletedBy);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Transactional
    @Override
    public boolean delete(IAuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            return false;
        }
        return deleteUser(null, authenticatedUser.getUsername());
    }


    @Override
    public Set<String> getRoles(String username, String applicationName) {
        final UserDTO userDTO = getByUsername(username);
        final Application application = applicationProvider.findByName(applicationName).orElseThrow(() ->
                new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found."));
        final UserDTO userWithRoles = setGrantedAuthorities(userDTO, application, null);
        if (userWithRoles != null) {
            final Set<String> roles = userWithRoles.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            UserManagerLogger.debug(this.getClass(), "Obtained roles '" + roles
                    + "' from '" + username + "'.");
            return roles;
        }
        return new HashSet<>();
    }


    @Override
    @Transactional
    public void delete(UserDTO entity, String deletedBy) {
        if (deletedBy != null && Objects.equals(entity.getUsername(), deletedBy)) {
            throw new InvalidParameterException(this.getClass(), "You cannot delete your own user.");
        }
        if (entity == null) {
            return;
        }
        final UserDTO deleter = getByUsername(deletedBy);
        setGrantedAuthorities(entity, null, null);
        if (deleter != null && entity.getGrantedAuthorities() != null
                && entity.getGrantedAuthorities().contains(ADMIN_AUTHORITY) && !deleter.getGrantedAuthorities().contains(ADMIN_AUTHORITY)) {
            throw new ActionNotAllowedException(this.getClass(), "You cannot delete an admin user.");
        }
        super.delete(entity, deletedBy);
    }


    @Override
    @Transactional
    public UserDTO update(UserDTO dto, String updaterName) {
        final User user = getProvider().findByUuid(dto.getUUID()).orElse(null);
        final String oldEmail = (user != null) ? user.getEmail() : null;

        final Optional<User> existingUserByUsername = getProvider().findByUsername(dto.getUsername());
        if (existingUserByUsername.isPresent() && !Objects.equals(dto.getUUID(), existingUserByUsername.get().getUuid())) {
            throw new UserAlreadyExistsException(this.getClass(), "Username '" + dto.getUsername() + "' already exists!");
        }

        if (dto.getExternalReference() != null) {
            final Optional<User> userWithReference = getProvider().findByExternalReference(dto.getExternalReference());
            if (userWithReference.isPresent() && Objects.equals(userWithReference.get().getUuid(), dto.getUUID())) {
                throw new ExternalReferenceAlreadyExistsException(this.getClass(),
                        "External reference '" + dto.getExternalReference() + "' already in use!");
            }
        }

        final Optional<User> existingUserByEmail = getProvider().findByEmail(dto.getEmail());
        if (existingUserByEmail.isPresent()
                && !Objects.equals(dto.getUUID(), existingUserByEmail.get().getUuid())) {
            throw new EmailAlreadyExistsException(this.getClass(), "Email '" + dto.getEmail() + "' already exists!");
        }

        try {
            return super.update(dto, updaterName);
        } finally {
            //Send an email if the email account has been updated.
            if (oldEmail != null && !Objects.equals(oldEmail, dto.getEmail())) {
                try {
                    UserManagerLogger.warning(this.getClass(), "User's mail has been changed from '{}' to '{}'.",
                            oldEmail, dto.getEmail());
                    emailService.sendUserUpdateEmail(user, oldEmail);
                } catch (EmailNotSentException | InvalidEmailAddressException | FileNotFoundException e) {
                    UserManagerLogger.severe(this.getClass(), e.getMessage());
                }
            }
        }
    }


    @Transactional
    public void delete(String username, String deletedBy) {
        //You cannot delete an admin user if you are not admin.
        final UserDTO userToBeDeleted = getByUsername(username);
        if (userToBeDeleted == null) {
            throw new UserNotFoundException(this.getClass(), "No uses exists with username '" + username + "'.");
        }
        delete(userToBeDeleted, deletedBy);
    }


    /**
     * Populate the authorities for a user. If a group is selected, only the one of the group. If not the roles that are not at group level.
     *
     * @param userDTO The user to populate.
     * @return the populated user.
     */
    private UserDTO setGrantedAuthorities(UserDTO userDTO, Application application, BackendService backendService) {
        if (userDTO != null) {
            final Set<UserApplicationBackendServiceRole> userApplicationBackendServiceRoles =
                    userApplicationBackendServiceRoleProvider.findByUserId(userDTO.getId());

            userApplicationBackendServiceRoles.forEach(userApplicationBackendServiceRole -> {
                if ((application == null
                        || application.getName().equalsIgnoreCase(userApplicationBackendServiceRole.getId().getApplicationName()))
                        && (backendService == null
                        || backendService.getName().equalsIgnoreCase(userApplicationBackendServiceRole.getId().getBackendServiceName()))) {
                    userDTO.addApplicationServiceRoles(RoleNameGenerator.createApplicationRoleName(userApplicationBackendServiceRole));
                    userDTO.addGrantedAuthorities(RoleNameGenerator.createBackendRoleName(userApplicationBackendServiceRole));
                }
            });

            UserManagerLogger.debug(this.getClass(), "Assigning application roles '{}' to '{}'.", userDTO.getApplicationRoles(), userDTO.getUsername());
            UserManagerLogger.debug(this.getClass(), "Assigning backend roles '{}' to '{}'.", userDTO.getGrantedAuthorities(), userDTO.getUsername());
        }
        return setGrantedAuthoritiesByGroups(userDTO, application, backendService);
    }


    private UserDTO setGrantedAuthoritiesByGroups(UserDTO userDTO, Application application, BackendService backendService) {
        if (userDTO == null) {
            return null;
        }
        final List<UserGroup> userGroups = userGroupProvider.getByUser(userDTO.getId());
        return setGrantedAuthorities(userDTO, userGroups, application, backendService);
    }


    private UserDTO setGrantedAuthorities(UserDTO userDTO, Collection<UserGroup> userGroups, Application application, BackendService backendService) {
        if (userDTO != null && userGroups != null) {

            final Set<UserGroupApplicationBackendServiceRole> userGroupApplicationBackendServiceRoles =
                    userGroupApplicationBackendServiceRoleProvider.findByUserGroupIdIn(userGroups.stream().map(UserGroup::getId).toList());

            userGroupApplicationBackendServiceRoles.forEach(userApplicationBackendServiceRole -> {
                if ((application == null
                        || application.getName().equalsIgnoreCase(userApplicationBackendServiceRole.getId().getApplicationName()))
                        && (backendService == null
                        || backendService.getName().equalsIgnoreCase(userApplicationBackendServiceRole.getId().getBackendServiceName()))) {
                    userDTO.addApplicationServiceRoles(RoleNameGenerator.createApplicationRoleName(userApplicationBackendServiceRole));
                    userDTO.addGrantedAuthorities(RoleNameGenerator.createBackendRoleName(userApplicationBackendServiceRole));
                }
            });
            UserManagerLogger.debug(this.getClass(), "Assigning application roles '{}' to '{}' from groups '{}'.",
                    userDTO.getApplicationRoles(), userDTO.getUsername(), userGroups);
            UserManagerLogger.debug(this.getClass(), "Assigning backend roles '{}' to '{}' from groups '{}'.",
                    userDTO.getGrantedAuthorities(), userDTO.getUsername(), userGroups);
        }
        return userDTO;
    }


    public void checkUsernameExists(String username) {
        if (getProvider().findByUsername(username).isEmpty()) {
            throw new UserNotFoundException(this.getClass(), "No uses exists with username '" + username + "'.");
        }
    }


    public void setApplicationBackendServiceRole(UserDTO
                                                         userDTO, List<ApplicationBackendServiceRole> applicationBackendServiceRoles) {
        final User user = reverse(userDTO);
        user.setApplicationBackendServiceRoles(new HashSet<>(applicationBackendServiceRoles));
        //Do not update password.
        user.setPassword(null);
        getProvider().save(user);
    }


    public void assign(UserDTO userDTO, List<ApplicationBackendServiceRole> applicationBackendServiceRoles) {
        applicationBackendServiceRoles.forEach(applicationBackendServiceRole -> {
            try {
                assign(userDTO.getUsername(),
                        applicationBackendServiceRole.getId().getApplicationRole().getId().getApplication().getName(),
                        applicationBackendServiceRole.getId().getApplicationRole().getId().getRole().getName(),
                        applicationBackendServiceRole.getId().getBackendServiceRole().getId().getBackendService().getName(),
                        applicationBackendServiceRole.getId().getBackendServiceRole().getId().getName());
            } catch (InvalidParameterException e) {
                UserManagerLogger.warning(this.getClass(), "Trying to assign an existing role '"
                        + applicationBackendServiceRole + "' to user '" + userDTO + "'.");
            }
        });
    }


    public UserDTO assign(
            String username, String applicationName, String applicationRoleName) {

        final User user = getProvider().findByUsername(username).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with username '" + username + "'."));

        final List<ApplicationBackendServiceRole> availableRoles = applicationBackendServiceRoleProvider
                .findByApplicationNameAndApplicationRole(applicationName, applicationRoleName);

        //Add any missing permission.
        final List<UserApplicationBackendServiceRole> alreadyAssignedPermissions = userApplicationBackendServiceRoleProvider
                .findBy(user.getId(), applicationName, applicationRoleName);

        final Set<UserApplicationBackendServiceRole> rolesToAdd = new HashSet<>();
        availableRoles.forEach(applicationBackendServiceRole -> {

            //Collection of availableRoles launch a lazy, as these ids are not retrieved in a collection.
            final ApplicationBackendServiceRole applicationBackendServiceRole1 =
                    applicationBackendServiceRoleProvider.findById(applicationBackendServiceRole.getId())
                            .orElseThrow(() -> new ApplicationRoleNotFoundException(this.getClass(),
                                    "No application role found with id '" + applicationBackendServiceRole.getId().getApplicationRole().getId() + "'."));
            final BackendServiceRole backendServiceRole =
                    backendServiceRoleProvider.findById(applicationBackendServiceRole1.getId().getBackendServiceRole().getId())
                            .orElseThrow(() -> new BackendServiceRoleNotFoundException(this.getClass(),
                                    "No backend service role found with id '" + applicationBackendServiceRole.getId().getBackendServiceRole().getId() + "'."));

            final UserApplicationBackendServiceRole userApplicationBackendServiceRole = new UserApplicationBackendServiceRole(
                    user.getId(),
                    applicationName,
                    applicationRoleName,
                    backendServiceRole.getId().getBackendService().getId(),
                    backendServiceRole.getId().getName()
            );
            if (!alreadyAssignedPermissions.contains(userApplicationBackendServiceRole)) {
                rolesToAdd.add(userApplicationBackendServiceRole);
            }
        });
        if (rolesToAdd.isEmpty()) {
            throw new RoleWithoutBackendServiceRoleException(this.getClass(), "Role '" + applicationRoleName + "' in application '" + applicationName
                    + "' is not linked to any backend service. Please link it to one or more backend serviced.");
        }
        userApplicationBackendServiceRoleProvider.saveAll(rolesToAdd);
        return setGrantedAuthorities(convert(user), null, null);
    }


    public UserDTO unAssign(
            String username, String applicationName, String applicationRoleName, String unassignedBy) {

        final User user = getProvider().findByUsername(username).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with username '" + username + "'."));

        //Add any missing permission.
        final List<UserApplicationBackendServiceRole> assignedPermissions = userApplicationBackendServiceRoleProvider
                .findBy(user.getId(), applicationName, applicationRoleName);

        //Send events.
        getEventSender().sendEvents(convert(user), EventSubject.UPDATED, UserEventSender.REVOCATION_EVENT_TAG, unassignedBy);


        userApplicationBackendServiceRoleProvider.deleteAll(assignedPermissions);
        return setGrantedAuthorities(convert(user), null, null);
    }


    public UserDTO assign(
            String username, String applicationName, String applicationRoleName, String backendServiceName, String backendServiceRoleName) {
        final User user = getProvider().findByUsername(username).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with username '" + username + "'."));

        //Ensure it does not exist yet.
        if (userApplicationBackendServiceRoleProvider
                .findBy(user.getId(), applicationName, applicationRoleName, backendServiceName, backendServiceRoleName).isPresent()) {
            throw new InvalidParameterException(this.getClass(), "User '" + username + "' already has role '" + applicationRoleName + "' for application '"
                    + applicationName + "' and service '" + backendServiceName + "' with role '" + backendServiceRoleName + "'.");
        }

        final Optional<ApplicationBackendServiceRole> optionalApplicationBackendServiceRole = applicationBackendServiceRoleProvider
                .findByApplicationRoleAndServiceRole(applicationName, applicationRoleName, backendServiceName, backendServiceRoleName);

        //Create it, if it does not exist.
        if (optionalApplicationBackendServiceRole.isEmpty()) {
            final ApplicationRole applicationRole = applicationRoleProvider
                    .findByApplicationIdAndRoleId(applicationName, applicationRoleName).orElseThrow(() ->
                            new ApplicationRoleNotFoundException(this.getClass(), "No application role defined for application '"
                                    + applicationName + "' and role '" + applicationRoleName + "'"));

            final BackendServiceRole backendServiceRole = backendServiceRoleProvider
                    .findByBackendServiceAndName(backendServiceName, backendServiceRoleName).orElseThrow(() ->
                            new BackendServiceRoleNotFoundException(this.getClass(), "No backend service role defined for backend '"
                                    + backendServiceName + "' and role '" + backendServiceRoleName + "'."));

            applicationBackendServiceRoleProvider.save(new ApplicationBackendServiceRole(applicationRole, backendServiceRole));
        }

        //Add directly to the join column.
        userApplicationBackendServiceRoleProvider.save(
                new UserApplicationBackendServiceRole(user.getId(), applicationName, applicationRoleName,
                        backendServiceName, backendServiceRoleName));

        //Load again all roles.
        return setGrantedAuthorities(convert(user), null, null);
    }


    public List<UserDTO> getByUserGroup(String userGroupName) {
        final UserGroup userGroup = userGroupProvider.findByName(userGroupName).orElseThrow(()
                -> new UserGroupNotFoundException(this.getClass(), "No UserGroup exists with name '" + userGroupName + "'."));
        return getByUserGroup(userGroup);
    }


    public List<UserDTO> getByUserGroup(Long userGroupId) {
        final UserGroup userGroup = userGroupProvider.findById(userGroupId).orElseThrow(()
                -> new UserGroupNotFoundException(this.getClass(), "No UserGroup exists with id '" + userGroupId + "'."));
        return getByUserGroup(userGroup);
    }


    public List<UserDTO> getByUserGroup(UserGroup userGroup) {
        final Set<Long> userIds = new HashSet<>();
        final Set<UserGroupUser> userGroupUsers = userGroupUserProvider.findByIdUserGroupId(userGroup.getId());
        userGroupUsers.forEach(user -> userIds.add(user.getId().getUserId()));
        return convertAll(getProvider().findByIdIn(userIds));
    }


    public void resetPassword(String email) throws EmailNotSentException {
        final User user = getProvider().findByEmail(email).orElseThrow(()
                -> new EmailNotFoundException(this.getClass(), "No user exists with the email '" + email + "'."));

        //Send an email with the token in a link!
        try {
            emailService.sendPasswordRecoveryEmail(user);
        } catch (FileNotFoundException e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
            throw new InvalidParameterException(this.getClass(), "Cannot sent confirmation email!");
        } catch (InvalidEmailAddressException e) {
            UserManagerLogger.errorMessage(this.getClass(), e);
            //Email must be already validated.
            throw new EmailNotSentException(this.getClass(), "Invalid email address", e);
        }
    }


    public PasswordResetToken checkToken(String token) {
        final PasswordResetToken passwordResetToken = passwordResetTokenProvider.findByToken(token).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No user exists with the provided token."));

        if (passwordResetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(this.getClass(), "Token has expired!");
        }
        return passwordResetToken;
    }

    public List<UserDTO> getByTeam(Long teamId) {
        return getByTeam(teamId, 0, DEFAULT_PAGE_SIZE);
    }


    public List<UserDTO> getByTeam(Long teamId, int page, int size) {
        if (teamProvider.findById(teamId).isEmpty()) {
            throw new TeamNotFoundException(this.getClass(), "No Team exists with id '" + teamId + "'.");
        }

        final Set<Long> userIds = new HashSet<>();
        final List<TeamMember> members = teamMemberProvider.findByIdUserGroupId(teamId, page, size);
        members.forEach(member -> userIds.add(member.getId().getUserId()));
        return convertAll(getProvider().findByIdIn(userIds));
    }


    public long countByTeam(String organizationName, String teamName) {
        final Optional<Organization> organization = organizationProvider.findByName(organizationName);
        if (organization.isEmpty()) {
            throw new OrganizationNotFoundException(this.getClass(), "No Organization exists with name '" + organizationName + "'.");
        }
        final Optional<Team> team = teamProvider.findByNameAndOrganization(teamName, organization.get());
        if (team.isEmpty()) {
            throw new TeamNotFoundException(this.getClass(), "No Team exists with name '" + teamName + "' at organization '" + organizationName + "'.");
        }
        return countByTeam(team.get().getId());
    }


    public long countByTeam(Long teamId) {
        return teamMemberProvider.countByIdUserGroupId(teamId);
    }


    public List<UserDTO> getByTeam(String organizationName, String teamName) {
        return getByTeam(organizationName, teamName, 0, DEFAULT_PAGE_SIZE);
    }


    public List<UserDTO> getByTeam(String organizationName, String teamName, int page, int size) {
        final Optional<Organization> organization = organizationProvider.findByName(organizationName);
        if (organization.isEmpty()) {
            throw new OrganizationNotFoundException(this.getClass(), "No Organization exists with name '" + organizationName + "'.");
        }
        final Optional<Team> team = teamProvider.findByNameAndOrganization(teamName, organization.get());
        if (team.isEmpty()) {
            throw new TeamNotFoundException(this.getClass(), "No Team exists with name '" + teamName + "' at organization '" + organizationName + "'.");
        }

        final Set<Long> userIds = new HashSet<>();
        final List<TeamMember> members = teamMemberProvider.findByIdUserGroupId(team.get().getId(), page, size);
        members.forEach(member -> userIds.add(member.getId().getUserId()));
        return convertAll(getProvider().findByIdIn(userIds));
    }


    public List<UserDTO> getByOrganization(String organizationName) {
        if (organizationProvider.findByName(organizationName).isEmpty()) {
            throw new OrganizationNotFoundException(this.getClass(), "No Organization exists with name '" + organizationName + "'.");
        }

        final Set<Long> userIds = new HashSet<>();
        final Set<TeamMember> members = teamMemberProvider.findByOrganizationName(organizationName);
        members.forEach(member -> userIds.add(member.getId().getUserId()));
        return convertAll(getProvider().findByIdIn(userIds));
    }


    public long countByOrganization(String organizationName) {
        if (organizationProvider.findByName(organizationName).isEmpty()) {
            throw new OrganizationNotFoundException(this.getClass(), "No Organization exists with name '" + organizationName + "'.");
        }
        return teamMemberProvider.countByOrganization(organizationName);
    }


    public List<UserDTO> findByUIDs(Collection<UUID> uuids) {
        return convertAll(getProvider().findByUuids(uuids));
    }

    public List<UserDTO> findByUIDsInUserOrganizations(Collection<UUID> uuids, String username) {
        final Collection<? extends IUserOrganization> organizations = userOrganizationProvider.get(0).findByUsername(username);
        return convertAll(getProvider().findByUuids(uuids, organizations.stream().map(IUserOrganization::getName).toList()));
    }


    @Override
    public Optional<UserDTO> findByExternalReference(String externalReference) {
        final Optional<User> user = getProvider().findByExternalReference(externalReference);
        return user.map(this::convert);
    }

    public UserDTO getByExternalReference(String externalReference) {
        return convert(getProvider().findByExternalReference(externalReference).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No user with reference '" + externalReference + "' exists.")));
    }

    public List<UserDTO> getByExternalReference(List<String> externalReference) {
        return convertAll(getProvider().findByExternalReferences(externalReference));
    }


    /**
     * Users can be in more than one organization.
     * Retrieves all users that are on a team from an organization of the user. Ignores CreatedOn property.
     *
     * @param page      Starting page.
     * @param size      Size of page.
     * @param requester the organization admin that searches the users.
     * @return a list of users that can pertain to any organization from the organization admin.
     */
    @Override
    public List<UserDTO> getByUserOrganization(int page, int size, String requester) {
        final Collection<? extends IUserOrganization> organizations = userOrganizationProvider.get(0).findByUsername(requester);
        final Set<Long> userIds = new HashSet<>();
        final List<TeamMember> members = teamMemberProvider.findByOrganizationNameIn(organizations.stream().map(IUserOrganization::getName).toList(),
                page, size);
        members.forEach(member -> userIds.add(member.getId().getUserId()));
        final Set<UserDTO> users = new HashSet<>();
        users.addAll(convertAll(getProvider().findByIdIn(userIds)));

        //Get users created by org admin, but are not assigned to a team.
        users.addAll(super.getByUserOrganization(page, size, requester));

        return new ArrayList<>(users);
    }
}

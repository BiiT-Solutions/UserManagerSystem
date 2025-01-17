package com.biit.usermanager.core.controller;

import com.biit.kafka.controllers.KafkaElementController;
import com.biit.kafka.events.EventSubject;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.server.security.exceptions.ActionNotAllowedException;
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
import com.biit.usermanager.core.exceptions.RoleWithoutBackendServiceRoleException;
import com.biit.usermanager.core.exceptions.TeamNotFoundException;
import com.biit.usermanager.core.exceptions.TokenExpiredException;
import com.biit.usermanager.core.exceptions.UserAlreadyExistsException;
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
import org.apache.kafka.common.errors.InvalidRequestException;
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

@Controller
@Order(1)
@Qualifier("userController")
public class UserController extends KafkaElementController<User, Long, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter> implements IAuthenticatedUserProvider {

    public static final String ROLE_PREFIX = "ROLE_";

    @Value("${bcrypt.salt:}")
    private String bcryptSalt;

    @Value("#{new Boolean('${mail.updated.warning.email:false}')}")
    private boolean sendEmailOnUpdate = false;

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
                             EmailService emailService, RoleProvider roleProvider) {
        super(provider, converter, userEventSender);
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
    }


    @Override
    protected UserConverterRequest createConverterRequest(User entity) {
        return new UserConverterRequest(entity);
    }

    public UserDTO getByUsername(String username) {
        final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No user with username '" + username + "' found on the system."))));
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
        UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", userDTO.getGrantedAuthorities());
        return userDTO;
    }


    public UserDTO getByEmail(String email) {
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByEmail(email).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(),
                            "No User with email '" + email + "' found on the system."))));
            final UserDTO grantedUserDTO = setGrantedAuthorities(userDTO, null, null);
            UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUserDTO.getGrantedAuthorities());
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
    public Optional<IAuthenticatedUser> findByUsername(String username) {
        try {
            return Optional.of(getByUsername(username));
        } catch (UserNotFoundException e) {
            UserManagerLogger.warning(this.getClass(), "No User with username '" + username + "' found on the system.");
            return Optional.empty();
        }
    }


    @Override
    public Optional<IAuthenticatedUser> findByUsername(String username, String backendServiceName) {
        if (backendServiceName == null) {
            return findByUsername(username);
        }
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
            final BackendService backendService = backendServiceProvider.findByName(backendServiceName).orElseThrow(() ->
                    new BackendServiceNotFoundException(this.getClass(), "Service with name '" + backendServiceName + "' not found."));
            final UserDTO grantedUser = setGrantedAuthorities(userDTO, null, backendService);
            UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUser.getGrantedAuthorities());
            return Optional.of(grantedUser);
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with username '" + username + "' found on the system.");
            throw e;
        }
    }


    public Optional<IAuthenticatedUser> findByUsernameAndApplication(String username, String applicationName) {
        if (applicationName == null) {
            return findByUsername(username);
        }
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
            final Application application = applicationProvider.findByName(applicationName).orElseThrow(() ->
                    new ApplicationNotFoundException(this.getClass(), "Application with name '" + applicationName + "' not found."));
            final UserDTO grantedUser = setGrantedAuthorities(userDTO, application, null);
            UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUser.getGrantedAuthorities());
            return Optional.of(grantedUser);
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with username '" + username + "' found on the system.");
            throw e;
        }
    }


    @Override
    public Optional<IAuthenticatedUser> findByEmailAddress(String email) {
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
    public Optional<IAuthenticatedUser> findByEmailAddress(String email, String applicationName) {
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
                UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUserDTO.getGrantedAuthorities());
                return Optional.of(grantedUserDTO);
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
    public Optional<IAuthenticatedUser> findByUID(String uid) {
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUuid(UUID.fromString(uid)).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with uid '" + uid + "' found on the system."))));
            final UserDTO grantedUserDTO = setGrantedAuthorities(userDTO, null, null);
            UserManagerLogger.debug(this.getClass(), "Granted authorities are '{}'.", grantedUserDTO.getGrantedAuthorities());
            return Optional.of(grantedUserDTO);
        } catch (IllegalArgumentException e) {
            UserManagerLogger.warning(this.getClass(), "Invalid uid '" + uid + "'!.");
            throw e;
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with id '" + uid + "' found on the system.");
            throw e;
        }
    }


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
        final UserDTO userDTO = super.create(dto, creatorName);
        if (userDTO != null) {
            try {
                final User user = reverse(userDTO);
                emailService.sendUserCreationEmail(user);
                userGroupUserProvider.assignToDefaultGroup(user);
            } catch (EmailNotSentException | InvalidEmailAddressException | FileNotFoundException e) {
                UserManagerLogger.severe(this.getClass(), e.getMessage());
            }
        }
        return userDTO;
    }


    public IAuthenticatedUser createPublicUser(CreateUserRequest createUserRequest) {
        if (allowPublicRegistration) {
            if (findByUsername(createUserRequest.getUsername()).isPresent()) {
                UserManagerLogger.warning(this.getClass(), "Username '" + createUserRequest.getUsername() + "' already exists!.");
                throw new UserAlreadyExistsException(this.getClass(), "Username exists!");
            }
            final UserDTO userDTO = (UserDTO) create(createUserRequest, createUserRequest.getUsername());
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
                        team = teamsFromOrganization.get(0);
                    }
                    //Assign user to team.
                    teamMemberProvider.assign(userDTO.getId(), team.getId());
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
    public IAuthenticatedUser create(CreateUserRequest createUserRequest, String createdBy) {
        final UserDTO authenticatedUser = (UserDTO) createUser(createUserRequest.getUsername(), createUserRequest.getUniqueId(),
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

    public IAuthenticatedUser createUser(String username, String uniqueId, String name, String lastName, String password, String externalReference,
                                         String createdBy) {
        return createUser(username, uniqueId, null, name, lastName, password, externalReference, createdBy);
    }

    public IAuthenticatedUser createUser(String username, String uniqueId, String email, String name, String lastName, String password,
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
    public IAuthenticatedUser updatePassword(String username, String oldPassword, String newPassword, String updatedBy) {
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


    @Override
    public IAuthenticatedUser updateUser(CreateUserRequest createUserRequest, String updatedBy) {
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
    public Collection<IAuthenticatedUser> findAll() {
        return getProvider().findAll().parallelStream().map(this::createConverterRequest).map(getConverter()::convert).collect(Collectors.toList());
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
        if (Objects.equals(entity.getUsername(), deletedBy)) {
            throw new InvalidParameterException(this.getClass(), "You cannot delete your own user.");
        }
        super.delete(entity, deletedBy);
    }

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
            if (sendEmailOnUpdate && oldEmail != null && !Objects.equals(oldEmail, dto.getEmail())) {
                try {
                    UserManagerLogger.warning(this.getClass(), "User's mail has been changed from '{}' to '{}'.",
                            oldEmail, dto.getEmail());
                    emailService.sendUserUpdateEmail(user, oldEmail);
                } catch (EmailNotSentException | InvalidEmailAddressException | FileNotFoundException e) {
                    UserManagerLogger.severe(this.getClass(), e.getMessage());
                }
            } else {
                UserManagerLogger.warning(this.getClass(), "Not sending email warning, as sendEmailOnUpdate is '{}' and email has changed from '{}' to '{}'.",
                        sendEmailOnUpdate, oldEmail, dto.getEmail());
            }
        }
    }


    @Transactional
    public void delete(String username, String deletedBy) {
        delete(getByUsername(username), deletedBy);
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
        getProvider().findByUsername(username).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No uses exists with username '" + username + "'."));
    }


    public void setApplicationBackendServiceRole(UserDTO
                                                         userDTO, List<ApplicationBackendServiceRole> applicationBackendServiceRoles) {
        final User user = reverse(userDTO);
        user.setApplicationBackendServiceRoles(new HashSet<>(applicationBackendServiceRoles));
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

        final List<ApplicationBackendServiceRole> availableRoles = applicationBackendServiceRoleProvider
                .findByApplicationNameAndApplicationRole(applicationName, applicationRoleName);

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
        final List<Long> userIds = new ArrayList<>();
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
            throw new InvalidRequestException("Cannot sent confirmation email!", e);
        } catch (InvalidEmailAddressException e) {
            //Email must be already validated.
            throw new RuntimeException(e);
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
        teamProvider.findById(teamId).orElseThrow(()
                -> new TeamNotFoundException(this.getClass(), "No Team exists with id '" + teamId + "'."));

        final List<Long> userIds = new ArrayList<>();
        final Set<TeamMember> members = teamMemberProvider.findByIdUserGroupId(teamId);
        members.forEach(member -> userIds.add(member.getId().getUserId()));
        return convertAll(getProvider().findByIdIn(userIds));
    }


    public List<UserDTO> getByOrganization(String organizationName) {
        organizationProvider.findByName(organizationName).orElseThrow(()
                -> new TeamNotFoundException(this.getClass(), "No Organization exists with name '" + organizationName + "'."));

        final List<Long> userIds = new ArrayList<>();
        final Set<TeamMember> members = teamMemberProvider.findByOrganizationName(organizationName);
        members.forEach(member -> userIds.add(member.getId().getUserId()));
        return convertAll(getProvider().findByIdIn(userIds));
    }


    public List<UserDTO> findByUIDs(Collection<UUID> uuids) {
        return convertAll(getProvider().findByUuids(uuids));
    }


    @Override
    public Optional<IAuthenticatedUser> findByExternalReference(String externalReference) {
        final Optional<User> user = getProvider().findByExternalReference(externalReference);
        return user.map(value -> (IAuthenticatedUser) value);
    }

    public UserDTO getByExternalReference(String externalReference) {
        return convert(getProvider().findByExternalReference(externalReference).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No user with reference '" + externalReference + "' exists.")));
    }

    public List<UserDTO> getByExternalReference(List<String> externalReference) {
        return convertAll(getProvider().findByExternalReferences(externalReference));
    }
}

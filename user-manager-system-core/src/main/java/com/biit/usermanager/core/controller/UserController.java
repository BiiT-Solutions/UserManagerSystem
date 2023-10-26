package com.biit.usermanager.core.controller;

import com.biit.server.controller.ElementController;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationNotFoundException;
import com.biit.usermanager.core.exceptions.BackendServiceNotFoundException;
import com.biit.usermanager.core.exceptions.InvalidParameterException;
import com.biit.usermanager.core.exceptions.InvalidPasswordException;
import com.biit.usermanager.core.exceptions.UserAlreadyExistsException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.BackendServiceProvider;
import com.biit.usermanager.core.providers.UserApplicationBackendServiceRoleProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.core.utils.RoleNameGenerator;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.ApplicationBackendServiceRole;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import com.biit.usermanager.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;

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
public class UserController extends ElementController<User, Long, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter> implements IAuthenticatedUserProvider {

    @Value("${bcrypt.salt:}")
    private String bcryptSalt;

    private final ApplicationProvider applicationProvider;
    private final BackendServiceProvider backendServiceProvider;

    private final UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider;

    @Autowired
    protected UserController(UserProvider provider, UserConverter converter,
                             ApplicationProvider applicationProvider, BackendServiceProvider backendServiceProvider,
                             UserApplicationBackendServiceRoleProvider userApplicationBackendServiceRoleProvider) {
        super(provider, converter);
        this.applicationProvider = applicationProvider;
        this.backendServiceProvider = backendServiceProvider;
        this.userApplicationBackendServiceRoleProvider = userApplicationBackendServiceRoleProvider;
    }

    @Override
    protected UserConverterRequest createConverterRequest(User entity) {
        return new UserConverterRequest(entity);
    }

    public UserDTO getByUsername(String username) {
        final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
        return setGrantedAuthorities(userDTO, null, null);
    }

    public UserDTO checkCredentials(String username, String email, String password) {
        final User user;
        if (username != null) {
            try {
                user = getProvider().findByUsername(username).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                        "No User with username '" + username + "' found on the system."));
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

    @Override
    public IAuthenticatedUser create(CreateUserRequest createUserRequest, String createdBy) {
        return createUser(createUserRequest.getUsername(), createUserRequest.getUniqueId(), createUserRequest.getFirstname(),
                createUserRequest.getLastname(), createUserRequest.getPassword(), createdBy);
    }

    public IAuthenticatedUser createUser(String username, String uniqueId, String name, String lastName, String password, String createdBy) {
        if (findByUsername(username).isPresent()) {
            UserManagerLogger.warning(this.getClass(), "Username '" + username + "' already exists!.");
            throw new UserAlreadyExistsException(this.getClass(), "Username exists!");
        }
        final UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setUUID(UUID.randomUUID());
        user.setFirstName(name != null ? name : "");
        user.setLastname(lastName != null ? lastName : "");
        user.setIdCard(uniqueId);
        user.setPassword(password);
        user.setCreatedBy(createdBy);
        try {
            return getConverter().convert(new UserConverterRequest(getProvider().save(getConverter().reverse(user))));
        } finally {
            UserManagerLogger.info(this.getClass(), "User '" + username + "' created on the system.");
        }
    }

    @Override
    public IAuthenticatedUser updatePassword(String username, String oldPassword, String newPassword, String updatedBy) {
        final User user = getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
        //Check old password.
        if (oldPassword != null && !BCrypt.checkpw(bcryptSalt + oldPassword, user.getPassword())) {
            UserManagerLogger.warning(this.getClass(), "Provided password is incorrect!.");
            throw new InvalidParameterException(this.getClass(), "Provided password is incorrect!");
        }

        user.setPassword(bcryptSalt + newPassword);
        user.setUpdatedBy(updatedBy);
        UserManagerLogger.info(this.getClass(), "Password updated!.");
        return getConverter().convert(new UserConverterRequest(getProvider().save(user)));
    }

    /**
     * Updates a user password, but does not check the old password. Only for using by Admins.
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

    public void checkPassword(String username, String password) {
        final User user = getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."));
        //Check old password.
        if (password != null && !BCrypt.checkpw(bcryptSalt + password, user.getPassword())) {
            UserManagerLogger.warning(this.getClass(), "Provided password is incorrect!.");
            throw new InvalidParameterException(this.getClass(), "Provided password is incorrect!");
        }
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

    public UserDTO getByPhone(String phone) {
        return getConverter().convert(new UserConverterRequest(getProvider().findByPhone(phone).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with username '" + phone + "' found on the system."))));
    }

    public List<UserDTO> getAllByExpired(boolean accountExpired) {
        final List<User> usersList = getProvider().findByAccountExpired(accountExpired);
        final List<UserDTO> usersdtList = new ArrayList<>();
        for (final User user : usersList) {
            usersdtList.add(getConverter().convert(new UserConverterRequest(user)));
        }
        return usersdtList;
    }

    public void delete(User user) {
        UserManagerLogger.warning(this.getClass(), "Deleting user '" + user + "'!.");
        getProvider().delete(user);
    }

    @Override
    public Collection<IAuthenticatedUser> findAll() {
        return getProvider().findAll().parallelStream().map(this::createConverterRequest).map(getConverter()::convert).collect(Collectors.toList());
    }

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
    public Set<String> getRoles(String username, String groupName, String applicationName) {
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
    public void delete(UserDTO entity, String deletedBy) {
        if (Objects.equals(entity.getUsername(), deletedBy)) {
            throw new InvalidParameterException(this.getClass(), "You cannot delete your own user.");
        }
        getProvider().delete(getConverter().reverse(entity));
    }


    public void delete(String username, String deletedBy) {
        if (Objects.equals(username, deletedBy)) {
            throw new InvalidParameterException(this.getClass(), "You cannot delete your own user.");
        }
        final long count = getProvider().deleteByUsername(username);
        if (count == 0) {
            throw new UserNotFoundException(this.getClass(), "Cannot delete user.");
        } else {
            UserManagerLogger.warning(this.getClass(), "Deleting user '" + username + "'!.");
        }
    }

    /**
     * Populate the authorities for a user. If a group is selected, only the one of the group. If not the roles that are not at group level.
     *
     * @param userDTO The user to populate.
     * @return the populated user.
     */
    private UserDTO setGrantedAuthorities(UserDTO userDTO, Application application, BackendService backendService) {
        if (userDTO != null) {
            final List<UserApplicationBackendServiceRole> userApplicationBackendServiceRoles =
                    userApplicationBackendServiceRoleProvider.findByUserId(userDTO.getId());

            userApplicationBackendServiceRoles.forEach(userApplicationBackendServiceRole -> {
                if ((application == null
                        || Objects.equals(userApplicationBackendServiceRole.getId().getApplicationName(), application.getName()))
                        && (backendService == null
                        || Objects.equals(userApplicationBackendServiceRole.getId().getBackendServiceName(), backendService.getName()))) {
                    userDTO.addApplicationServiceRoles(RoleNameGenerator.createApplicationRoleName(userApplicationBackendServiceRole));
                    userDTO.addGrantedAuthorities(RoleNameGenerator.createBackendRoleName(userApplicationBackendServiceRole));
                }
            });

            UserManagerLogger.debug(this.getClass(), "Assigning application roles '" + userDTO.getApplicationRoles()
                    + "' to '" + userDTO.getUsername() + "'.");
            UserManagerLogger.debug(this.getClass(), "Assigning backend roles '" + userDTO.getGrantedAuthorities()
                    + "' to '" + userDTO.getUsername() + "'.");
        }
        return userDTO;
    }

    public void checkUsernameExists(String username) {
        getProvider().findByUsername(username).orElseThrow(()
                -> new UserNotFoundException(this.getClass(), "No uses exists with the username '" + username + "'."));
    }

    public void setApplicationBackendServiceRole(UserDTO userDTO, List<ApplicationBackendServiceRole> applicationBackendServiceRoles) {
        final User user = reverse(userDTO);
        user.setApplicationBackendServiceRoles(applicationBackendServiceRoles);
        getProvider().save(user);
    }
}

package com.biit.usermanager.core.controller;

import com.biit.server.controller.BasicElementController;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.GroupConverter;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.converters.models.GroupConverterRequest;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.exceptions.ApplicationNotFoundException;
import com.biit.usermanager.core.exceptions.InvalidParameterException;
import com.biit.usermanager.core.exceptions.InvalidPasswordException;
import com.biit.usermanager.core.exceptions.UserAlreadyExistsException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.GroupProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.core.providers.UserRoleProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.GroupDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.User;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@Order(1)
@Qualifier("userController")
public class UserController extends BasicElementController<User, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter> implements IAuthenticatedUserProvider {
    private final UserRoleProvider userRoleProvider;

    @Value("${bcrypt.salt:}")
    private String bcryptSalt;

    private final ApplicationProvider applicationProvider;
    private final ApplicationConverter applicationConverter;
    private final GroupProvider groupProvider;
    private final GroupConverter groupConverter;

    @Autowired
    protected UserController(UserProvider provider, UserConverter converter,
                             UserRoleProvider userRoleProvider, ApplicationConverter applicationConverter,
                             ApplicationProvider applicationProvider, GroupProvider groupProvider, GroupConverter groupConverter) {
        super(provider, converter);
        this.userRoleProvider = userRoleProvider;
        this.applicationProvider = applicationProvider;
        this.groupProvider = groupProvider;
        this.applicationConverter = applicationConverter;
        this.groupConverter = groupConverter;
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
    public Optional<IAuthenticatedUser> findByUsername(String username, String applicationName) {
        if (applicationName == null) {
            return findByUsername(username);
        }
        try {
            final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
            final UserDTO grantedUser = setGrantedAuthorities(userDTO, null,
                    applicationConverter.convert(new ApplicationConverterRequest(applicationProvider.findByName(applicationName).orElseThrow(() ->
                            new ApplicationNotFoundException(this.getClass(), "No application exists with name '" + applicationName + "'.")))));
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
            try {
                final UserDTO grantedUserDTO = setGrantedAuthorities(userDTO, null,
                        applicationConverter.convert(new ApplicationConverterRequest(applicationProvider.findByName(applicationName).orElseThrow(() ->
                                new ApplicationNotFoundException(this.getClass(), "No application exists with name '" + applicationName + "'.")))));
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
        final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
        //Check old password.
        if (oldPassword != null && !BCrypt.checkpw(bcryptSalt + oldPassword, userDTO.getPassword())) {
            UserManagerLogger.warning(this.getClass(), "Provided password is incorrect!.");
            throw new InvalidParameterException(this.getClass(), "Provided password is incorrect!");
        }

        userDTO.setPassword(newPassword);
        userDTO.setUpdatedBy(updatedBy);
        UserManagerLogger.info(this.getClass(), "Password updated!.");
        return getConverter().convert(new UserConverterRequest(getProvider().save(getConverter().reverse(userDTO))));
    }

    public void checkPassword(String username, String password) {
        final UserDTO userDTO = getConverter().convert(new UserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
        //Check old password.
        if (password != null && !BCrypt.checkpw(bcryptSalt + password, userDTO.getPassword())) {
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
     * @return
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
        final UserDTO userDTO = getByUsername(createUserRequest.getUsername());
        userDTO.setUsername(createUserRequest.getUsername());
        userDTO.setFirstName(createUserRequest.getFirstname());
        userDTO.setLastname(createUserRequest.getLastname());
        userDTO.setIdCard(createUserRequest.getUniqueId());
        userDTO.setUpdatedBy(updatedBy);
        UserManagerLogger.info(this.getClass(), "User '" + createUserRequest.getUsername() + "' updated!.");
        return getConverter().convert(new UserConverterRequest(getProvider().update(getConverter().reverse(userDTO))));
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
    public boolean deleteUser(String name, String username) {
        return delete(username) != null;
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
        final GroupDTO groupDTO = groupConverter.convert(new GroupConverterRequest(
                groupProvider.findByName(groupName).orElse(null)));
        final ApplicationDTO applicationDTO = applicationConverter.convert(new ApplicationConverterRequest(
                applicationProvider.findByName(applicationName).orElse(null)));
        final UserDTO userWithRoles = setGrantedAuthorities(userDTO, groupDTO, applicationDTO);
        if (userWithRoles != null) {
            final Set<String> roles = userWithRoles.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            UserManagerLogger.debug(this.getClass(), "Obtained roles '" + roles
                    + "' from '" + username + "'.");
            return roles;
        }
        return new HashSet<>();
    }

    public UserDTO delete(String username) {
        final Optional<User> user = getProvider().deleteByUsername(username);
        UserManagerLogger.warning(this.getClass(), "Deleting user '" + user + "'!.");
        return user.map(value -> getConverter().convert(new UserConverterRequest(value))).orElse(null);
    }

    private UserDTO setGrantedAuthorities(UserDTO userDTO, GroupDTO groupDTO, ApplicationDTO applicationDTO) {
        if (userDTO != null) {
            final Set<String> grantedAuthorities = new HashSet<>();
            userRoleProvider.findByUserAndGroupAndApplication(
                            getConverter().reverse(userDTO),
                            groupConverter.reverse(groupDTO),
                            applicationConverter.reverse(applicationDTO)
                    ).stream()
                    .filter(userRole -> userRole.getRole() != null && userRole.getRole().getName() != null)
                    .forEach(userRole -> grantedAuthorities.add(userRole.getRole().getName().toUpperCase()));
            UserManagerLogger.debug(this.getClass(), "Assigning authorities '" + grantedAuthorities
                    + "' to '" + userDTO.getUsername() + "'.");
            userDTO.setGrantedAuthorities(grantedAuthorities);
        }
        return userDTO;
    }
}

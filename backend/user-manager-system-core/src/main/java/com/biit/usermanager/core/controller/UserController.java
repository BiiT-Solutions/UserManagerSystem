package com.biit.usermanager.core.controller;

import com.biit.server.controller.BasicInsertableController;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.OrganizationConverter;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.UserRoleConverter;
import com.biit.usermanager.core.converters.models.ApplicationConverterRequest;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.exceptions.*;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.OrganizationProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.core.providers.UserRoleProvider;
import com.biit.usermanager.dto.ApplicationDTO;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Controller
public class UserController extends BasicInsertableController<User, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter> implements IAuthenticatedUserProvider {
    private final UserRoleProvider userRoleProvider;

    private final UserRoleConverter userRoleConverter;
    private final ApplicationProvider applicationProvider;
    private final ApplicationConverter applicationConverter;
    private final OrganizationProvider organizationProvider;
    private final OrganizationConverter organizationConverter;

    @Autowired
    protected UserController(UserProvider provider, UserConverter converter,
                             UserRoleProvider userRoleProvider, UserRoleConverter userRoleConverter,
                             ApplicationConverter applicationConverter, ApplicationProvider applicationProvider,
                             OrganizationProvider organizationProvider, OrganizationConverter organizationConverter) {
        super(provider, converter);
        this.userRoleProvider = userRoleProvider;
        this.userRoleConverter = userRoleConverter;
        this.applicationProvider = applicationProvider;
        this.organizationProvider = organizationProvider;
        this.applicationConverter = applicationConverter;
        this.organizationConverter = organizationConverter;
    }

    @Override
    protected UserConverterRequest createConverterRequest(User entity) {
        return new UserConverterRequest(entity);
    }

    public UserDTO getByUsername(String username) {
        final UserDTO userDTO = converter.convert(new UserConverterRequest(provider.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
        return setGrantedAuthorities(userDTO, null, null);
    }

    public UserDTO checkCredentials(String username, String email, String password) {
        final User user;
        if (username != null) {
            user = provider.findByUsername(username).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                    "No User with username '" + username + "' found on the system."));
        } else {
            user = provider.findByEmail(email).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                    "No User with email '" + email + "' found on the system."));
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new InvalidPasswordException(this.getClass(), "Password '" + password + "' does not match the correct one!");
        }
        return setGrantedAuthorities(converter.convert(new UserConverterRequest(user)), null, null);
    }

    public UserDTO getByEmail(String email) {
        final UserDTO userDTO = converter.convert(new UserConverterRequest(provider.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(this.getClass(),
                        "No User with email '" + email + "' found on the system."))));
        return setGrantedAuthorities(userDTO, null, null);
    }


    public UserDTO getByUserId(String id) {
        return converter.convert(new UserConverterRequest(provider.getById(id).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with id '" + id + "' found on the system."))));
    }

    @Override
    public Optional<IAuthenticatedUser> findByUsername(String username) {
        try {
            return Optional.of(getByUsername(username));
        } catch (UserNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<IAuthenticatedUser> findByUsername(String username, String applicationName) {
        if (applicationName == null) {
            return findByUsername(username);
        }
        final UserDTO userDTO = converter.convert(new UserConverterRequest(provider.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
        return Optional.of(setGrantedAuthorities(userDTO, null,
                applicationConverter.convert(new ApplicationConverterRequest(applicationProvider.findByName(applicationName).orElseThrow(() ->
                        new ApplicationNotFoundException(this.getClass(), "No application exists with name '" + applicationName + "'."))))));
    }

    @Override
    public Optional<IAuthenticatedUser> findByUID(String uid) {
        final UserDTO userDTO = converter.convert(new UserConverterRequest(provider.get(Long.parseLong(uid)).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with uid '" + uid + "' found on the system."))));
        return Optional.of(setGrantedAuthorities(userDTO, null, null));
    }

    @Override
    public IAuthenticatedUser create(CreateUserRequest createUserRequest) {
        return createUser(createUserRequest.getUsername(), createUserRequest.getUniqueId(), createUserRequest.getName(),
                createUserRequest.getLastname(), createUserRequest.getPassword());
    }

    public IAuthenticatedUser createUser(String username, String uniqueId, String name, String lastName, String password) {
        if (findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException(this.getClass(), "Username exists!");
        }
        final UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setFirstName(name);
        user.setLastname(lastName);
        user.setIdCard(uniqueId);
        user.setPassword(password);
        return converter.convert(new UserConverterRequest(provider.save(converter.reverse(user))));
    }

    @Override
    public IAuthenticatedUser updatePassword(String username, String oldPassword, String newPassword) {
        final UserDTO userDTO = converter.convert(new UserConverterRequest(provider.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
        //Check old password.
        if (oldPassword != null && !BCrypt.checkpw(oldPassword, userDTO.getPassword())) {
            throw new InvalidParameterException(this.getClass(), "Provided password is incorrect!");
        }

        userDTO.setPassword(newPassword);
        return converter.convert(new UserConverterRequest(provider.save(converter.reverse(userDTO))));
    }

    @Override
    public IAuthenticatedUser updateUser(CreateUserRequest createUserRequest) {
        final UserDTO userDTO = getByUsername(createUserRequest.getUsername());
        userDTO.setUsername(createUserRequest.getUsername());
        userDTO.setFirstName(createUserRequest.getName());
        userDTO.setLastname(createUserRequest.getLastname());
        userDTO.setIdCard(createUserRequest.getUniqueId());
        return converter.convert(new UserConverterRequest(provider.update(converter.reverse(userDTO))));
    }

    public List<UserDTO> getByEnable(Boolean enable) {
        return provider.findAllByEnable(enable).parallelStream().map(this::createConverterRequest).map(converter::convert).collect(Collectors.toList());
    }

    public UserDTO getByPhone(String phone) {
        return converter.convert(new UserConverterRequest(provider.findByPhone(phone).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with username '" + phone + "' found on the system."))));
    }

    public List<UserDTO> getAllByExpired(boolean accountExpired) {
        final List<User> usersList = provider.findByAccountExpired(accountExpired);
        final List<UserDTO> usersdtList = new ArrayList<>();
        for (final User user : usersList) {
            usersdtList.add(converter.convert(new UserConverterRequest(user)));
        }
        return usersdtList;
    }

    public void delete(User user) {
        provider.delete(user);
    }

    @Override
    public Collection<IAuthenticatedUser> findAll() {
        return provider.findAll().parallelStream().map(this::createConverterRequest).map(converter::convert).collect(Collectors.toList());
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
    public Set<String> getRoles(String username, String organizationName, String applicationName) {
        final UserDTO userDTO = getByUsername(username);
        final OrganizationDTO organizationDTO = organizationConverter.convert(new OrganizationConverterRequest(
                organizationProvider.findByName(organizationName).orElse(null)));
        final ApplicationDTO applicationDTO = applicationConverter.convert(new ApplicationConverterRequest(
                applicationProvider.findByName(applicationName).orElse(null)));
        final UserDTO userWithRoles = setGrantedAuthorities(userDTO, organizationDTO, applicationDTO);
        if (userWithRoles != null) {
            return userWithRoles.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public UserDTO delete(String username) {
        final Optional<User> user = provider.deleteByUsername(username);
        return user.map(value -> converter.convert(new UserConverterRequest(value))).orElse(null);
    }

    private UserDTO setGrantedAuthorities(UserDTO userDTO, OrganizationDTO organizationDTO, ApplicationDTO applicationDTO) {
        if (userDTO != null) {
            final Set<String> grantedAuthorities = new HashSet<>();
            userRoleProvider.findByUserAndOrganizationAndApplication(
                            converter.reverse(userDTO),
                            organizationConverter.reverse(organizationDTO),
                            applicationConverter.reverse(applicationDTO)
                    ).stream()
                    .filter(userRole -> userRole.getRole() != null && userRole.getRole().getName() != null)
                    .forEach(userRole -> grantedAuthorities.add(userRole.getRole().getName().toUpperCase()));
            userDTO.setGrantedAuthorities(grantedAuthorities);
        }
        return userDTO;
    }
}

package com.biit.usermanager.rest.providers;

import com.biit.server.exceptions.BadRequestException;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.server.security.rest.exceptions.InvalidPasswordException;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.controller.UserRoleController;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.exceptions.InvalidParameterException;
import com.biit.usermanager.dto.OrganizationDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.logger.UserManagerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary
public class AuthenticatedUserProvider implements IAuthenticatedUserProvider {
    private final UserController userController;
    private final UserRoleController userRoleController;

    @Autowired
    public AuthenticatedUserProvider(UserController userController, UserRoleController userRoleController) {
        this.userController = userController;
        this.userRoleController = userRoleController;
    }


    @Override
    public Optional<IAuthenticatedUser> findByUsername(String username) {
        try {
            return Optional.of(setGrantedAuthorities(userController.getByUserName(username), null));
        } catch (UserNotFoundException e) {
            UserManagerLogger.warning(this.getClass(), e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<IAuthenticatedUser> findByUID(String uid) {
        try {
            return Optional.of(setGrantedAuthorities(userController.get(Long.parseLong(uid)), null));
        } catch (NotFoundException e) {
            UserManagerLogger.warning(this.getClass(), e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public IAuthenticatedUser create(CreateUserRequest createUserRequest) {
        return createUser(createUserRequest.getUsername(), createUserRequest.getUniqueId(), createUserRequest.getName(),
                createUserRequest.getLastname(), createUserRequest.getPassword());
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        try {
            userController.updatePassword(username, oldPassword, newPassword);
        } catch (InvalidParameterException e) {
            throw new InvalidPasswordException(this.getClass(), "Provided password is incorrect!");
        }
    }

    @Override
    public IAuthenticatedUser updateUser(CreateUserRequest createUserRequest) {
        final IAuthenticatedUser user = userController.getByUserName(createUserRequest.getUsername());
        final UserDTO userDTO = new UserDTO();
        userDTO.setUsername(createUserRequest.getUsername());
        userDTO.setFirstName(createUserRequest.getName());
        userDTO.setLastname(createUserRequest.getLastname());
        userDTO.setIdCard(createUserRequest.getUniqueId());
        userDTO.setPassword(user.getPassword());
        userDTO.setGrantedAuthorities((Set<SimpleGrantedAuthority>) user.getAuthorities());
        return userController.update(userDTO);
    }

    @Override
    public long count() {
        return userController.count();
    }

    @Override
    public Collection<IAuthenticatedUser> findAll() {
        return new ArrayList<>(userController.findAll());
    }

    @Override
    public void deleteUser(String name, String username) {
        delete(findByUsername(username).orElse(null));
    }

    @Override
    public void delete(IAuthenticatedUser authenticatedUser) {
        userController.delete((UserDTO) authenticatedUser);
    }

    @Override
    public Set<String> getRoles(String username) {
        throw new UnsupportedOperationException("getRoles not implemented yet!");
    }

    public IAuthenticatedUser createUser(String username, String uniqueId, String name, String lastName, String password) {
        if (findByUsername(username).isPresent()) {
            throw new BadRequestException(this.getClass(), "Username exists!");
        }
        final UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setFirstName(name);
        user.setLastname(lastName);
        user.setIdCard(uniqueId);
        user.setPassword(password);
        return userController.create(user);
    }

    private UserDTO setGrantedAuthorities(UserDTO userDTO, OrganizationDTO organizationDTO) {
        if (userDTO != null) {
            final Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
            userRoleController.getByUserAndOrganization(userDTO, organizationDTO).stream()
                    .filter(userRoleDTO -> userRoleDTO.getRole() != null && userRoleDTO.getRole().getName() != null)
                    .forEach(userRoleDTO -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userRoleDTO.getRole().getName().toUpperCase())));
            userDTO.setGrantedAuthorities(grantedAuthorities);
        }
        return userDTO;
    }
}

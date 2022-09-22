package com.biit.usermanager.rest.providers;

import com.biit.server.exceptions.BadRequestException;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.controller.UserRoleController;
import com.biit.usermanager.core.controller.models.OrganizationDTO;
import com.biit.usermanager.core.controller.models.UserDTO;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.logger.UserManagerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
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

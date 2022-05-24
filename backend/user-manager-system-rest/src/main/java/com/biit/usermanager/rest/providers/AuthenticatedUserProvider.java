package com.biit.usermanager.rest.providers;

import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.exceptions.NotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.logger.UserManagerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthenticatedUserProvider implements IAuthenticatedUserProvider<Long> {
    private final UserController userController;

    @Autowired
    public AuthenticatedUserProvider(UserController userController) {
        this.userController = userController;
    }

    @Override
    public Optional<IAuthenticatedUser<Long>> findByUsername(String username) {
        try {
            return Optional.of(userController.getByUserName(username));
        } catch (UserNotFoundException e) {
            UserManagerLogger.warning(this.getClass(), e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<IAuthenticatedUser<Long>> findByUniqueId(Long uniqueId) {
        try {
            return Optional.of(userController.get(uniqueId));
        } catch (NotFoundException e) {
            UserManagerLogger.warning(this.getClass(), e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public IAuthenticatedUser<Long> create(CreateUserRequest createUserRequest) {
        return null;
    }
}

package com.biit.usermanager.rest.providers;

import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.server.security.rest.dto.CreateUserRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthenticatedUserProvider implements IAuthenticatedUserProvider {

    @Override
    public Optional<IAuthenticatedUser> findByUsername(String s) {
        return Optional.empty();
    }

    @Override
    public Optional<IAuthenticatedUser> findByUniqueId(String s) {
        return Optional.empty();
    }

    @Override
    public IAuthenticatedUser create(CreateUserRequest createUserRequest) {
        return null;
    }
}

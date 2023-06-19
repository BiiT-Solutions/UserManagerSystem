package com.biit.server.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Primary
@Repository
public class AuthenticatedUserProvider implements IAuthenticatedUserProvider {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Value("#{'${user.provider.test.authorities}'.split(',')}")
    private List<String> authorities;

    private static int idCounter = 1;

    private final Collection<IAuthenticatedUser> usersOnMemory = new ArrayList<>();


    @Override
    public Optional<IAuthenticatedUser> findByUsername(String username) {
        return usersOnMemory.stream().filter(user -> Objects.equals(username, user.getUsername())).findAny();
    }

    @Override
    public Optional<IAuthenticatedUser> findByUsername(String username, String applicationName) {
        return findByUsername(username);
    }

    @Override
    public Optional<IAuthenticatedUser> findByEmailAddress(String email) {
        return usersOnMemory.stream().filter(user -> Objects.equals(email, user.getEmailAddress())).findAny();
    }

    @Override
    public Optional<IAuthenticatedUser> findByEmailAddress(String email, String applicationName) {
        return findByEmailAddress(email);
    }

    @Override
    public Optional<IAuthenticatedUser> findByUID(String uid) {
        return usersOnMemory.stream().filter(user -> Objects.equals(uid, user.getUID())).findAny();
    }

    @Override
    public IAuthenticatedUser create(CreateUserRequest createUserRequest, String createdBy) {
        return createUser(createUserRequest.getUsername(), createUserRequest.getUniqueId(), createUserRequest.getFirstname(),
                createUserRequest.getLastname(), createUserRequest.getPassword());
    }

    public void clear() {
        usersOnMemory.clear();
    }

    public IAuthenticatedUser createUser(String username, String uniqueId, String name, String lastName, String password) {
        if (findByUsername(username).isPresent()) {
            throw new RuntimeException("Username exists!");
        }

        final AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setUsername(username);
        authenticatedUser.setAuthorities(authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        authenticatedUser.setUID(uniqueId);
        authenticatedUser.setName(name);
        authenticatedUser.setLastname(lastName);
        if (password != null) {
            authenticatedUser.setPassword(encoder.encode(password));
        }

        usersOnMemory.add(authenticatedUser);

        return authenticatedUser;
    }

    public IAuthenticatedUser createUser(String username, String name, String password) {
        if (findByUsername(username).isPresent()) {
            throw new RuntimeException("Username exists!");
        }

        final AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setUsername(username);
        authenticatedUser.setUID(String.valueOf(idCounter++));
        authenticatedUser.setAuthorities(authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        authenticatedUser.setName(name);
        authenticatedUser.setPassword(password);

        usersOnMemory.add(authenticatedUser);

        return authenticatedUser;
    }


    @Override
    public IAuthenticatedUser updateUser(CreateUserRequest createUserRequest, String updatedBy) {
        final AuthenticatedUser user = (AuthenticatedUser) usersOnMemory.stream().filter(iAuthenticatedUser ->
                iAuthenticatedUser.getUsername().equals(createUserRequest.getUsername())).findAny().orElseThrow(() ->
                new RuntimeException("User with username '" + createUserRequest.getUsername() + "' does not exists"));
        user.setName(createUserRequest.getFirstname());
        user.setLastname(createUserRequest.getLastname());
        return user;
    }

    @Override
    public IAuthenticatedUser updatePassword(String username, String oldPassword, String newPassword, String updatedBy) {
        final IAuthenticatedUser user = usersOnMemory.stream().filter(iAuthenticatedUser -> iAuthenticatedUser.getUsername().equals(username))
                .findAny().orElseThrow(() ->
                        new RuntimeException("User with username '" + username + "' does not exists"));

        //Check old password.
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new RuntimeException("Provided password is incorrect!");
        }

        //Update new password.
        user.setPassword(newPassword);
        return user;
    }

    @Override
    public String getPassword(String username) {
        IAuthenticatedUser user = usersOnMemory.stream().filter(iAuthenticatedUser -> iAuthenticatedUser.getUsername().equals(username))
                .findAny().orElseThrow(() ->
                new RuntimeException("User with username '" + username + "' does not exists"));
        return user.getPassword();
    }

    @Override
    public String getPasswordByUid(String uid) {
        IAuthenticatedUser user = usersOnMemory.stream().filter(iAuthenticatedUser -> iAuthenticatedUser.getUID().equals(uid))
                .findAny().orElseThrow(() ->
                        new RuntimeException("User with uid '" + uid + "' does not exists"));
        return user.getPassword();
    }


    @Override
    public Collection<IAuthenticatedUser> findAll() {
        return usersOnMemory;
    }

    @Override
    public boolean deleteUser(String name, String username) {
        return delete(findByUsername(username).orElse(null));
    }

    @Override
    public boolean delete(IAuthenticatedUser authenticatedUser) {
        return usersOnMemory.remove(authenticatedUser);
    }

    @Override
    public Set<String> getRoles(String username, String groupName, String application) {
        return null;
    }


    @Override
    public long count() {
        return usersOnMemory.size();
    }
}

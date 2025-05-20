package com.biit.usermanager.client.providers;

import com.biit.server.client.user.AuthenticatedUser;
import com.biit.server.security.CreateUserRequest;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.IAuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticatedUserProvider implements IAuthenticatedUserProvider {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Value("#{'${user.provider.test.authorities:ADMIN,EDITOR,VIEWER}'.split(',')}")
    private List<String> authorities;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${bcrypt.salt:}")
    private String bcryptSalt;

    private final Collection<IAuthenticatedUser> usersOnMemory = new ArrayList<>();
    private final Map<IAuthenticatedUser, Set<String>> userRoles = new HashMap<>();


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
                createUserRequest.getLastname(), createUserRequest.getPassword(), Locale.ENGLISH);
    }

    public void clear() {
        usersOnMemory.clear();
    }

    public IAuthenticatedUser createUser(String username, String uuid, String name, String lastName, String password, Locale locale) {
        if (findByUsername(username).isPresent()) {
            throw new RuntimeException("Username exists!");
        }

        final AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setUsername(username);

        //Add application in authority.
        final Set<String> applicationAuthorities = authorities.stream().map(s -> applicationName.toUpperCase() + "_" + s.replace(" ", ""))
                .collect(Collectors.toSet());

        //Also set the pure ones to be sure that on tests people is allowed to use them.
        applicationAuthorities.addAll(authorities.stream().map(s -> s.replace(" ", "")).collect(Collectors.toSet()));

        authenticatedUser.setAuthorities(applicationAuthorities.stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        authenticatedUser.setUID(uuid);
        authenticatedUser.setName(name);
        authenticatedUser.setLastname(lastName);
        authenticatedUser.setLocale(locale);
        if (password != null) {
            authenticatedUser.setPassword(encoder.encode(bcryptSalt + password));
        }

        usersOnMemory.add(authenticatedUser);
        //Roles with application name.
        final Set<String> roles = authorities.stream().map(s -> applicationName.toUpperCase() + "_" + s.replace(" ", ""))
                .collect(Collectors.toSet());
        //Pure roles
        roles.addAll(authorities.stream().map(s -> s.replace(" ", "")).collect(Collectors.toSet()));
        setRoles(authenticatedUser, roles);

        return authenticatedUser;
    }

    public IAuthenticatedUser createUser(String username, String name, String password) {
        return createUser(username, UUID.randomUUID().toString(), name, null, password, Locale.ENGLISH);
    }

    public void updateUser(AuthenticatedUser user) {
        if (user == null) {
            return;
        }
        usersOnMemory.removeIf(x -> Objects.equals(x.getUsername(), user.getUsername()));
        usersOnMemory.add(user);
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
        if (!BCrypt.checkpw(bcryptSalt + oldPassword, user.getPassword())) {
            throw new RuntimeException("Provided password is incorrect!");
        }

        //Update new password.
        user.setPassword(newPassword);
        return user;
    }

    @Override
    public String getPassword(String username) {
        final IAuthenticatedUser user = usersOnMemory.stream().filter(iAuthenticatedUser -> iAuthenticatedUser.getUsername().equals(username))
                .findAny().orElseThrow(() ->
                        new RuntimeException("User with username '" + username + "' does not exists"));
        return user.getPassword();
    }

    @Override
    public String getPasswordByUid(String uid) {
        final IAuthenticatedUser user = usersOnMemory.stream().filter(iAuthenticatedUser -> iAuthenticatedUser.getUID().equals(uid))
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
    public Set<String> getRoles(String username, String application) {
        final IAuthenticatedUser user = usersOnMemory.stream().filter(iAuthenticatedUser -> iAuthenticatedUser.getUsername().equals(username))
                .findAny().orElseThrow(() ->
                        new RuntimeException("User with username '" + username + "' does not exists"));
        return userRoles.get(user);
    }

    public void setRoles(String username, Set<String> roles) {
        final IAuthenticatedUser user = usersOnMemory.stream().filter(iAuthenticatedUser -> iAuthenticatedUser.getUsername().equals(username))
                .findAny().orElseThrow(() ->
                        new RuntimeException("User with username '" + username + "' does not exists"));
        setRoles(user, roles);
    }

    @Override
    public Optional<IAuthenticatedUser> findByExternalReference(String externalReference) {
        return usersOnMemory.stream().filter(iAuthenticatedUser -> iAuthenticatedUser.getExternalReference().equals(externalReference))
                .findAny();
    }

    public void setRoles(IAuthenticatedUser user, String... roles) {
        setRoles(user, Set.of(roles));
    }

    public void setRoles(IAuthenticatedUser user, Set<String> roles) {
        userRoles.put(user, roles);
    }

    @Override
    public long count() {
        return usersOnMemory.size();
    }
}

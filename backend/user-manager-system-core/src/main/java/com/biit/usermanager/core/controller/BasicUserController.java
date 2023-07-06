package com.biit.usermanager.core.controller;

import com.biit.server.controller.BasicElementController;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.usermanager.core.converters.ApplicationConverter;
import com.biit.usermanager.core.converters.BasicUserConverter;
import com.biit.usermanager.core.converters.GroupConverter;
import com.biit.usermanager.core.converters.models.BasicUserConverterRequest;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.ApplicationProvider;
import com.biit.usermanager.core.providers.GroupProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.core.providers.UserRoleProvider;
import com.biit.usermanager.dto.BasicUserDTO;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Controller
public class BasicUserController extends BasicElementController<User, BasicUserDTO, UserRepository,
        UserProvider, BasicUserConverterRequest, BasicUserConverter> {
    private final UserRoleProvider userRoleProvider;

    private final ApplicationProvider applicationProvider;
    private final ApplicationConverter applicationConverter;
    private final GroupProvider groupProvider;
    private final GroupConverter groupConverter;

    @Autowired
    protected BasicUserController(UserProvider provider, BasicUserConverter converter,
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
    protected BasicUserConverterRequest createConverterRequest(User entity) {
        return new BasicUserConverterRequest(entity);
    }

    public BasicUserDTO getByUsername(String username) {
        return getConverter().convert(new BasicUserConverterRequest(getProvider().findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
    }

    public BasicUserDTO getByUserId(String id) {
        return getConverter().convert(new BasicUserConverterRequest(getProvider().getById(id).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with id '" + id + "' found on the system."))));
    }

    public Optional<BasicUserDTO> findByUsername(String username) {
        try {
            return Optional.of(getByUsername(username));
        } catch (UserNotFoundException e) {
            UserManagerLogger.warning(this.getClass(), "No User with username '" + username + "' found on the system.");
            return Optional.empty();
        }
    }

    public Optional<IAuthenticatedUser> findByUID(String uuid) {
        try {
            return Optional.of(getConverter().convert(new BasicUserConverterRequest(getProvider().findByUuid(UUID.fromString(uuid)).orElseThrow(() ->
                    new UserNotFoundException(this.getClass(), "No User with uuid '" + uuid + "' found on the system.")))));
        } catch (IllegalArgumentException e) {
            UserManagerLogger.warning(this.getClass(), "Invalid uuid '" + uuid + "'!.");
            throw e;
        } catch (Exception e) {
            UserManagerLogger.warning(this.getClass(), "No User with id '" + uuid + "' found on the system.");
            throw e;
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
        final User user = getProvider().getById(uid).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with id '" + uid + "' found on the system."));
        return user.getPassword();
    }

    public List<BasicUserDTO> getByEnable(Boolean enable) {
        return getProvider().findAllByEnable(enable).parallelStream().map(this::createConverterRequest).map(getConverter()::convert)
                .collect(Collectors.toList());
    }

    public BasicUserDTO getByPhone(String phone) {
        return getConverter().convert(new BasicUserConverterRequest(getProvider().findByPhone(phone).orElseThrow(() ->
                new UserNotFoundException(this.getClass(),
                "No User with username '" + phone + "' found on the system."))));
    }

    public List<BasicUserDTO> getAllByExpired(boolean accountExpired) {
        final List<User> usersList = getProvider().findByAccountExpired(accountExpired);
        final List<BasicUserDTO> usersdtList = new ArrayList<>();
        for (final User user : usersList) {
            usersdtList.add(getConverter().convert(new BasicUserConverterRequest(user)));
        }
        return usersdtList;
    }

    public void delete(BasicUserDTO user) {
        UserManagerLogger.warning(this.getClass(), "Deleting user '" + user + "'!.");
        getProvider().delete(getConverter().reverse(user));
    }
}

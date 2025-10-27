package com.biit.usermanager.core.controller;

/*-
 * #%L
 * User Manager System (core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.kafka.controllers.KafkaElementController;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IAuthenticatedUser;
import com.biit.server.security.model.IUserOrganization;
import com.biit.usermanager.core.converters.BasicUserConverter;
import com.biit.usermanager.core.converters.models.BasicUserConverterRequest;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.kafka.BasicUserEventSender;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.BasicUserDTO;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Controller
public class BasicUserController extends KafkaElementController<User, Long, BasicUserDTO, UserRepository,
        UserProvider, BasicUserConverterRequest, BasicUserConverter> {

    @Autowired
    protected BasicUserController(UserProvider provider, BasicUserConverter converter,
                                  BasicUserEventSender eventSender, List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(provider, converter, eventSender, userOrganizationProvider);
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

    public List<BasicUserDTO> getByAccountBlocked(Boolean accountBlocked) {
        return getProvider().findAllByAccountBlocked(accountBlocked).parallelStream().map(this::createConverterRequest).map(getConverter()::convert)
                .collect(Collectors.toList());
    }

    public void delete(BasicUserDTO user) {
        UserManagerLogger.warning(this.getClass(), "Deleting user '" + user + "'!.");
        getProvider().delete(getConverter().reverse(user));
    }
}

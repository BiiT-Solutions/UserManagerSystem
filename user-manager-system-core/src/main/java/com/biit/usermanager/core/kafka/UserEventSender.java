package com.biit.usermanager.core.kafka;

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


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.EventSubject;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.core.utils.EventTags;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserApplicationBackendServiceRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserEventSender extends EventSender<UserDTO> {

    private static final String EVENT_TYPE = "users";
    public static final String REVOCATION_EVENT_TAG = "Revoked Token";

    private final UserConverter userConverter;

    private final UserProvider userProvider;


    public UserEventSender(@Autowired(required = false) KafkaEventTemplate kafkaTemplate,
                           UserConverter userConverter, UserProvider userProvider) {
        super(kafkaTemplate, EventTags.USER, EVENT_TYPE);
        this.userConverter = userConverter;
        this.userProvider = userProvider;
    }

    public void sendEvents(Collection<UserApplicationBackendServiceRole> usersToInform, String tag, String launchedBy) {
        sendEventsByUserId(usersToInform.stream().map(userApplicationBackendServiceRole
                -> userApplicationBackendServiceRole.getId().getUserId()).collect(Collectors.toSet()), tag, launchedBy);
    }

    public void sendEventsByUserId(Collection<Long> userIds, String tag, String launchedBy) {
        final List<User> users = userProvider.findByIdIn(userIds);
        userConverter.convertAll(users.stream().map(UserConverterRequest::new).toList()).forEach(userDTO ->
                sendEvents(userDTO, EventSubject.UPDATED, tag, launchedBy));
    }
}

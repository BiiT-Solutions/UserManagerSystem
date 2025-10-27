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
import com.biit.usermanager.core.converters.UserGroupConverter;
import com.biit.usermanager.core.converters.models.UserGroupConverterRequest;
import com.biit.usermanager.core.providers.UserGroupProvider;
import com.biit.usermanager.core.utils.EventTags;
import com.biit.usermanager.dto.UserGroupDTO;
import com.biit.usermanager.persistence.entities.UserGroup;
import com.biit.usermanager.persistence.entities.UserGroupApplicationBackendServiceRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserGroupEventSender extends EventSender<UserGroupDTO> {

    private static final String EVENT_TYPE = "userGroups";
    public static final String REVOCATION_EVENT_TAG = "Revoked Token";

    private final UserGroupConverter userGroupConverter;

    private final UserGroupProvider userGroupProvider;


    public UserGroupEventSender(@Autowired(required = false) KafkaEventTemplate kafkaTemplate,
                                UserGroupConverter userGroupConverter, UserGroupProvider userGroupProvider) {
        super(kafkaTemplate, EventTags.USER_GROUP, EVENT_TYPE);
        this.userGroupConverter = userGroupConverter;
        this.userGroupProvider = userGroupProvider;
    }

    public void sendEvents(Collection<UserGroupApplicationBackendServiceRole> userGroupsToInform, String tag, String launchedBy) {
        sendEventsByUserGroupsId(userGroupsToInform.stream().map(userApplicationBackendServiceRole
                -> userApplicationBackendServiceRole.getId().getUserGroupId()).collect(Collectors.toSet()), tag, launchedBy);
    }

    public void sendEventsByUserGroupsId(Collection<Long> userGroupsIds, String tag, String launchedBy) {
        final List<UserGroup> users = userGroupProvider.findByIdIn(userGroupsIds);
        userGroupConverter.convertAll(users.stream().map(UserGroupConverterRequest::new).toList()).forEach(userDTO ->
                sendEvents(userDTO, EventSubject.UPDATED, tag, launchedBy));
    }
}

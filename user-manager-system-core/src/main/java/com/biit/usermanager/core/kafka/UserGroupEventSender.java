package com.biit.usermanager.core.kafka;


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

package com.biit.usermanager.core.kafka;


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

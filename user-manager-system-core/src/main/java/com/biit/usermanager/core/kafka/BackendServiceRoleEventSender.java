package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventTags;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackendServiceRoleEventSender extends EventSender<BackendServiceRoleDTO> {

    private static final String EVENT_TYPE = "backendServiceRoles";


    public BackendServiceRoleEventSender(@Autowired(required = false) KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventTags.BACKEND_ROLE, EVENT_TYPE);
    }
}

package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventTags;
import com.biit.usermanager.dto.RoleDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleEventSender extends EventSender<RoleDTO> {

    private static final String EVENT_TYPE = "roles";


    public RoleEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventTags.ROLE, EVENT_TYPE);
    }
}

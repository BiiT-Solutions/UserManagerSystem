package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventTags;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRolesEventSender extends EventSender<ApplicationRoleDTO> {

    private static final String EVENT_TYPE = "applicationRoles";


    public ApplicationRolesEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventTags.ROLE, EVENT_TYPE);
    }
}

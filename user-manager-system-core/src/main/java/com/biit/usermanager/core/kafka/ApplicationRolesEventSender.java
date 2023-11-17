package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventUtils;
import com.biit.usermanager.dto.ApplicationRoleDTO;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRolesEventSender extends EventSender<ApplicationRoleDTO> {

    private static final String EVENT_TYPE = "applicationRoles";


    public ApplicationRolesEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventUtils.TAG, EVENT_TYPE);
    }
}

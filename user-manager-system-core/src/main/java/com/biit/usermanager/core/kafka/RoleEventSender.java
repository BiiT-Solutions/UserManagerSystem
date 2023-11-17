package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventUtils;
import com.biit.usermanager.dto.RoleDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleEventSender extends EventSender<RoleDTO> {

    private static final String EVENT_TYPE = "roles";


    public RoleEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventUtils.TAG, EVENT_TYPE);
    }
}

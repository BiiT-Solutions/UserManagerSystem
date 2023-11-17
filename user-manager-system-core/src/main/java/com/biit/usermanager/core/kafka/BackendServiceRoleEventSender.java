package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventUtils;
import com.biit.usermanager.dto.BackendServiceRoleDTO;
import org.springframework.stereotype.Component;

@Component
public class BackendServiceRoleEventSender extends EventSender<BackendServiceRoleDTO> {

    private static final String EVENT_TYPE = "backendServiceRoles";


    public BackendServiceRoleEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventUtils.TAG, EVENT_TYPE);
    }
}

package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.dto.ApplicationBackendServiceRoleDTO;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBackendServiceRoleEventSender extends EventSender<ApplicationBackendServiceRoleDTO> {

    private static final String EVENT_TYPE = "applicationBackendServiceRoles";


    public ApplicationBackendServiceRoleEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EVENT_TYPE);
    }
}

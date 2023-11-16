package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.dto.BackendServiceDTO;
import org.springframework.stereotype.Component;

@Component
public class BackendServiceEventSender extends EventSender<BackendServiceDTO> {

    private static final String EVENT_TYPE = "backendServices";


    public BackendServiceEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EVENT_TYPE);
    }
}

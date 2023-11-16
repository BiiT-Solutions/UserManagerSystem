package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserEventSender extends EventSender<UserDTO> {

    private static final String EVENT_TYPE = "users";


    public UserEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EVENT_TYPE);
    }
}

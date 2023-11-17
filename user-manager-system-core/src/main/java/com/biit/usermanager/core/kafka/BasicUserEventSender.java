package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventUtils;
import com.biit.usermanager.dto.BasicUserDTO;
import org.springframework.stereotype.Component;

@Component
public class BasicUserEventSender extends EventSender<BasicUserDTO> {

    private static final String EVENT_TYPE = "users";


    public BasicUserEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventUtils.TAG, EVENT_TYPE);
    }
}

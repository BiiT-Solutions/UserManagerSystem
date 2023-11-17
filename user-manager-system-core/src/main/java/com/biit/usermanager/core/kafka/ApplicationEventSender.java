package com.biit.usermanager.core.kafka;


import com.biit.kafka.events.EventSender;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.usermanager.core.utils.EventUtils;
import com.biit.usermanager.dto.ApplicationDTO;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventSender extends EventSender<ApplicationDTO> {

    private static final String EVENT_TYPE = "applications";


    public ApplicationEventSender(KafkaEventTemplate kafkaTemplate) {
        super(kafkaTemplate, EventUtils.TAG, EVENT_TYPE);
    }
}
